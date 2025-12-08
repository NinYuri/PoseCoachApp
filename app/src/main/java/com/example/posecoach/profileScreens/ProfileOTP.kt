package com.example.posecoach.profileScreens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.posecoach.R
import com.example.posecoach.components.ContinueButton
import com.example.posecoach.components.ScreenLoader
import com.example.posecoach.data.model.ResendOtp
import com.example.posecoach.data.viewModel.EmPhViewModel
import com.example.posecoach.ui.theme.colorError
import com.example.posecoach.ui.theme.colorPrin
import com.example.posecoach.ui.theme.colorSec
import com.example.posecoach.ui.theme.colorWhite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileOTP(navController: NavController, verificationType: VerificationType, emPhViewModel: EmPhViewModel) {
    val loading = emPhViewModel.loading.value
    val viewModelMensaje = emPhViewModel.mensaje.value
    val viewModelError = emPhViewModel.error.value

    var otpcode by remember { mutableStateOf("") }
    var otpcodeError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    var timeRemaining by remember { mutableStateOf(600) }
    var timerJob by remember { mutableStateOf<Job?>(null) }
    var isResend by remember { mutableStateOf(false) }

    LaunchedEffect(viewModelError) {
        if(viewModelError.isNotEmpty()) {
            otpcodeError = true
            Toast.makeText(context, viewModelError, Toast.LENGTH_SHORT).show()
            emPhViewModel.clearMessages()
        }
    }

    LaunchedEffect(viewModelMensaje, viewModelError) {
        if(viewModelMensaje.isNotEmpty() && viewModelError.isEmpty()) {
            if(isResend) {
                Toast.makeText(context, viewModelMensaje, Toast.LENGTH_SHORT).show()
                isResend = false
            } else {
                Toast.makeText(context, viewModelMensaje, Toast.LENGTH_SHORT).show()
                delay(500)
                navController.navigate("account") {
                    popUpTo("profileOTP") { inclusive = true }
                }
                emPhViewModel.clearFields()
            }
            emPhViewModel.clearMessages()
        }
    }

    // INICIAR TIMER
    LaunchedEffect(Unit) {
        timerJob = launch {
            while (timeRemaining > 0) {
                delay(1000L)
                timeRemaining--
            }
        }
    }

    fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    fun resendCode() {
        isResend = true
        timerJob?.cancel()
        timeRemaining = 600
        timerJob = CoroutineScope(Dispatchers.Main).launch {
            while (timeRemaining > 0) {
                delay(1000L)
                timeRemaining--
            }
        }

        emPhViewModel.resendProfileOTP(
            ResendOtp(
                email = emPhViewModel.userEmail.value,
                phone = emPhViewModel.userPhone.value
            )
        )
    }

    fun Validate(): Boolean {
        return when {
            otpcode.isBlank() -> {
                otpcodeError = true
                errorMessage = "Por favor, escribe tu código OTP para verificar tu nuevo correo."
                false
            }
            otpcode.length != 6 -> {
                otpcodeError = true
                errorMessage = "Por favor, escribe todas las cifras de tu código correctamente."
                false
            }
            else -> {
                otpcodeError = false
                errorMessage = ""
                true
            }
        }
    }

    fun verifyOTP() {
        when(verificationType) {
            is VerificationType.EMAIL -> emPhViewModel.verifyEmailOTP(otpcode)
            is VerificationType.PHONE -> emPhViewModel.verifyPhoneOTP(otpcode)
        }
    }

    fun getBack(): String {
        return when (verificationType) {
            is VerificationType.EMAIL -> "changeEmail"
            is VerificationType.PHONE -> "changePhone"
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
                contentDescription = "OTP Verification",
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
                        .clickable { navController.navigate(getBack()) },
                    contentScale = ContentScale.Fit
                )
            }

            Box( modifier = Modifier.padding(top = 17.dp, end = 13.dp) ){
                Text(
                    "VERIFICAR",
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
                    "VERIFICAR",
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
                    "IDENTIDAD",
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
                    "IDENTIDAD",
                    color = colorPrin,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer( modifier = Modifier.height(200.dp) )
            OutlinedTextField(
                value = otpcode,
                onValueChange = {
                    if(otpcodeError) {
                        otpcodeError = false
                        errorMessage = ""
                    }

                    if(it.length <= 6)
                        otpcode = it
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
                        "― ― ― ― ― ―",
                        color = if(otpcodeError) colorError else colorWhite,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily(Font(R.font.figtree)),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                textStyle = LocalTextStyle.current.merge(
                    TextStyle(
                        color = if(otpcodeError) colorError else colorWhite,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily(Font(R.font.figtree)),
                        textAlign = TextAlign.Center
                    )
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = if(otpcodeError) colorError else colorWhite,
                    unfocusedTextColor = if(otpcodeError) colorError else colorWhite,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = colorWhite,
                    focusedContainerColor = colorPrin.copy(alpha = 0.6f),
                    unfocusedContainerColor = colorPrin.copy(alpha = 0.6f),
                ),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = null,
                leadingIcon = null
            )

            Spacer( modifier = Modifier.height(15.dp) )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp),
                contentAlignment = Alignment.TopEnd
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    if(timeRemaining > 0) {
                        Text(
                            "Reenviar código en ${formatTime(timeRemaining)}",
                            color = colorWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,
                            fontFamily = FontFamily(Font(R.font.figtree)),
                            textAlign = TextAlign.Start
                        )
                    }

                    Spacer( modifier = Modifier.weight(1f) )
                    Box(
                        modifier = Modifier.clickable( onClick = { resendCode() } ),
                        contentAlignment = Alignment.CenterEnd
                    ){
                        Text(
                            "Reenviar código",
                            color = colorSec,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.figtree)),
                            textAlign = TextAlign.End,
                            style = TextStyle(
                                drawStyle = Stroke( width = 2f )
                            )
                        )

                        Text(
                            "Reenviar código",
                            color = colorSec,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.figtree)),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }

            Spacer( modifier = Modifier.height(294.dp) )
            ContinueButton(
                text = "VERIFICAR",
                onClick = {
                    if(Validate()) {
                        verifyOTP()
                    } else
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(0.965f)
            )
        }
    }
}

sealed class VerificationType {
    object EMAIL : VerificationType()
    object PHONE : VerificationType()

    companion object {
        fun fromString(type: String): VerificationType {
            return when (type.uppercase()) {
                "EMAIL" -> EMAIL
                "PHONE" -> PHONE
                else -> EMAIL
            }
        }
    }
}