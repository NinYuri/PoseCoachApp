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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.posecoach.R
import com.example.posecoach.components.ContinueButton
import com.example.posecoach.components.GenericTextField
import com.example.posecoach.data.model.UpdateRequest
import com.example.posecoach.data.viewModel.ProfileViewModel
import com.example.posecoach.data.viewModel.UserViewModel
import com.example.posecoach.ui.theme.colorPrin
import com.example.posecoach.ui.theme.colorWhite

@Composable
fun UserChange(navController: NavController, profileViewModel: ProfileViewModel, userViewModel: UserViewModel) {
    var selectedUsername by remember { mutableStateOf("") }

    // BACKEND USERNAME
    val checkUsername = userViewModel.checkUsername.value
    val usernameAvailable = userViewModel.usernameAvailable.value
    val usernameMessage = userViewModel.usernameMessage.value
    var lastVerifiedUsername by remember { mutableStateOf("") }

    // BACKEND PROFILE
    val message = profileViewModel.updateResult.value
    val error = profileViewModel.error.value

    val context = LocalContext.current
    var usernameError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(profileViewModel.selectedUsername.value) {
        selectedUsername = profileViewModel.selectedUsername.value
    }

    LaunchedEffect(message) {
        if(!message.isNullOrEmpty()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            profileViewModel.updateResult.value = null
        }
    }

    LaunchedEffect(error) {
        if(error.isNotEmpty()) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            profileViewModel.error.value = ""
        }
    }

    fun Validate(): Boolean {
        return when {
            selectedUsername.isBlank() -> {
                usernameError = true
                errorMessage = "Lo siento, tu nuevo nombre de usuario no puede estar vacío."
                false
            }
            selectedUsername.trim().length < 5 -> {
                usernameError = true
                errorMessage = "Tu nuevo nombre de usuario debe tener al menos 5 caracteres."
                false
            }
            selectedUsername.trim().length > 20 -> {
                usernameError = true
                errorMessage = "Tu nuevo nombre de usuario es demasiado largo."
                false
            }
            !selectedUsername.matches(Regex("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+$")) -> {
                usernameError = true
                errorMessage = "Tu nuevo nombre de usuario sólo puede contener letras y números."
                false
            }
            checkUsername -> {
                errorMessage = "Verificando nombre de usuario..."
                false
            }
            selectedUsername.trim() != lastVerifiedUsername -> {
                errorMessage = "Por favor, espere a que termine la verificación de disponibilidad."
                false
            }
            !usernameAvailable -> {
                usernameError = true
                errorMessage = usernameMessage.ifEmpty { "Lo siento, el nuevo nombre de usuario no está disponible" }
                false
            } else -> {
                usernameError = false
                errorMessage = ""
                true
            }
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
                .padding(top = 160.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.otp_pass),
                contentDescription = "Username Change",
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
                        .clickable { navController.navigate("account") },
                    contentScale = ContentScale.Fit
                )
            }

            Box( modifier = Modifier.padding(top = 17.dp, end = 13.dp) ){
                Text(
                    "CAMBIAR",
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
                    "CAMBIAR",
                    color = colorWhite,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Box( modifier = Modifier.padding(top = 10.dp, end = 10.dp) ){
                Text(
                    "NOMBRE",
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
                    "NOMBRE",
                    color = colorPrin,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(200.dp))
            GenericTextField(
                value = selectedUsername,
                onValueChange = { text ->
                    if(usernameError) {
                        usernameError = false
                        errorMessage = ""
                    }

                    selectedUsername = text

                    if(text.trim().length >= 5){
                        userViewModel.checkUsername(text.trim())
                        lastVerifiedUsername = text.trim()
                    } else {
                        userViewModel.clearUsername()
                        lastVerifiedUsername = ""
                    }
                },
                placeholder = "Nombre de Usuario",
                isError = usernameError
            )

            Spacer(modifier = Modifier.height(332.dp))
            ContinueButton(
                text = "ACTUALIZAR",
                onClick = {
                    if(Validate()){
                        profileViewModel.updateProfile(
                            UpdateRequest(
                                username = selectedUsername
                        ))
                    } else
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(0.965f)
            )
        }
    }
}