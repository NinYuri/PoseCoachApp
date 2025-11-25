package com.example.posecoach.userScreens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.example.posecoach.R
import com.example.posecoach.components.ContinueButton
import com.example.posecoach.components.GenericTextField
import com.example.posecoach.data.model.LoginRequest
import com.example.posecoach.data.viewModel.LoginViewModel
import com.example.posecoach.ui.theme.colorDark
import com.example.posecoach.ui.theme.colorError
import com.example.posecoach.ui.theme.colorSec
import com.example.posecoach.ui.theme.colorWhite
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(navController: NavController, loginViewModel: LoginViewModel) {
    // BACKEND
    val loading = loginViewModel.loading.value
    val viewModelMensaje = loginViewModel.mensaje.value
    val viewModelError = loginViewModel.error.value
    val isLoggedIn = loginViewModel.isLoggedIn.value

    // LOGIN - Animación
    val minOffset = 0f
    val maxOffset = 630f
    val loginHeight = 680.dp
    val dragSensitivity = 0.5f  // Velocidad
    var offsetY by remember { mutableStateOf(maxOffset) }
    val progress = 1f - (offsetY - minOffset) / (maxOffset - minOffset)     // Progreso

    // LOGIN - Campos
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // WELCOME - Opacidad del texto
    val textOpacity by animateFloatAsState(
        targetValue = if (progress < 0.1f) 1f else 0f,   // Desaparece al 10%
        animationSpec = tween(durationMillis = 300)
    )

    // WELCOME - Rotación de las flechas
    val rotation by animateFloatAsState(
        targetValue = progress * 180f,
        animationSpec = tween(durationMillis = 500)
    )

    // ERROR - Estados
    var usernameError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Observar si el login fue exitoso
    LaunchedEffect(isLoggedIn) {
        if(isLoggedIn) {
            delay(300L)
            navController.navigate("home") {
                popUpTo("welcome") { inclusive = true }
            }
        }
    }

    // Errores
    LaunchedEffect(viewModelError) {
        if(viewModelError.isNotEmpty()) {
            usernameError = true
            passwordError = true
            Toast.makeText(context, viewModelError, Toast.LENGTH_SHORT).show()
            loginViewModel.clearMessages()
        }
    }

    // Mensajes exitosos
    LaunchedEffect(viewModelMensaje) {
        if(viewModelMensaje.isNotEmpty() && viewModelError.isEmpty()) {
            Toast.makeText(context, viewModelMensaje, Toast.LENGTH_SHORT).show()
            loginViewModel.clearMessages()
        }
    }

    fun Validate(): Boolean {
        return when {
            username.isBlank() -> {
                usernameError = true
                errorMessage = "Por favor, escribe tu nombre de usuario."
                false
            }
            password.isBlank() -> {
                passwordError = true
                errorMessage = "Por favor, escribe tu contraseña."
                false
            }
            else -> {
                usernameError = false
                passwordError = false
                errorMessage = ""
                true
            }
        }
    }

    fun clearErrors() {
        usernameError = false
        passwordError = false
        errorMessage = ""
    }

    // Realizar Login
    fun performLogin() {
        if(Validate()) {
            loginViewModel.Login(
                LoginRequest(
                    identificador = username.trim(),
                    password = password
                )
            )
        } else
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.welcome),
                contentDescription = "Welcome",
                modifier = Modifier
                    .fillMaxSize()
                    .blur(progress * 4.dp),
                contentScale = ContentScale.Fit
            )
        }

        // LOGIN
        Box(
            modifier = Modifier
                .align(Alignment.Companion.BottomCenter)
                .fillMaxWidth()
                .height(loginHeight)
                .offset(y = offsetY.dp)
                .background(
                    Color.Black.copy(alpha = 0.7f),
                    RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(
                        brush = Brush.Companion.verticalGradient(
                            colors = listOf(
                                colorSec,
                                colorSec.copy(alpha = 0f)
                            )
                        ),
                        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
            )

            if (offsetY < maxOffset * 0.8f) {
                AnimatedVisibility(
                    visible = offsetY < maxOffset * 0.8f,
                    enter = fadeIn(animationSpec = tween(600)) +
                            slideInVertically(initialOffsetY = { it / 3 }), // entra desde abajo
                    exit = fadeOut(animationSpec = tween(400)) +
                            slideOutVertically(targetOffsetY = { it / 3 })   // se va hacia abajo
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp, 0.dp),
                        horizontalAlignment = Alignment.Companion.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier.offset(y = (-5).dp),
                            contentAlignment = Alignment.Companion.Center
                        ) {
                            // Stroke
                            Text(
                                "BIENVENIDO",
                                color = colorWhite,
                                fontSize = 50.sp,
                                fontFamily = FontFamily(Font(R.font.comfortaa)),
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(
                                    drawStyle = Stroke(
                                        width = 6f,
                                        join = StrokeJoin.Round
                                    )
                                )
                            )

                            // Relleno
                            Text(
                                "BIENVENIDO",
                                color = colorWhite,
                                fontSize = 50.sp,
                                fontFamily = FontFamily(Font(R.font.comfortaa)),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            "Ingresa para continuar con tu progreso",
                            color = colorWhite,
                            fontSize = 17.sp,
                            fontFamily = FontFamily(Font(R.font.figtree)),
                            fontWeight = FontWeight.Normal
                        )

                        Spacer(modifier = Modifier.height(53.dp))
                        GenericTextField(
                            value = username,
                            onValueChange = {
                                clearErrors()
                                username = it
                            },
                            placeholder = "Nombre de Usuario",
                            isError = usernameError,
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.user_wine),
                                    contentDescription = "User Icon",
                                    modifier = Modifier.size(32.dp),
                                    tint = if(usernameError) colorError else colorDark
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(30.dp))
                        GenericTextField(
                            value = password,
                            onValueChange = {
                                clearErrors()
                                password = it
                            },
                            placeholder = "Contraseña",
                            isError = passwordError,
                            keyboardType = KeyboardType.Password,
                            visualTransformation =
                                if(passwordVisible)
                                    VisualTransformation.None
                                else
                                    PasswordVisualTransformation(),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.lock_wine),
                                    contentDescription = "Password Icon",
                                    modifier = Modifier.size(25.dp),
                                    tint = if(passwordError) colorError else colorDark
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { passwordVisible = !passwordVisible },
                                    modifier = Modifier.size(26.dp)
                                ){
                                    Icon(
                                        painter =
                                            if (passwordVisible)
                                                painterResource(id = R.drawable.open_wine)
                                            else
                                                painterResource(id = R.drawable.closed_wine),
                                        contentDescription = "Password Toggle",
                                        modifier = Modifier.size(26.dp),
                                        tint = if(passwordError) colorError else colorDark
                                    )
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(13.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ){
                            Text(
                                "¿Olvidaste tu contraseña?",
                                color = colorWhite,
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.figtree)),
                                fontWeight = FontWeight.Light,
                                modifier = Modifier.clickable {
                                    loginViewModel.clearMessages()
                                    navController.navigate("forgotPass")
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(230.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.welc_shadow),
                                contentDescription = "Shadow",
                                modifier = Modifier
                                    .size(228.dp)
                                    .offset(x = (-13).dp)
                            )

                            ContinueButton(
                                onClick = { performLogin() },
                                modifier = Modifier.offset(y = (165).dp),
                                text = "INICIAR SESIÓN"
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))
                        Box(contentAlignment = Alignment.Companion.Center) {
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            color = colorWhite,
                                            fontWeight = FontWeight.Light
                                        )
                                    ) {
                                        append("¿No tienes cuenta? ")
                                    }

                                    withStyle(
                                        style = SpanStyle(
                                            color = colorSec,
                                            fontWeight = FontWeight.Bold,
                                            drawStyle = Stroke(width = 1.5f)
                                        )
                                    ) {
                                        append("Regístrate")
                                    }
                                },
                                fontSize = 15.sp,
                                fontFamily = FontFamily(Font(R.font.figtree))
                            )

                            var annotatedText = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = colorWhite,
                                        fontWeight = FontWeight.Light
                                    )
                                ) {
                                    append("¿No tienes cuenta? ")
                                }

                                pushStringAnnotation("REGISTER", "register")
                                withStyle(
                                    style = SpanStyle(
                                        color = colorSec,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("Regístrate")
                                }
                                pop()
                            }

                            ClickableText(
                                text = annotatedText,
                                onClick = { offset ->
                                    annotatedText.getStringAnnotations("REGISTER", offset, offset)
                                        .firstOrNull()?.let {
                                            navController.navigate("register")
                                        }
                                },
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    fontFamily = FontFamily(Font(R.font.figtree))
                                )
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.Companion.BottomCenter)
                    .fillMaxWidth()
                    .height(25.dp)
                    .background(
                        brush = Brush.Companion.verticalGradient(
                            colors = listOf(
                                colorSec.copy(alpha = 0f),
                                colorSec
                            )
                        ),
                        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
            )
        }

        // FLECHAS
        Box(
            modifier = Modifier
                .align(Alignment.Companion.BottomCenter)
                .offset(y = offsetY.dp - 695.dp)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { change, dragAmount ->
                        val newOffset = offsetY + (dragAmount * dragSensitivity)
                        offsetY = newOffset.coerceIn(minOffset, maxOffset)

                        if(offsetY == maxOffset || offsetY == minOffset) {
                            username = ""
                            password = ""
                            clearErrors()
                        }
                    }
                }
        ) {
            Column(
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(-15.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.arrow),
                    contentDescription = "Arrow",
                    modifier = Modifier
                        .size(34.dp)
                        .rotate(rotation),
                    contentScale = ContentScale.Fit,
                )

                Image(
                    painter = painterResource(id = R.drawable.arrow),
                    contentDescription = "Arrow",
                    modifier = Modifier
                        .size(34.dp)
                        .rotate(rotation),
                    contentScale = ContentScale.Fit
                )
            }
        }

        // WELCOME
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 165.dp)
                .graphicsLayer { alpha = textOpacity },
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Companion.CenterHorizontally
        ){
            // SWIPE UP (Superpuesto)
            Box( contentAlignment = Alignment.Companion.Center ) {
                // Stroke
                Text(
                    "SWIPE UP",
                    color = colorWhite,
                    fontSize = 68.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    style = TextStyle(
                        drawStyle = Stroke(
                            width = 20f,
                            join = StrokeJoin.Round
                        ),
                        letterSpacing = 0.04.em     // 4%
                    )
                )

                // Relleno
                Text(
                    "SWIPE UP",
                    color = colorWhite,
                    fontSize = 68.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    style = TextStyle(
                        letterSpacing = 0.04.em
                    )
                )
            }

            Box( contentAlignment = Alignment.Companion.Center ){
                // Stroke
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("y alcanza tu ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorSec,
                                drawStyle = Stroke(width = 2.5f)
                            )
                        ) {
                            append("mejor versión")
                        }
                    },
                    fontSize = 26.sp,
                    fontFamily = FontFamily(Font(R.font.figtree))
                )

                // Relleno
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = colorWhite
                            )
                        ) {
                            append("y alcanza tu ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorSec
                            )
                        ) {
                            append("mejor versión")
                        }
                    },
                    fontSize = 26.sp,
                    fontFamily = FontFamily(Font(R.font.figtree))
                )
            }
        }
    }
}