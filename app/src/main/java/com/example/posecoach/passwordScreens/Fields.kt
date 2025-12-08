package com.example.posecoach.passwordScreens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.posecoach.R
import com.example.posecoach.components.ContinueButton
import com.example.posecoach.components.Country
import com.example.posecoach.components.GenericTextField
import com.example.posecoach.components.PhoneField
import com.example.posecoach.components.ScreenLoader
import com.example.posecoach.data.viewModel.ForgotPassViewModel
import com.example.posecoach.ui.theme.colorDark
import com.example.posecoach.ui.theme.colorDarker
import com.example.posecoach.ui.theme.colorError
import com.example.posecoach.ui.theme.colorPrin
import com.example.posecoach.ui.theme.colorSec
import com.example.posecoach.ui.theme.colorWhite

@Composable
fun Fields(navController: NavController, forgotPassViewModel: ForgotPassViewModel) {
    // Backend
    val loading = forgotPassViewModel.loading.value
    val viewModelMensaje = forgotPassViewModel.mensaje.value
    val viewModelError = forgotPassViewModel.error.value
    val userVerified = forgotPassViewModel.userVerified.value

    // Opciones
    var selectedOption by remember { mutableStateOf("Email") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf<Country>(Country("+52", "MX", "México")) }

    // Campos
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // Error
    var emailError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Validación
    fun validEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return email.matches(emailRegex)
    }

    fun validPhone(phone: String): Boolean {
        val regex = Regex("^\\+?[0-9\\s-]{10,20}$")
        val cleanPhone = phone.replace(Regex("[^0-9]"), "")
        return regex.matches(phone) && cleanPhone.length in 10..15
    }

    fun Validar(): Boolean {
        return when {
            selectedOption == "Email" && email.isBlank() -> {
                emailError = true
                errorMessage = "Por favor, escribe tu correo electrónico para continuar."
                false
            }
            selectedOption == "Email" && !validEmail(email) -> {
                emailError = true
                errorMessage = "Por favor, ingresa un correo electrónico válido."
                false
            }

            selectedOption == "Teléfono" && phone.isBlank() -> {
                phoneError = true
                errorMessage = "Por favor, escribe tu número telefónico para continuar."
                false
            }
            selectedOption == "Teléfono" && !validPhone(phone) -> {
                phoneError = true
                errorMessage = "Por favor, ingresa un número telefónico válido."
                false
            }
            selectedOption == "Teléfono" && phone.length > 10 -> {
                phoneError = true
                errorMessage = "Por favor, ingresa un número telefónico válido."
                false
            }

            else -> {
                emailError = false
                phoneError = false
                errorMessage = ""
                true
            }
        }
    }

    LaunchedEffect(userVerified) {
        if(userVerified)
            navController.navigate("otpPass")
    }

    LaunchedEffect(viewModelError) {
        if(viewModelError.isNotEmpty()) {
            Toast.makeText(context, viewModelError, Toast.LENGTH_SHORT).show()
            emailError = true
            phoneError = true
            forgotPassViewModel.clearMessages()
        }
    }

    LaunchedEffect(viewModelMensaje) {
        if(viewModelMensaje.isNotEmpty() && viewModelError.isEmpty()) {
            Toast.makeText(context, viewModelMensaje, Toast.LENGTH_SHORT).show()
            forgotPassViewModel.clearMessages()
        }
    }

    fun clearErrors() {
        emailError = false
        phoneError = false
        errorMessage = ""
    }

    fun resetFields() {
        email = ""
        phone = ""
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
                .padding(top = 300.dp),
            contentAlignment = Alignment.BottomStart
        ){
            Image(
                painter = painterResource(R.drawable.forgot_pass),
                contentDescription = "Forgot Password",
                modifier = Modifier
                    .size(1400.dp)
                    .offset(y = (70).dp, x = (-20).dp),
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
                    .padding(top = 68.dp)
                    .fillMaxWidth()
                    .height(30.dp)
                    .offset(x = (-10).dp),
                contentAlignment = Alignment.CenterStart
            ){
                Image(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { navController.navigate("welcome") },
                    contentScale = ContentScale.Fit
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp),
                contentAlignment = Alignment.Center
            ){
                Text(
                    "RECUPERA",
                    color = colorWhite,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    style = TextStyle(
                        drawStyle = Stroke(
                            4f,
                            join = StrokeJoin.Round
                        )
                    )
                )

                Text(
                    "RECUPERA",
                    color = colorWhite,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa))
                )
            }

            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    "TU CONTRASEÑA",
                    color = colorWhite,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    style = TextStyle(
                        drawStyle = Stroke(
                            width = 4f,
                            join = StrokeJoin.Round
                        )
                    )
                )

                Text(
                    "TU CONTRASEÑA",
                    color = colorWhite,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa))
                )
            }

            Spacer(modifier = Modifier.height(35.dp))
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
                            append("No te preocupes, escribe tu ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorSec,
                                fontWeight = FontWeight.Bold,
                                drawStyle = Stroke(width = 2f)
                            )
                        ){
                            if(selectedOption == "Email")
                                append("correo electrónico ")
                            else
                                append("número telefónico ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("y te enviaré un código para restablecerla.")
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
                            append("No te preocupes, escribe tu ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorSec,
                                fontWeight = FontWeight.Bold,
                            )
                        ){
                            if(selectedOption == "Email")
                                append("correo electrónico ")
                            else
                                append("número telefónico ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("y te enviaré un código para restablecerla.")
                        }
                    },
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.figtree)),
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(55.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(colorDarker),
                verticalAlignment = Alignment.CenterVertically
            ){
                // Botón Email
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topStart = 15.dp, bottomStart = 15.dp))
                        .background( if(selectedOption == "Email") colorWhite else colorDarker)
                        .clickable {
                            selectedOption = "Email"
                            resetFields()
                            clearErrors()
                        },
                    contentAlignment = Alignment.Center
                ){
                    if(selectedOption == "Email") {
                        Box( contentAlignment = Alignment.Center ) {
                            Text(
                                "Email",
                                color = colorPrin,
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.figtree)),
                                fontWeight = FontWeight.Bold,
                                style = TextStyle( drawStyle = Stroke(width = 2.5f) )
                            )

                            Text(
                                "Email",
                                color = colorPrin,
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.figtree)),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Text(
                            "Email",
                            color = colorWhite,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.figtree)),
                            fontWeight = FontWeight.Light
                        )
                    }
                }

                // Botón Teléfono
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topEnd = 15.dp, bottomEnd = 15.dp))
                        .background( if(selectedOption == "Teléfono") colorWhite else colorDarker )
                        .clickable {
                            selectedOption = "Teléfono"
                            resetFields()
                            clearErrors()
                        },
                    contentAlignment = Alignment.Center
                ){
                    if (selectedOption == "Teléfono") {
                        Box( contentAlignment = Alignment.Center ) {
                            Text(
                                "Teléfono",
                                color = colorPrin,
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.figtree)),
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(
                                    drawStyle = Stroke( width = 2.5f )
                                )
                            )

                            Text(
                                "Teléfono",
                                color = colorPrin,
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.figtree)),
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    } else {
                        Text(
                            "Teléfono",
                            color = colorWhite,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.figtree)),
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))
            if(selectedOption == "Email") {
                GenericTextField(
                    value = email,
                    onValueChange = { text ->
                        clearErrors()
                        email = text
                    },
                    placeholder = "Correo Electrónico",
                    isError = emailError,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.mail_wine),
                            contentDescription = "Mail Icon",
                            modifier = Modifier.size(22.dp),
                            tint = if(emailError) colorError else colorDark
                        )
                    }
                )
            } else {
                PhoneField(
                    phone = phone,
                    onPhoneChange = {
                        clearErrors()
                        phone = it
                    },
                    selectedCountry = selectedCountry,
                    onCountryChange = { selectedCountry = it },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    phoneError = phoneError
                )
            }

            Spacer(modifier = Modifier.height(338.dp))
            ContinueButton(
                onClick = {
                    if(Validar()) {
                        if(selectedOption == "Email")
                            forgotPassViewModel.forgotPassByEmail(email)
                        else
                            forgotPassViewModel.forgotPassByPhone(phone)
                    } else
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(0.95f),
                text = "ENVIAR"
            )
        }
    }
}