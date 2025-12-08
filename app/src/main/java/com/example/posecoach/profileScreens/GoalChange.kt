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
fun GoalChange(navController: NavController, profileViewModel: ProfileViewModel) {
    val selGoal = profileViewModel.selectedGoal.value
    val context = LocalContext.current
    val loading = profileViewModel.loading.value

    var tonificar by remember { mutableStateOf(Ton(selGoal)) }
    var peso by remember { mutableStateOf(Peso(selGoal)) }
    var musculo by remember { mutableStateOf(Muscle(selGoal)) }
    var forma by remember { mutableStateOf(Forma(selGoal)) }

    LaunchedEffect(selGoal) {
        tonificar = Ton(selGoal)
        peso = Peso(selGoal)
        musculo = Muscle(selGoal)
        forma = Forma(selGoal)
    }

    fun selcOption(option: String) {
        tonificar = option == "tonificar"
        peso = option == "perder_peso"
        musculo = option == "ganar_musculo"
        forma = option == "mantener_forma"
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
                contentDescription = "Goal Change",
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
                    "OBJETIVO",
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
                    "OBJETIVO",
                    color = colorPrin,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer( modifier = Modifier.height(30.dp) )
            OptionSelectorDer(
                text = "Mantenerme en forma",
                isSelected = forma,
                onClick = { selcOption("mantener_forma") },
                modifier = Modifier.padding(end = 15.dp)
            )

            Spacer( modifier = Modifier.height(12.dp) )
            OptionSelectorDer(
                text = "Ganar músculo",
                isSelected = musculo,
                onClick = { selcOption("ganar_musculo") },
                modifier = Modifier.padding(end = 15.dp)
            )

            Spacer( modifier = Modifier.height(12.dp) )
            OptionSelectorDer(
                text = "Perder peso",
                isSelected = peso,
                onClick = { selcOption("perder_peso") },
                modifier = Modifier.padding(end = 15.dp)
            )

            Spacer( modifier = Modifier.height(12.dp) )
            OptionSelectorDer(
                text = "Tonificar",
                isSelected = tonificar,
                onClick = { selcOption("tonificar") },
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
                    val selGoal = when {
                        tonificar -> "tonificar"
                        peso -> "perder_peso"
                        musculo -> "ganar_musculo"
                        forma -> "mantener_forma"
                        else -> selGoal
                    }

                    profileViewModel.updateProfile(
                        UpdateRequest(
                            goal = selGoal
                        )
                    )
                    Toast.makeText(context, "Objetivo actualizado exitosamente", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth(0.885f)
                    .padding(end = 10.dp)
            )
        }
    }
}

private fun Ton(option: String): Boolean { return option == "tonificar" }

private fun Peso(option: String): Boolean { return option == "perder_peso" }

private fun Muscle(option: String): Boolean { return option == "ganar_musculo" }

private fun Forma(option: String): Boolean { return option == "mantener_forma" }