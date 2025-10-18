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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.posecoach.R
import com.example.posecoach.components.ContinueButton
import com.example.posecoach.components.OptionSelectorDer
import com.example.posecoach.data.viewModel.RegistroViewModel
import com.example.posecoach.ui.theme.colorSec
import com.example.posecoach.ui.theme.colorWhite

@Composable
fun GoalScreen(navController: NavController, registroViewModel: RegistroViewModel) {
    val context = LocalContext.current
    var tonificar by remember { mutableStateOf(false) }
    var peso by remember { mutableStateOf(false) }
    var musculo by remember { mutableStateOf(false) }
    var forma by remember { mutableStateOf(false) }

    fun selcOption(option: String) {
        tonificar = option == "tonificar"
        peso = option == "perder_peso"
        musculo = option == "ganar_musculo"
        forma = option == "mantener_forma"

        registroViewModel.usuario.value = registroViewModel.usuario.value.copy(
            goal = option
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 160.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.onb_m),
                contentDescription = "Goal",
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = (-90).dp),
                contentScale = ContentScale.Fit
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
                    .fillMaxWidth()
                    .padding(top = 117.dp, end = 10.dp)
            ){
                Text(
                    "¿CUÁL ES TU OBJETIVO?",
                    color = colorWhite,
                    fontSize = 47.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    lineHeight = 64.sp,
                    textAlign = TextAlign.End,
                    style = TextStyle(
                        drawStyle = Stroke(
                            4f,
                            join = StrokeJoin.Round
                        )
                    )
                )

                Text(
                    "¿CUÁL ES TU OBJETIVO?",
                    color = colorWhite,
                    fontSize = 47.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    lineHeight = 64.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.offset(y = (-5).dp)
                )
            }

            Spacer( modifier = Modifier.height(3.dp) )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 9.dp),
                contentAlignment = Alignment.TopEnd
            ){
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("Elige el que más se ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorSec,
                                fontWeight = FontWeight.Bold,
                                drawStyle = Stroke(width = 2f)
                            )
                        ){
                            append("parezca ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("a tu meta.")
                        }
                    },
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.figtree)),
                    lineHeight = 22.sp,
                    textAlign = TextAlign.End
                )

                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("Elige el que más se ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorSec,
                                fontWeight = FontWeight.Bold
                            )
                        ){
                            append("parezca ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("a tu meta.")
                        }
                    },
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.figtree)),
                    lineHeight = 22.sp,
                    textAlign = TextAlign.End
                )
            }

            Spacer( modifier = Modifier.height(235.dp) )
            OptionSelectorDer(
                text = "Tonificar",
                isSelected = tonificar,
                onClick = { selcOption("tonificar") },
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
                text = "Ganar músculo",
                isSelected = musculo,
                onClick = { selcOption("ganar_musculo") },
                modifier = Modifier.padding(end = 15.dp)
            )

            Spacer( modifier = Modifier.height(12.dp) )
            OptionSelectorDer(
                text = "Mantenerme en forma",
                isSelected = forma,
                onClick = { selcOption("mantener_forma") },
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
                onClick = {
                    if(!tonificar && !peso && !musculo && !forma)
                        Toast.makeText(context, "Por favor, selecciona tu objetivo antes de continuar.", Toast.LENGTH_SHORT).show()
                    else
                        navController.navigate("experience") },
                modifier = Modifier
                    .fillMaxWidth(0.885f)
                    .padding(end = 10.dp)
            )
        }
    }
}