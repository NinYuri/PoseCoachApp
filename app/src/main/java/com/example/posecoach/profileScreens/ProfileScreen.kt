package com.example.posecoach.profileScreens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.posecoach.R
import com.example.posecoach.components.Dialog
import com.example.posecoach.components.GlowItem
import com.example.posecoach.components.HomeMenu
import com.example.posecoach.components.ScreenLoader
import com.example.posecoach.data.viewModel.LoginViewModel
import com.example.posecoach.data.viewModel.ProfileViewModel
import com.example.posecoach.ui.theme.colorDark
import com.example.posecoach.ui.theme.colorPrin
import com.example.posecoach.ui.theme.colorSec
import com.example.posecoach.ui.theme.colorWhite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController, profileViewModel: ProfileViewModel, loginViewModel: LoginViewModel) {
    // BACKEND LOGOUT
    val mensaje = loginViewModel.mensaje.value
    val error = loginViewModel.error.value
    val isLoggedIn = loginViewModel.isLoggedIn.value

    // BACKEND DELETE
    val loading = profileViewModel.loading.value
    val mesDelete = profileViewModel.mensaje.value
    val errDelete = profileViewModel.error.value
    val isDelete by profileViewModel.isDelete

    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { profileViewModel.getUserProfile() }

    LaunchedEffect(isDelete) {
        if(isDelete) {
            delay(500L)
            navController.navigate("welcome") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    LaunchedEffect(error) {
        if(error.isNotEmpty()) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            loginViewModel.clearMessages()
        }
    }

    LaunchedEffect(errDelete) {
        if(errDelete.isNotEmpty()) {
            Toast.makeText(context, errDelete, Toast.LENGTH_SHORT).show()
            profileViewModel.clearMessages()
        }
    }

    LaunchedEffect(mensaje) {
        if(mensaje.isNotEmpty() && error.isEmpty()) {
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
            loginViewModel.clearMessages()
        }
    }

    LaunchedEffect(mesDelete) {
        if(mesDelete.isNotEmpty() && errDelete.isEmpty()) {
            Toast.makeText(context, mesDelete, Toast.LENGTH_SHORT).show()
            profileViewModel.clearMessages()
        }
    }

    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            delay(300L)
            navController.navigate("welcome") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        ScreenLoader( isLoading = loading )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp)
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 97.dp)
            ){
                Text(
                    "PERFIL",
                    color = colorWhite,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        drawStyle = Stroke(
                            4f,
                            join = StrokeJoin.Round
                        ),
                        letterSpacing = 0.05.em
                    )
                )

                Text(
                    "PERFIL",
                    color = colorWhite,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.Start,
                    letterSpacing = 0.05.em,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer( modifier = Modifier.height(5.dp) )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ){
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("Cada ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorSec,
                                fontWeight = FontWeight.Bold,
                                drawStyle = Stroke(width = 2f)
                            )
                        ){
                            append("cambio ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("comienza con una decisión.")
                        }
                    },
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.figtree)),
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Start
                )

                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("Cada ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorSec,
                                fontWeight = FontWeight.Bold
                            )
                        ){
                            append("cambio ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("comienza con una decisión.")
                        }
                    },
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.figtree)),
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Start
                )
            }

            Spacer( Modifier.height(40.dp) )
            TextTitle("Información Personal")
            Spacer(Modifier.height(5.dp))

            GlowItem("Cuenta", true) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(100L)
                    navController.navigate("account")
                }
            }
            GlowItem("Fecha de Nacimiento", true) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(100L)
                    navController.navigate("changeDate")
                }}
            GlowItem("Género", true) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(100L)
                    navController.navigate("changeGender")
                }}
            GlowItem("Altura", true) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(100L)
                    navController.navigate("changeHeight")
                }}
            Spacer(Modifier.height(20.dp))

            TextTitle("Preferencias de Entrenamiento")
            Spacer(Modifier.height(5.dp))

            GlowItem("Experiencia", true) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(100L)
                    navController.navigate("changeExp")
                }}
            GlowItem("Objetivo", true) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(100L)
                    navController.navigate("changeGoal")
                }}
            GlowItem("Equipo Disponible", true) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(100L)
                    navController.navigate("changeEquip")
                }}
            Spacer(Modifier.height(20.dp))

            TextTitle("Configuración")
            Spacer(Modifier.height(5.dp))

            GlowItem("Cerrar sesión", false) {
                showLogoutDialog = true
            }
            GlowItem("Eliminar cuenta", false) {
                showDeleteDialog = true
            }
        }

        HomeMenu(
            navController = navController,
            selectedOpt = "profile",
            isRecording = false,
            onRecordClick = { }
        )
    }

    if(showLogoutDialog) {
        Dialog(
            { showLogoutDialog = false },
            "Cerrar sesión",
            "Podrás volver cuando lo desees iniciando sesión nuevamente.",
            { showLogoutDialog = false
                        loginViewModel.Logout() },
            "Salir",
            { showLogoutDialog = false }
        )
    }

    if(showDeleteDialog) {
        Dialog(
            { showDeleteDialog = false },
            "Eliminar cuenta",
            "¿De verdad deseas eliminar tu cuenta?\n" +
                    "Este proceso es permanente y perderás tu progreso, rutinas y ajustes personalizados.\n" +
                    "Puedes volver cuando quieras, pero tu información ya no estará disponible.",
            { showDeleteDialog = false
                        profileViewModel.deleteUser(loginViewModel) },
            "Eliminar",
            { showDeleteDialog = false }
        )
    }
}

@Composable
fun TextTitle(title: String) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ){
        Text(
            title,
            color = colorWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.figtree)),
            lineHeight = 22.sp,
            style = TextStyle( drawStyle = Stroke(2f) ),
            letterSpacing = 0.05.em,
            modifier = Modifier.offset(y = (1.6).dp)
        )

        Text(
            title,
            color = colorWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.figtree)),
            lineHeight = 22.sp,
            letterSpacing = 0.05.em
        )
    }
}