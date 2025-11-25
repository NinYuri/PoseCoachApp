package com.example.posecoach.profileScreens

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
import com.example.posecoach.components.GlowItem
import com.example.posecoach.components.HomeMenu
import com.example.posecoach.data.viewModel.ProfileViewModel
import com.example.posecoach.ui.theme.colorWhite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(navController: NavController, profileViewModel: ProfileViewModel) {
    val selEmail = profileViewModel.selectedEmail.value
    val selPhone = profileViewModel.selectedPhone.value

    LaunchedEffect(Unit) { profileViewModel.getUserProfile() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp, 0.dp),
            verticalArrangement = Arrangement.Top
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
                        .clickable { navController.navigate("profile") },
                    contentScale = ContentScale.Fit
                )
            }

            Box( modifier = Modifier.padding(top = 17.dp, end = 13.dp) ){
                Text(
                    "CUENTA",
                    color = colorWhite,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        drawStyle = Stroke(
                            4f,
                            join = StrokeJoin.Round
                        )
                    )
                )

                Text(
                    "CUENTA",
                    color = colorWhite,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
            GlowItem("Nombre de Usuario", true) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(100L)
                    navController.navigate("changeUser")
                }
            }

            GlowItem(
                text = if(selEmail.isEmpty()) "Añadir Correo Electrónico"
                       else "Correo Electrónico", true
            ){
                CoroutineScope(Dispatchers.Main).launch {
                    delay(100L)
                    val hasEmail = selEmail.isNotEmpty()
                    navController.navigate("changeEmail/$hasEmail")
                }
            }

            GlowItem(
                text = if(selPhone.isEmpty()) "Añadir Número Telefónico"
                       else "Número Telefónico", true
            ){
                CoroutineScope(Dispatchers.Main).launch {
                    delay(100L)
                    val hasPhone = selPhone.isNotEmpty()
                    navController.navigate("changePhone/$hasPhone")
                }
            }
        }

        HomeMenu(
            navController = navController,
            selectedOpt = "profile",
            isRecording = false,
            onRecordClick = { }
        )
    }
}