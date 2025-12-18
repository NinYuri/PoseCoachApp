package com.example.posecoach.homeScreens

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.camera.core.Preview
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.posecoach.R
import com.example.posecoach.components.HomeMenu
import com.example.posecoach.data.viewModel.SelectedExVM
import com.example.posecoach.ui.theme.colorDark
import com.example.posecoach.ui.theme.colorDarker
import com.example.posecoach.ui.theme.colorPrin
import com.example.posecoach.ui.theme.colorSec
import com.example.posecoach.ui.theme.colorWhite
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mediapipe.tasks.core.BaseOptions
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@Composable
fun CameraScreen(navController: NavController, selectedExVM: SelectedExVM) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    // Camara
    var recording: Recording? by remember { mutableStateOf(null) }
    var isRecording by remember { mutableStateOf(false) }
    var videoCapture by remember { mutableStateOf<VideoCapture<Recorder>?>(null) }
    var useFrontCamera by remember { mutableStateOf(true) }
    val previewView = remember { PreviewView(context) }

    // Estados para MediaPipe
    var poseLandmarker by remember { mutableStateOf<PoseLandmarker?>(null) }
    var poseResult by remember { mutableStateOf<PoseLandmarkerResult?>(null) }
    var frameWidth by remember { mutableStateOf(0) }
    var frameHeight by remember { mutableStateOf(0) }

    // Feedback
    var feedbackMessage by remember { mutableStateOf("") }
    var isBodyStable by remember { mutableStateOf(false) }
    var stableFrameCount by remember { mutableStateOf(0) }
    var showFeedDialog by remember { mutableStateOf(false) }
    var lastFeedbackTime by remember { mutableStateOf(0L) }
    var lastFeedbackText by remember { mutableStateOf("") }
    val FEEDBACK_COOLDOWN = 2000L
    var errorLandmarks by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var errorStartTime by remember { mutableStateOf<Long?>(null) }
    val ERROR_HOLD_TIME = 100L

    LaunchedEffect(showFeedDialog) {
        if (showFeedDialog) {
            delay(2000)
            showFeedDialog = false
            feedbackMessage = ""
            errorLandmarks = emptySet()
        }
    }


    // Función para calcular ángulo entre 3 puntos
    fun calculateAngle(a: NormalizedLandmark, b: NormalizedLandmark, c: NormalizedLandmark): Float {
        val ab = Pair(b.x() - a.x(), b.y() - a.y())
        val cb = Pair(b.x() - c.x(), b.y() - c.y())
        val dot = ab.first * cb.first + ab.second * cb.second
        val cross = ab.first * cb.second - ab.second * cb.first
        return Math.toDegrees(Math.atan2(cross.toDouble(), dot.toDouble())).toFloat().absoluteValue
    }

    // Procesar los resultados de la detección de pose y comparar con reglas del ejercicio
    fun processPoseResults(result: PoseLandmarkerResult) {
        // --------- estabilidad del cuerpo ---------
        val landmarkList = result.landmarks()
        if(landmarkList.isEmpty()) {
            stableFrameCount = 0
            isBodyStable = false
            return
        }

        stableFrameCount++
        if(stableFrameCount < 20) return
        isBodyStable = true

        // --------- evaluar si el cuerpo está estable ---------
        if(!isBodyStable) return

        val rules = selectedExVM.exerciseRules.value ?: return
        val landmarks = landmarkList[0]
        var feedbackText = ""
        val errorPoints = mutableSetOf<Int>()

        rules.ideal_angles.forEach { (angleName, rule) ->
            if(rule.landmarks.size < 2) return@forEach

            val measuredAngle = if(rule.landmarks.size == 2) {
                // Línea simple
                val dy = landmarks[rule.landmarks[1]].y() - landmarks[rule.landmarks[0]].y()
                val dx = landmarks[rule.landmarks[1]].x() - landmarks[rule.landmarks[0]].x()
                Math.toDegrees(Math.atan2(dy.toDouble(), dx.toDouble())).toFloat().absoluteValue
            } else {
                // Ángulo real de 3 puntos
                calculateAngle(
                    landmarks[rule.landmarks[0]],
                    landmarks[rule.landmarks[1]],
                    landmarks[rule.landmarks[2]]
                )
            }

            // Verificación con min/max y errores
            val mistakes = rules.common_mistakes.filter {
                (it.condicion.contains("<") && measuredAngle < rule.min) ||
                (it.condicion.contains(">") && measuredAngle > rule.max)
            }
            for(m in mistakes) {
                feedbackText += "${angleName}: ${m.error}\n"

                rule.landmarks.forEach { index ->
                    errorPoints.add(index)
                }
            }
        }

        val currentTime = System.currentTimeMillis()
        if (feedbackText.isNotBlank()) {
            // Si es la primera vez que aparece el error, iniciar conteo
            if (errorStartTime == null)
                errorStartTime = currentTime

            // Si el error se mantuvo 2 segundos
            if (currentTime - errorStartTime!! >= ERROR_HOLD_TIME) {
                // Mostrar feedback SOLO UNA VEZ por error sostenido
                if (feedbackText != lastFeedbackText) {
                    feedbackMessage = feedbackText
                    showFeedDialog = true
                    lastFeedbackText = feedbackText
                }
            }
        } else {
            // El error desapareció → resetear
            errorStartTime = null
            lastFeedbackText = ""
            showFeedDialog = false
        }

        errorLandmarks =
            if(feedbackText.isNotBlank()) errorPoints
            else emptySet()
    }

    // Inicializar MediaPipe Pose
    fun initializeMediaPipePose(context: Context) {
        try {
            val base = BaseOptions.builder()
                .setModelAssetPath("pose_landmarker_full.task")
                .build()

            val options = PoseLandmarker.PoseLandmarkerOptions.builder()
                .setBaseOptions(base)
                .setRunningMode(RunningMode.LIVE_STREAM)
                .setResultListener { result, _ -> poseResult = result; processPoseResults(result) } // CAMBIO: procesamos pose
                .setErrorListener { error -> Log.e("PoseCoach", "Error MediaPipe: $error") }
                .build()

            poseLandmarker = PoseLandmarker.createFromOptions(context, options)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al inicializar MediaPipe", Toast.LENGTH_SHORT).show()
        }
    }

    // Procesar frame para detección de pose
    fun processFrameForPose(imageProxy: ImageProxy) {
        val detector = poseLandmarker ?: return imageProxy.close()
        try {
            var bitmap = imageProxy.toBitmap().rotate(imageProxy.imageInfo.rotationDegrees.toFloat())
            frameWidth = bitmap.width
            frameHeight = bitmap.height
            val mpImage = BitmapImageBuilder(bitmap).build()
            detector.detectAsync(mpImage, imageProxy.imageInfo.timestamp)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            imageProxy.close()
        }
    }

    // Pedir permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            // Verificar permisos e inicializar MediaPipe
            if(permissions.all { it.value })
                initializeMediaPipePose(context)
            else
                Toast.makeText(context, "Lo siento, permisos denegados", Toast.LENGTH_SHORT).show()
        }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
        )
    }

    fun toggleRecording() {
        val capture = videoCapture ?: return

        // Verificar permisos
        val hasCameraPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        val hasAudioPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if(!hasCameraPermission || !hasAudioPermission) {
            Toast.makeText(context, "Por favor, otorga permisos de cámara y micrófono", Toast.LENGTH_SHORT).show()
            return
        }

        if(!isRecording) {
            val name = "video_${System.currentTimeMillis()}.mp4"

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Compose")
            }

            val outputOptions = MediaStoreOutputOptions.Builder(
                context.contentResolver,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            ).setContentValues(contentValues).build()

            val recordingInstance = capture.output
                .prepareRecording(context, outputOptions)
                .apply { if(hasAudioPermission) withAudioEnabled() }
                .start(ContextCompat.getMainExecutor(context)) { event ->
                    if(event is VideoRecordEvent.Finalize)
                        Toast.makeText(context, "Vídeo guardado en galería", Toast.LENGTH_SHORT).show()
                }

            recording = recordingInstance
            isRecording = true
        } else {
            recording?.stop()
            recording = null
            isRecording = false
        }
    }

    // Renderizar Camera
    val cameraSelector = if(useFrontCamera)
        CameraSelector.DEFAULT_FRONT_CAMERA
    else
        CameraSelector.DEFAULT_BACK_CAMERA

    LaunchedEffect(useFrontCamera) {
        stableFrameCount = 0
        isBodyStable = false

        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        // Configurar ImageAnalysis para MediaPipe
        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()
            .also {
                it.setAnalyzer(ContextCompat.getMainExecutor(context)) { frame ->
                    processFrameForPose(frame)
                }
            }

        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HD))
            .build()

        val capture = VideoCapture.withOutput(recorder)

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            capture,
            imageAnalysis
        )

        videoCapture = capture
    }


    Box( modifier = Modifier.fillMaxSize() )
    {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // Overlay para los landmarks
        PoseOverlay(
            poseResult = poseResult,
            frameWidth = frameWidth,
            frameHeight = frameHeight,
            isFrontCamera = useFrontCamera,
            errorLandmarks = errorLandmarks
        )

        // Flip Camera
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, end = 20.dp),
            contentAlignment = Alignment.TopEnd
        ){
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(colorDark, CircleShape),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(id = R.drawable.switch_cam),
                    contentDescription = "Flip Camera",
                    modifier = Modifier
                        .size(33.dp)
                        .clickable { useFrontCamera = !useFrontCamera },
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(colorWhite)
                )
            }
        }

        // Mostrar feedback de errores
        if(showFeedDialog) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {},
                dismissButton = {},
                title = {
                    Text(
                        "Corrección",
                        color = colorSec,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.figtree)),
                        fontWeight = FontWeight.Bold,
                    )
                },
                text = {
                    Text(
                        feedbackMessage,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.figtree)),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            )
        }
    }

    HomeMenu(
        navController = navController,
        selectedOpt = "camera",
        isRecording = isRecording,
        onRecordClick = { toggleRecording() }
    )
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = android.graphics.Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

