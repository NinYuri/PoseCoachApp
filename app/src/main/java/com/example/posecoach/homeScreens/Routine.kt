package com.example.posecoach.homeScreens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.posecoach.R
import com.example.posecoach.components.ExerciseCard
import com.example.posecoach.components.HomeMenu
import com.example.posecoach.data.viewModel.RoutineViewModel
import com.example.posecoach.ui.theme.colorSec
import com.example.posecoach.ui.theme.colorWhite

@Composable
fun RoutineScreen(navController: NavController, routineViewModel: RoutineViewModel, selectedDayKey: String) {
    val rutinaCompleta = routineViewModel.rutinaCompleta.value
    val dia = rutinaCompleta?.dias?.get(selectedDayKey)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp),
            verticalArrangement = Arrangement.Top
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp)
            ){
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .offset(x = (-10).dp),
                    contentAlignment = Alignment.CenterStart
                ){
                    Image(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { navController.navigate("home") },
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Box( contentAlignment = Alignment.Center ){
                    Text(
                        "RUTINA DE HOY",
                        color = colorWhite,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.comfortaa)),
                        letterSpacing = 0.05.em,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            drawStyle = Stroke(
                                4f,
                                join = StrokeJoin.Round
                            )
                        )
                    )

                    Text(
                        "RUTINA DE HOY",
                        color = colorWhite,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.comfortaa)),
                        letterSpacing = 0.05.em,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 13.dp),
                contentAlignment = Alignment.Center
            ){
                Text(
                    dia?.nombre ?: "",
                    color = colorSec,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    letterSpacing = 0.05.em,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        drawStyle = Stroke(
                            2f,
                            join = StrokeJoin.Round
                        )
                    )
                )

                Text(
                    dia?.nombre ?: "",
                    color = colorSec,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    letterSpacing = 0.05.em,
                    textAlign = TextAlign.Center,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, bottom = 100.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ){
                if(!dia?.detalles.isNullOrEmpty()) {
                    dia!!.detalles.forEach { ejercicio ->
                        ExerciseCard(
                            ejercicio.imageUrl,
                            ejercicio.name,
                            "${ejercicio.series} x ${ejercicio.reps}",
                            formatRestTime(ejercicio.restSeconds),
                            {}
                        )
                    }
                }
            }
        }

        HomeMenu(
            navController = navController,
            selectedOpt = "home",
            isRecording = false,
            onRecordClick = { }
        )
    }
}

fun formatRestTime(seconds: Int): String {
    return if (seconds < 60)
        "${seconds} s"
    else {
        val min = seconds / 60
        val sec = seconds % 60

        if(sec == 0) "${min} min"
        else "%d:%02d min".format(min, sec)
    }
}