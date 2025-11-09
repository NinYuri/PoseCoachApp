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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.posecoach.components.HomeMenu
import com.example.posecoach.ui.theme.colorDarker
import com.example.posecoach.ui.theme.colorSec
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mediapipe.tasks.core.BaseOptions
import kotlin.math.abs

@Composable
fun CameraScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var recording: Recording? by remember { mutableStateOf(null) }
    var isRecording by remember { mutableStateOf(false) }
    var videoCapture by remember { mutableStateOf<VideoCapture<Recorder>?>(null) }

    // Estados para MediaPipe
    var poseLandmarker by remember { mutableStateOf<PoseLandmarker?>(null) }
    var poseResult by remember { mutableStateOf<PoseLandmarkerResult?>(null) }
    var frameWidth by remember { mutableStateOf(0) }
    var frameHeight by remember { mutableStateOf(0) }

    fun checkPostureAlignment(
        leftShoulder: NormalizedLandmark,
        rightShoulder: NormalizedLandmark
    ){
        val shoulderDifference = abs(leftShoulder.y() - rightShoulder.y())

        if (shoulderDifference > 0.1f) {
            // Postura desalineada - podrías mostrar feedback al usuario
            Log.d("PoseCoach", "¡Postura desalineada detectada! Diferencia: $shoulderDifference")
        }
    }

    // Procesar los resultados de la detección de pose
    fun processPoseResults(result: PoseLandmarkerResult) {
        if (result.landmarks().isNotEmpty()) {
            val landmarks = result.landmarks()[0]

            // Ejemplo: obtener landmarks específicos (índices según MediaPipe Pose)
            // 11: left shoulder, 12: right shoulder en el modelo completo
            if (landmarks.size > 12) {
                val leftShoulder = landmarks[11]
                val rightShoulder = landmarks[12]

                // Lógica de coaching (ángulos)
                checkPostureAlignment(leftShoulder, rightShoulder)
            }
        }
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
                .setResultListener { result, _ ->
                    poseResult = result
                    // Procesar resultados de la pose
                    processPoseResults(result)
                }
                .setErrorListener { error ->
                    Log.e("PoseCoach", "Error en MediaPipe: $error")
                }
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
            var bitmap = imageProxy.toBitmap()
            bitmap = bitmap.rotate(imageProxy.imageInfo.rotationDegrees.toFloat())

            // Guardar tamaño del frame
            frameWidth = bitmap.width
            frameHeight = bitmap.height

            val mpImage = BitmapImageBuilder(bitmap).build()

            val ts = imageProxy.imageInfo.timestamp
            detector.detectAsync(mpImage, ts) // Detectar poses
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


    Box( modifier = Modifier.fillMaxSize() )
    {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val preview = Preview.Builder().build()
                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                // Configurar ImageAnalysis para MediaPipe
                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                    .build()
                    .also {
                        it.setAnalyzer(ContextCompat.getMainExecutor(ctx)) {
                            frame -> processFrameForPose(frame)
                        }
                    }

                val recorder = Recorder.Builder()
                    .setQualitySelector(QualitySelector.from(Quality.HD))
                    .build()

                val capture = VideoCapture.withOutput(recorder)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    preview.setSurfaceProvider(previewView.surfaceProvider)

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            capture,
                            imageAnalysis
                        )
                        videoCapture = capture
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(ctx))

                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        // Overlay para los landmarks
        PoseOverlay(
            poseResult = poseResult,
            frameWidth = frameWidth,
            frameHeight = frameHeight
        )
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

// Overlay con landmarks
@Composable
fun PoseOverlay(poseResult: PoseLandmarkerResult?, frameWidth: Int, frameHeight: Int) {
    if(poseResult == null) return

    Canvas( modifier = Modifier.fillMaxSize() ){
        val landmarksList = poseResult.landmarks()
        if(landmarksList.isEmpty()) return@Canvas

        val landmarks = landmarksList[0]
        val width = size.width
        val height = size.height

        // Escalado a la pantalla
        fun lx(norm: Float): Float {
            val scale = maxOf(width / frameWidth, height / frameHeight)
            val offsetX = (width - frameWidth * scale) / 2f
            return width - (offsetX + norm * frameWidth * scale)
        }

        fun ly(norm: Float): Float {
            val scale = maxOf(width / frameWidth, height / frameHeight)
            val offsetY = (height - frameHeight * scale) / 2f
            return offsetY + norm * frameHeight * scale
        }

        // Dibujo de puntos
        for(lm in landmarks) {
            drawCircle(
                color = colorSec,
                radius = 6f,
                center = Offset(
                    x = lx(lm.x()),
                    y = ly(lm.y())
                )
            )
        }

        // Conexiones del esqueleto (LANDMARKS_doc)
        val connections = listOf(
            11 to 12,                       // hombros
            11 to 13, 13 to 15,             // brazo izquierdo
            12 to 14, 14 to 16,             // brazo derecho
            11 to 23, 12 to 24,             // torso
            23 to 24,                       // cadera
            23 to 25, 25 to 27, 27 to 31,   // pierna izquierda
            24 to 26, 26 to 28, 28 to 32    // pierna derecha
        )

        for((start, end) in connections) {
            if(start < landmarks.size && end < landmarks.size) {
                val a = landmarks[start]
                val b = landmarks[end]

                drawLine(
                    color = colorDarker,
                    start = androidx.compose.ui.geometry.Offset(lx(a.x()), ly(a.y())),
                    end = androidx.compose.ui.geometry.Offset(lx(b.x()), ly(b.y())),
                    strokeWidth = 4f
                )
            }
        }
    }
}