// Conversion a MPImage
fun ImageProxy.toMPImage(): MPImage {
    val bitmap = this.toBitmap()
    return BitmapImageBuilder(bitmap).build()
}

@Composable
fun PoseOverlay(
    poseResult: PoseLandmarkerResult?,
    frameWidth: Int,
    frameHeight: Int,
    isFrontCamera: Boolean,
    errorLandmarks: Set<Int>
){
    if(poseResult == null) return

    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
        val landmarksList = poseResult.landmarks()
        if(landmarksList.isEmpty()) return@Canvas
        val landmarks = landmarksList[0]
        val widthScale = size.width
        val heightScale = size.height

        fun lx(norm: Float): Float {
            val scale = widthScale / frameWidth
            val offsetX = (widthScale - frameWidth * scale) / 2f
            val x = offsetX + norm * frameWidth * scale

            return if (isFrontCamera) widthScale - x else x
        }
        fun ly(norm: Float) = (heightScale - frameHeight * (heightScale / frameHeight)) / 2f + norm * frameHeight * (heightScale / frameHeight)

        // Dibujo de puntos
        landmarks.forEachIndexed { index, lm ->
            drawCircle(
                color = if (errorLandmarks.contains(index)) Color.Red else colorSec,
                radius = if (errorLandmarks.contains(index)) 9f else 6f,
                center = Offset(lx(lm.x()), ly(lm.y()))
            )
        }

        // Conexiones
        val connections = listOf(
            11 to 12, 11 to 13, 13 to 15, 12 to 14, 14 to 16, 11 to 23, 12 to 24,
            23 to 24, 23 to 25, 25 to 27, 27 to 31, 24 to 26, 26 to 28, 28 to 32
        )
        for((start, end) in connections) {
            if(start < landmarks.size && end < landmarks.size) {
                val a = landmarks[start]
                val b = landmarks[end]
                drawLine(color = colorDarker, start = androidx.compose.ui.geometry.Offset(lx(a.x()), ly(a.y())),
                    end = androidx.compose.ui.geometry.Offset(lx(b.x()), ly(b.y())), strokeWidth = 4f)
            }
        }
    }
}