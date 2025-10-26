package com.example.posecoach.passwordScreens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.posecoach.R
import com.example.posecoach.components.ContinueButton
import com.example.posecoach.components.GenericTextField
import com.example.posecoach.data.viewModel.ForgotPassViewModel
import com.example.posecoach.ui.theme.colorDark
import com.example.posecoach.ui.theme.colorError
import com.example.posecoach.ui.theme.colorSec
import com.example.posecoach.ui.theme.colorWhite

@Composable
fun NewPasswordScreen(navController: NavController, forgotPassViewModel: ForgotPassViewModel) {
    // BACKEND
    val loading = forgotPassViewModel.loading.value
    val viewModelMensaje = forgotPassViewModel.mensaje.value
    val viewModelError = forgotPassViewModel.error.value
    val passUpdate = forgotPassViewModel.reset.value

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passVisible by remember { mutableStateOf(false) }
    var passConfVisible by remember { mutableStateOf(false) }

    var passError by remember { mutableStateOf(false) }
    var confError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(passUpdate) {
        if(passUpdate) {
            navController.navigate("welcome") {
                popUpTo("newPass") { inclusive = true }
            }
        }
    }

    LaunchedEffect(viewModelError) {
        if(viewModelError.isNotEmpty()) {
            Toast.makeText(context, viewModelError, Toast.LENGTH_SHORT).show()
            passError = true
            confError = true
            forgotPassViewModel.clearMessages()
        }
    }

    LaunchedEffect(viewModelMensaje) {
        if(viewModelMensaje.isNotEmpty() && viewModelError.isEmpty()) {
            Toast.makeText(context, viewModelMensaje, Toast.LENGTH_SHORT).show()
            forgotPassViewModel.clearMessages()
        }
    }

    fun Validar(): Boolean {
        val pass = password.trim()
        val conf = confirmPassword.trim()

        return when {
            pass.isEmpty() -> {
                passError = true
                errorMessage = "Por favor, escribe tu nueva contraseña."
                false
            }
            pass.length < 8 -> {
                passError = true
                errorMessage = "La nueva contraseña debe tener al menos 8 caracteres."
                false
            }

            conf.isBlank() -> {
                confError = true
                errorMessage = "Por favor, confirma tu contraseña."
                false
            }
            pass != conf -> {
                confError = true
                errorMessage = "Las contraseñas no coinciden."
                false
            }

            else -> {
                passError = false
                confError = false
                errorMessage = ""
                true
            }
        }
    }

    fun clearErrors() {
        passError = false
        confError = false
        errorMessage = ""
        forgotPassViewModel.clearMessages()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 500.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.new_pass),
                contentDescription = "New Password",
                modifier = Modifier
                    .size(400.dp)
                    .offset(y = (15).dp, x = (30).dp),
                contentScale = ContentScale.Fit
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp, 0.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 117.dp),
                contentAlignment = Alignment.Center
            ){
                Text(
                    "NUEVA CONTRASEÑA",
                    color = colorWhite,
                    fontSize = 45.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    lineHeight = 52.sp,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        drawStyle = Stroke(
                            width = 4f,
                            join = StrokeJoin.Round
                        )
                    )
                )

                Text(
                    "NUEVA CONTRASEÑA",
                    color = colorWhite,
                    fontSize = 45.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    lineHeight = 52.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                contentAlignment = Alignment.Center
            ){
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("Tu nueva contraseña debe ser ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorSec,
                                fontWeight = FontWeight.Bold,
                                drawStyle = Stroke(width = 2f)
                            )
                        ){
                            append("diferente ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("a la anterior.")
                        }
                    },
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.figtree)),
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("Tu nueva contraseña debe ser ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorSec,
                                fontWeight = FontWeight.Bold
                            )
                        ){
                            append("diferente ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("a la anterior.")
                        }
                    },
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.figtree)),
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(50.dp))
            GenericTextField(
                value = password,
                onValueChange = {
                    clearErrors()
                    password = it
                },
                placeholder = "Nueva Contraseña",
                isError = passError,
                keyboardType = KeyboardType.Password,
                visualTransformation =
                    if(passVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.lock_wine),
                        contentDescription = "Password Icon",
                        modifier = Modifier.size(25.dp),
                        tint = if(passError) colorError else colorDark
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { passVisible = !passVisible },
                        modifier = Modifier.size(26.dp)
                    ){
                        Icon(
                            painter =
                                if (passVisible)
                                    painterResource(id = R.drawable.open_wine)
                                else
                                    painterResource(id = R.drawable.closed_wine),
                            contentDescription = "Password Toggle",
                            modifier = Modifier.size(26.dp),
                            tint = if(passError) colorError else colorDark
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
            GenericTextField(
                value = confirmPassword,
                onValueChange = {
                    clearErrors()
                    confirmPassword = it
                },
                placeholder = "Confirmar Nueva Contraseña",
                isError = confError,
                keyboardType = KeyboardType.Password,
                visualTransformation =
                    if(passConfVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.lock_wine),
                        contentDescription = "Confirm Password Icon",
                        modifier = Modifier.size(25.dp),
                        tint = if(confError) colorError else colorDark
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { passConfVisible = !passConfVisible },
                        modifier = Modifier.size(26.dp)
                    ){
                        Icon(
                            painter =
                                if (passConfVisible)
                                    painterResource(id = R.drawable.open_wine)
                                else
                                    painterResource(id = R.drawable.closed_wine),
                            contentDescription = "Password Toggle",
                            modifier = Modifier.size(26.dp),
                            tint = if(confError) colorError else colorDark
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(333.dp))
            ContinueButton(
                onClick = {
                    if(Validar()) {
                        if(forgotPassViewModel.userEmail.value != "") {
                            forgotPassViewModel.passEmail.value = forgotPassViewModel.passEmail.value.copy(
                                new_password = password.trim(),
                                new_password_confirm = confirmPassword.trim()
                            )

                            val password = forgotPassViewModel.passEmail.value
                            forgotPassViewModel.resetPassEmail(password)
                        } else {
                            forgotPassViewModel.passPhone.value = forgotPassViewModel.passPhone.value.copy(
                                new_password = password.trim(),
                                new_password_confirm = confirmPassword.trim()
                            )

                            val password = forgotPassViewModel.passPhone.value
                            forgotPassViewModel.resetPassPhone(password)
                        }
                    }
                    else
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(0.95f),
                text = "ACTUALIZAR"
            )
        }
    }
}