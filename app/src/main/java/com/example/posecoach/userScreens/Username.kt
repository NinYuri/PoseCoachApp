package com.example.posecoach.userScreens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.posecoach.R
import com.example.posecoach.components.ContinueButton
import com.example.posecoach.data.viewModel.RegistroViewModel
import com.example.posecoach.data.viewModel.UserViewModel
import com.example.posecoach.ui.theme.colorError
import com.example.posecoach.ui.theme.colorPrin
import com.example.posecoach.ui.theme.colorWhite
import kotlinx.coroutines.delay

@Composable
fun UsernameScreen(navController: NavController, registroViewModel: RegistroViewModel, userViewModel: UserViewModel, temporalId: Int = 0) {
    // BACKEND
    val currentTemporalId = if(temporalId > 0) temporalId else userViewModel.temporalId.value
    val checkUsername = userViewModel.checkUsername.value
    val usernameAvailable = userViewModel.usernameAvailable.value
    val usernameMessage = userViewModel.usernameMessage.value
    var lastVerifiedUsername by remember { mutableStateOf("") }

    val context = LocalContext.current
    var username by remember{ mutableStateOf("") }
    var usernameError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Validación
    fun validarUsername(): Boolean {
        return when {
            username.isBlank() -> {
                usernameError = true
                errorMessage = "Por favor, escribe tu nombre para continuar."
                false
            }
            username.trim().length < 5 -> {
                usernameError = true
                errorMessage = "Tu nombre de usuario debe tener al menos 5 caracteres."
                false
            }
            username.trim().length > 20 -> {
                usernameError = true
                errorMessage = "Tu nombre de usuario es demasiado largo."
                false
            }
            !username.matches(Regex("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+$")) -> {
                usernameError = true
                errorMessage = "Tu nombre de usuario sólo puede contener letras y números."
                false
            }
            checkUsername -> {
                errorMessage = "Verificando nombre de usuario..."
                false
            }
            username.trim() != lastVerifiedUsername -> {
                errorMessage = "Por favor, espere a que termine la verificación de disponibilidad."
                false
            }
            !usernameAvailable -> {
                usernameError = true
                errorMessage = usernameMessage.ifEmpty { "Lo siento, el nombre de usuario no está disponible" }
                false
            }
            else -> {
                usernameError = false
                errorMessage = ""
                true
            }
        }
    }

    // Cambios en los estados del ViewModel
    LaunchedEffect(username) {
        val trimmedUsername = username.trim()

        if(trimmedUsername.length >= 5) {
            delay(600)
            userViewModel.checkUsername(trimmedUsername)
            lastVerifiedUsername = trimmedUsername
        } else {
            userViewModel.clearUsername()
            lastVerifiedUsername = ""
        }
    }

    LaunchedEffect(usernameMessage) {
        if(usernameMessage.isNotEmpty() && !usernameAvailable) {
            errorMessage = usernameMessage
            usernameError = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 270.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.otp),
                contentDescription = "Username",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp, 0.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier.padding(top = 110.dp),
                contentAlignment = Alignment.Center
            ){
                // Stroke
                Text(
                    "¿CÓMO PREFIERES QUE TE LLAME?",
                    color = colorWhite,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.Center,
                    lineHeight = 52.sp,
                    style = TextStyle(
                        drawStyle = Stroke(
                            width = 4f,
                            join = StrokeJoin.Round
                        )
                    )
                )

                // Relleno
                Text(
                    "¿CÓMO PREFIERES QUE TE LLAME?",
                    color = colorWhite,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.Center,
                    lineHeight = 52.sp
                )
            }

            Spacer(modifier = Modifier.height(35.dp))
            OutlinedTextField(
                value = username,
                onValueChange = {
                    if(usernameError) {
                        usernameError = false
                        errorMessage = ""
                    }
                    username = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(10.dp),
                        clip = false,
                        ambientColor = Color.Black.copy(alpha = 0.7f),
                        spotColor = Color.Black.copy(alpha = 0.7f)
                    ),
                placeholder = {
                    Text(
                        "Escribe aquí tu nombre",
                        color = if(usernameError) colorError else colorWhite,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.figtree)),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                textStyle = LocalTextStyle.current.merge(
                    TextStyle(
                        color = if(usernameError) colorError else colorWhite,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.figtree)),
                        textAlign = TextAlign.Center
                    )
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = if(usernameError) colorError else colorWhite,
                    unfocusedTextColor = if(usernameError) colorError else colorWhite,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = colorWhite,
                    focusedContainerColor = colorPrin.copy(alpha = 0.7f),
                    unfocusedContainerColor = colorPrin.copy(alpha = 0.7f),
                ),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                maxLines = 1,
                trailingIcon = {
                    if(checkUsername) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = colorWhite,
                            strokeWidth = 2.dp
                        )
                    }
                },
                leadingIcon = null
            )

            Spacer(modifier = Modifier.height(439.dp))
            ContinueButton(
                onClick = {
                    if (validarUsername()) {
                        registroViewModel.usuario.value = registroViewModel.usuario.value.copy(temporal_id = currentTemporalId)
                        registroViewModel.usuario.value = registroViewModel.usuario.value.copy(username = username.trim())
                        navController.navigate("gender")
                    } else
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}