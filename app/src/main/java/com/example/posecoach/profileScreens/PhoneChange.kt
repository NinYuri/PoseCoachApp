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
import com.example.posecoach.components.Country
import com.example.posecoach.components.PhoneField
import com.example.posecoach.components.ScreenLoader
import com.example.posecoach.data.viewModel.EmPhViewModel
import com.example.posecoach.data.viewModel.ProfileViewModel
import com.example.posecoach.ui.theme.colorPrin
import com.example.posecoach.ui.theme.colorWhite
import kotlinx.coroutines.delay

@Composable
fun PhoneChange(navController: NavController, hasPhone: Boolean, profileViewModel: ProfileViewModel, emPhViewModel: EmPhViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf<Country>(Country("+52", "MX", "México")) }

    var error by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    var selectedPhone by remember {
        mutableStateOf(
            profileViewModel.selectedPhone.value
                ?.removePrefix("+${selectedCountry.code.removePrefix("+")}")
                ?.removePrefix("+52")
                ?.removePrefix("+")
                ?: ""
        )
    }

    // BACKEND
    val loading = emPhViewModel.loading.value
    val viewModelMensaje = emPhViewModel.mensaje.value
    val viewModelError = emPhViewModel.error.value

    LaunchedEffect(viewModelError) {
        if(viewModelError.isNotEmpty()) {
            error = true
            Toast.makeText(context,viewModelError, Toast.LENGTH_SHORT).show()
            emPhViewModel.clearMessages()
        }
    }

    LaunchedEffect(viewModelMensaje, viewModelError) {
        if(viewModelMensaje.isNotEmpty() && viewModelError.isEmpty()) {
            emPhViewModel.userPhone.value = selectedPhone

            Toast.makeText(context, viewModelMensaje, Toast.LENGTH_SHORT).show()
            delay(500)

            navController.navigate("profileOTP/PHONE")
            emPhViewModel.clearMessages()
        }
    }

    fun validPhone(phone: String): Boolean {
        val regex = Regex("^\\+?[0-9\\s-]{10,20}$")
        val cleanPhone = phone.replace(Regex("[^0-9]"), "")
        return regex.matches(phone) && cleanPhone.length in 10..15
    }

    fun Validate(): Boolean {
        return when {
            selectedPhone.isBlank() -> {
                error = true
                errorMessage = "Por favor, escribe tu nuevo número telefónico."
                false
            }
            !validPhone(selectedPhone) -> {
                error = true
                errorMessage = "Por favor, ingresa un nuevo número telefónico válido."
                false
            }
            selectedPhone.length > 10 -> {
                error = true
                errorMessage = "Por favor, ingresa un nuevo número telefónico válido."
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
                contentDescription = "Phone Change",
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
                    if(hasPhone) "CAMBIAR" else "AÑADIR",
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
                    if(hasPhone) "CAMBIAR" else "AÑADIR",
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
                    "TELÉFONO",
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
                    "TELÉFONO",
                    color = colorPrin,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer( modifier = Modifier.height(200.dp) )
            PhoneField(
                phone = selectedPhone,
                onPhoneChange = {
                    if(error) {
                        error = false
                        errorMessage = ""
                    }

                    selectedPhone = it
                },
                selectedCountry = selectedCountry,
                onCountryChange = { selectedCountry = it },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                phoneError = error
            )

            Spacer(modifier = Modifier.height(332.dp))
            ContinueButton(
                text = "VERIFICAR",
                onClick = {
                    if(Validate()){
                        if(hasPhone)
                            emPhViewModel.changePhone("${selectedCountry.code}$selectedPhone")
                        else
                            emPhViewModel.addPhone("${selectedCountry.code}$selectedPhone")
                    } else
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(0.965f)
            )
        }
    }
}