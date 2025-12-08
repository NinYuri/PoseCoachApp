package com.example.posecoach.profileScreens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.posecoach.R
import com.example.posecoach.components.ContinueButton
import com.example.posecoach.components.GenericTextField
import com.example.posecoach.components.ScreenLoader
import com.example.posecoach.data.viewModel.EmPhViewModel
import com.example.posecoach.data.viewModel.ProfileViewModel
import com.example.posecoach.ui.theme.colorDark
import com.example.posecoach.ui.theme.colorError
import com.example.posecoach.ui.theme.colorPrin
import com.example.posecoach.ui.theme.colorWhite
import kotlinx.coroutines.delay

@Composable
fun EmailChange(navController: NavController, hasEmail: Boolean, profileViewModel: ProfileViewModel, emPhViewModel: EmPhViewModel) {
    var selectedEmail by remember { mutableStateOf(profileViewModel.selectedEmail.value) }
    var error by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    // BACKEND
    val loading = emPhViewModel.loading.value
    val viewModelMensaje = emPhViewModel.mensaje.value
    val viewModelError = emPhViewModel.error.value

    LaunchedEffect(viewModelError) {
        if(viewModelError.isNotEmpty()) {
            error = true
            Toast.makeText(context, viewModelError, Toast.LENGTH_SHORT).show()
            emPhViewModel.clearMessages()
        }
    }

    LaunchedEffect(viewModelMensaje, viewModelError) {
        if(viewModelMensaje.isNotEmpty() && viewModelError.isEmpty()) {
            emPhViewModel.userEmail.value = selectedEmail

            Toast.makeText(context, viewModelMensaje, Toast.LENGTH_SHORT).show()
            delay(500)

            navController.navigate("profileOTP/EMAIL")
            emPhViewModel.clearMessages()
        }
    }

    fun validEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return email.matches(emailRegex)
    }

    fun Validate(): Boolean {
        return when {
            selectedEmail.isBlank() -> {
                error = true
                errorMessage = "Por favor, escribe tu nuevo correo electrónico."
                false
            }
            !validEmail(selectedEmail) -> {
                error = true
                errorMessage = "Por favor, ingresa un nuevo correo electrónico válido."
                false
            }
            else -> {
                error = false
                errorMessage = ""
                true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        ScreenLoader( isLoading = loading )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 160.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.otp_pass),
                contentDescription = "Email Change",
                modifier = Modifier
                    .size(590.dp)
                    .offset(x = (50).dp, y = (46).dp),
                contentScale = ContentScale.Fit
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp, 0.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ){
            Box(
                modifier = Modifier
                    .padding(top = 68.dp)
                    .fillMaxWidth()
                    .height(30.dp)
                    .offset(x = (-10).dp),
                contentAlignment = Alignment.CenterStart
            ){
                Image(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            navController.navigate("account")
                            emPhViewModel.clearFields()
                        },
                    contentScale = ContentScale.Fit
                )
            }

            Box( modifier = Modifier.padding(top = 17.dp, end = 13.dp) ){
                Text(
                    if(hasEmail) "CAMBIAR" else "AÑADIR",
                    color = colorWhite,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.End,
                    style = TextStyle(
                        drawStyle = Stroke(
                            4f,
                            join = StrokeJoin.Round
                        )
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    if(hasEmail) "CAMBIAR" else "AÑADIR",
                    color = colorWhite,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Box(modifier = Modifier.padding(top = 10.dp, end = 10.dp) ){
                Text(
                    "CORREO",
                    color = colorPrin,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.End,
                    style = TextStyle(
                        drawStyle = Stroke(
                            4f,
                            join = StrokeJoin.Round
                        )
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "CORREO",
                    color = colorPrin,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer( modifier = Modifier.height(200.dp) )
            GenericTextField(
                value = selectedEmail,
                onValueChange = { text ->
                    if(error) {
                        error = false
                        errorMessage = ""
                    }

                    selectedEmail = text
                },
                placeholder = "Correo Electrónico",
                isError = error,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.mail_wine),
                        contentDescription = "Mail Icon",
                        modifier = Modifier.size(22.dp),
                        tint = if(error) colorError else colorDark
                    )
                }
            )

            Spacer(modifier = Modifier.height(332.dp))
            ContinueButton(
                text = if(hasEmail) "CAMBIAR" else "AÑADIR",
                onClick = {
                    if(Validate()){
                        if(hasEmail)
                            emPhViewModel.changeEmail(selectedEmail)
                        else
                            emPhViewModel.addEmail(selectedEmail)
                    } else
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(0.965f)
            )
        }
    }
}