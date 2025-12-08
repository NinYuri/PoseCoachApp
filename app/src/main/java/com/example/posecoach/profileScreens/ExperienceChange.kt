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
import com.example.posecoach.components.OptionSelectorDer
import com.example.posecoach.components.ScreenLoader
import com.example.posecoach.data.model.UpdateRequest
import com.example.posecoach.data.viewModel.ProfileViewModel
import com.example.posecoach.ui.theme.colorPrin
import com.example.posecoach.ui.theme.colorWhite

@Composable
fun ExperienceChange(navController: NavController, profileViewModel: ProfileViewModel) {
    val selExp = profileViewModel.selectedExp.value
    val context = LocalContext.current
    val loading = profileViewModel.loading.value

    var avanzado by remember { mutableStateOf(Adv(selExp)) }
    var intermedio by remember { mutableStateOf(Int(selExp)) }
    var principiante by remember { mutableStateOf(Prin(selExp)) }

    LaunchedEffect(selExp) {
        avanzado = Adv(selExp)
        intermedio = Int(selExp)
        principiante = Prin(selExp)
    }

    fun selcOption(option: String) {
        avanzado = option == "avanzado"
        intermedio = option == "intermedio"
        principiante = option == "principiante"
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
                .padding(top = 190.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Experience Change",
                modifier = Modifier
                    .size(600.dp)
                    .offset(x = (-17).dp, y = (80).dp),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp, 0.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End
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

            Box(modifier = Modifier.padding(top = 17.dp, end = 13.dp) ) {
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

            Box( modifier = Modifier.padding(top = 10.dp, end = 10.dp) ) {
                Text(
                    "EXPERIENCIA",
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
                    "EXPERIENCIA",
                    color = colorPrin,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer( modifier = Modifier.height(50.dp) )
            OptionSelectorDer(
                text = "Avanzado",
                isSelected = avanzado,
                onClick = { selcOption("avanzado") },
                modifier = Modifier.padding(end = 15.dp)
            )

            Spacer(modifier = Modifier.height(15.dp))
            OptionSelectorDer(
                text = "Intermedio",
                isSelected = intermedio,
                onClick = { selcOption("intermedio") },
                modifier = Modifier.padding(end = 15.dp)
            )

            Spacer(modifier = Modifier.height(15.dp))
            OptionSelectorDer(
                text = "Principiante",
                isSelected = principiante,
                onClick = { selcOption("principiante") },
                modifier = Modifier.padding(end = 15.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 62.dp),
            contentAlignment = Alignment.BottomCenter
        ){
            ContinueButton(
                text = "ACTUALIZAR",
                onClick = {
                    val selectedExp = when {
                        avanzado -> "avanzado"
                        intermedio -> "intermedio"
                        principiante -> "principiante"
                        else -> selExp
                    }

                    profileViewModel.updateProfile(
                        UpdateRequest(
                            experience = selectedExp
                    ))
                    Toast.makeText(context, "Experiencia actualizada exitosamente", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth(0.885f)
                    .padding(end = 10.dp)
            )
        }
    }
}

private fun Adv(option: String): Boolean { return option == "avanzado" }

private fun Int(option: String): Boolean { return option == "intermedio" }

private fun Prin(option: String): Boolean { return option == "principiante" }