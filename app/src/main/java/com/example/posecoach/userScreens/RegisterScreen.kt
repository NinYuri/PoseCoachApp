package com.example.posecoach.userScreens

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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.posecoach.R
import com.example.posecoach.components.ContinueButton
import com.example.posecoach.components.Country
import com.example.posecoach.components.GenericTextField
import com.example.posecoach.components.PhoneField
import com.example.posecoach.data.model.RegistroRequest
import com.example.posecoach.data.viewModel.UserViewModel
import com.example.posecoach.ui.theme.colorDark
import com.example.posecoach.ui.theme.colorDarker
import com.example.posecoach.ui.theme.colorError
import com.example.posecoach.ui.theme.colorPrin
import com.example.posecoach.ui.theme.colorSec
import com.example.posecoach.ui.theme.colorWhite

@Composable
fun RegisterScreen(navController: NavController, userViewModel: UserViewModel) {
    // BACKEND
    val loading = userViewModel.loading.value
    val viewModelMensaje = userViewModel.mensaje.value
    val viewModelError = userViewModel.error.value
    val temporalId = userViewModel.temporalId.value

    // REGISTRO - Opciones
    var selectedOption by remember { mutableStateOf("Email") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf<Country>(Country("+52", "MX", "México")) }

    // REGISTRO - Campos
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var passVisible by remember { mutableStateOf(false) }
    var passConfVisible by remember { mutableStateOf(false) }

    // ERROR - Estados
    var emailError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var passwordConfirmError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Cambios en los estados del ViewModel
    LaunchedEffect(viewModelMensaje, viewModelError, temporalId) {
        if(viewModelMensaje.isNotEmpty() && viewModelError.isEmpty() && temporalId > 0)
            navController.navigate("otpcode?temporalId=${temporalId}")
    }

    LaunchedEffect(viewModelError) {
        if(viewModelError.isNotEmpty()) {
            if(selectedOption == "Email")
                emailError = true
            else
                phoneError = true
            Toast.makeText(context, viewModelError, Toast.LENGTH_SHORT).show()
            userViewModel.clearMessages()
        }
    }

    LaunchedEffect(viewModelMensaje) {
        if(viewModelMensaje.isNotEmpty() && viewModelError.isEmpty()) {
            Toast.makeText(context, viewModelMensaje, Toast.LENGTH_SHORT).show()
            userViewModel.clearMessages()
        }
    }

    // Validación
    fun validEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return email.matches(emailRegex)
    }

    fun validPhone(phone: String): Boolean {
        // Permite + opcional, espacios o guiones
        val regex = Regex("^\\+?[0-9\\s-]{10,20}$")

        // Eliminar espacios y guiones
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

            password.isBlank() -> {
                passwordError = true
                errorMessage = "Por favor, escribe tu contraseña."
                false
            }
            password.length < 8 -> {
                passwordError = true
                errorMessage = "La contraseña debe tener al menos 8 caracteres."
                false
            }

            passwordConfirm.isBlank() -> {
                passwordConfirmError = true
                errorMessage = "Por favor, confirma tu contraseña."
                false
            }
            password != passwordConfirm -> {
                passwordConfirmError = true
                errorMessage = "Las contraseñas no coinciden."
                false
            }

            else -> {
                emailError = false
                phoneError = false
                passwordError = false
                passwordConfirmError = false
                errorMessage = ""
                true
            }
        }
    }

    // Limpiar errores cuando el usuario empiece a escribir
    fun clearErrors() {
        emailError = false
        phoneError = false
        passwordError = false
        passwordConfirmError = false
        errorMessage = ""
        userViewModel.clearMessages()
    }

    fun resetFields() {
        email = ""
        phone = ""
        password = ""
        passwordConfirm = ""
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.welcome),
                contentDescription = "Register",
                modifier = Modifier
                    .fillMaxSize()
                    .blur(4.dp),
                contentScale = ContentScale.Fit
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(700.dp)
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
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorSec,
                                colorSec.copy(alpha = 0f)
                            )
                        ),
                        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                /* ViewModel loading
                if(loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = colorWhite
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }*/

                Box(
                    modifier = Modifier.padding(top = 50.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        "REGISTRO",
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

                    Text(
                        "REGISTRO",
                        color = colorWhite,
                        fontSize = 50.sp,
                        fontFamily = FontFamily(Font(R.font.comfortaa)),
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    "Comienza a transformar tu entrenamiento",
                    color = colorWhite,
                    fontSize = 17.sp,
                    fontFamily = FontFamily(Font(R.font.figtree)),
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(35.dp))
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
                        if (selectedOption == "Email") {
                            Box( contentAlignment = Alignment.Center ) {
                                Text(
                                    "Email",
                                    color = colorPrin,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.figtree)),
                                    fontWeight = FontWeight.Bold,
                                    style = TextStyle(
                                        drawStyle = Stroke( width = 2.5f )
                                    )
                                )

                                Text(
                                    "Email",
                                    color = colorPrin,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.figtree)),
                                    fontWeight = FontWeight.Bold,
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

                Spacer(modifier = Modifier.height(40.dp))
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

                Spacer(modifier = Modifier.height(15.dp))
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
                        if(passVisible)
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
                                tint = if(passwordError) colorError else colorDark
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(15.dp))
                GenericTextField(
                    value = passwordConfirm,
                    onValueChange = {
                        clearErrors()
                        passwordConfirm = it
                    },
                    placeholder = "Confirmar Contraseña",
                    isError = passwordConfirmError,
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
                            tint = if(passwordConfirmError) colorError else colorDark
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
                                tint = if(passwordConfirmError) colorError else colorDark
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(30.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                ){
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.reg_shadow),
                            contentDescription = "Shadow",
                            modifier = Modifier
                                .size(260.dp)
                                .offset(x = (65).dp)
                        )
                    }

                    ContinueButton(
                        onClick = {
                            if(Validar()) {
                                val datos = RegistroRequest(
                                    email = if(selectedOption == "Email") email else "",
                                    phone = if(selectedOption == "Teléfono") "${selectedCountry.code}$phone" else "",
                                    password = password,
                                    confirm_password = passwordConfirm
                                )

                                userViewModel.registerInitial(datos)
                            } else
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.offset(y = (110).dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(25.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorSec.copy(alpha = 0f),
                                colorSec
                            )
                        ),
                        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
            )
        }
    }
}