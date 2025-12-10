package com.example.posecoach.homeScreens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.posecoach.components.Calendar
import com.example.posecoach.components.HomeMenu
import com.example.posecoach.components.ScreenLoader
import com.example.posecoach.data.responses.Dia
import com.example.posecoach.data.viewModel.ProfileViewModel
import com.example.posecoach.data.viewModel.RoutineViewModel
import com.example.posecoach.ui.theme.colorDarker
import com.example.posecoach.ui.theme.colorPrin
import com.example.posecoach.ui.theme.colorSec
import com.example.posecoach.ui.theme.colorWhite
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Locale
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController, profileViewModel: ProfileViewModel, routineViewModel: RoutineViewModel) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val context = LocalContext.current

    // BACKEND
    val loading = routineViewModel.loading.value
    val viewModelError = routineViewModel.error.value
    val routineId = routineViewModel.rutina_id.value
    val rutinaCompleta = routineViewModel.rutinaCompleta.value

    LaunchedEffect(Unit) { profileViewModel.getUserProfile() }

    LaunchedEffect(Unit) { routineViewModel.checkRoutine() }

    LaunchedEffect(routineId) {
        if(routineId.isNotEmpty() && rutinaCompleta == null)
            routineViewModel.getRoutine(routineId)
    }

    LaunchedEffect(viewModelError) {
        if(viewModelError.isNotEmpty()) {
            Toast.makeText(context, viewModelError, Toast.LENGTH_SHORT).show()
            routineViewModel.clearMessages()
        }
    }

    // Determinar clave del día
    fun dayKeyFromDate(date: LocalDate): String {
        return when(date.dayOfWeek) {
            DayOfWeek.MONDAY -> "lunes"
            DayOfWeek.TUESDAY -> "martes"
            DayOfWeek.WEDNESDAY -> "miercoles"
            DayOfWeek.THURSDAY -> "jueves"
            DayOfWeek.FRIDAY -> "viernes"
            DayOfWeek.SATURDAY -> "sabado"
            DayOfWeek.SUNDAY -> "domingo"
        }
    }

    // Obtener datos del día seleccionado
    val selectedDayKey = dayKeyFromDate(selectedDate)
    val diaSeleccionado: Dia? = rutinaCompleta?.dias?.get(selectedDayKey)

    val duration = remember(selectedDate) { durationForDate(selectedDate) }
    val muscle = diaSeleccionado?.musculo?.replace("_", " ")?.replaceFirstChar { it.uppercase() } ?: ""
    val weekDayLabel = selectedDate.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL,
        Locale("es", "ES")).replaceFirstChar { it.uppercase() }
    val trainName = diaSeleccionado?.nombre ?: "Día de Descanso"

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
                    .padding(top = 97.dp),
                contentAlignment = Alignment.Center
            ){
                Text(
                    if(profileViewModel.selectedSex.value == "F") "BIENVENIDA"
                            else "BIENVENIDO",
                    color = colorWhite,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        drawStyle = Stroke(
                            4f,
                            join = StrokeJoin.Round
                        ),
                        letterSpacing = 0.05.em
                    )
                )

                Text(
                    if(profileViewModel.selectedSex.value == "F") "BIENVENIDA"
                            else "BIENVENIDO",
                    color = colorWhite,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.05.em
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 7.dp),
                contentAlignment = Alignment.Center
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Box{
                        Text(
                            profileViewModel.selectedUsername.value,
                            color = colorSec,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.comfortaa)),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                drawStyle = Stroke(
                                    3f,
                                    join = StrokeJoin.Round
                                ),
                                letterSpacing = 0.05.em
                            )
                        )

                        Text(
                            profileViewModel.selectedUsername.value,
                            color = colorSec,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.comfortaa)),
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.05.em
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.heart),
                        contentDescription = "Heart",
                        modifier = Modifier
                            .size(23.dp)
                            .padding(start = 10.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 55.dp)
            ){
                Text(
                    "TU SESIÓN DE HOY",
                    color = colorWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        drawStyle = Stroke(
                            2f,
                            join = StrokeJoin.Round
                        ),
                        letterSpacing = 0.05.em
                    )
                )

                Text(
                    "TU SESIÓN DE HOY",
                    color = colorWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    letterSpacing = 0.05.em,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Calendar(
                selectedDate = selectedDate,
                onDateSelected = { newDate -> selectedDate = newDate },
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 20.dp)
            ){
                Text(
                    "PROGRESO",
                    color = colorWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        drawStyle = Stroke(
                            2f,
                            join = StrokeJoin.Round
                        ),
                        letterSpacing = 0.05.em
                    )
                )

                Text(
                    "PROGRESO",
                    color = colorWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    letterSpacing = 0.05.em,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            ProgressBar(0.7f)
            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ){
                Text(
                    "RUTINA",
                    color = colorWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        drawStyle = Stroke(
                            2f,
                            join = StrokeJoin.Round
                        ),
                        letterSpacing = 0.05.em
                    )
                )

                Text(
                    "RUTINA",
                    color = colorWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    letterSpacing = 0.05.em,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
                    .background(colorPrin, RoundedCornerShape(10.dp))
                    .clickable {
                        if(trainName != "Día de Descanso")
                            navController.navigate("routine/$selectedDayKey")
                        else
                            Toast.makeText(context, "Hoy no hay ejercicios \uD83E\uDE77", Toast.LENGTH_SHORT).show()
                    }
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(247.dp)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ){
                    Column {
                        Box(
                            modifier = Modifier.background(Color.Black, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ){
                            Row(
                                modifier = Modifier.padding(vertical = 6.dp, horizontal = 15.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Image(
                                    painter = painterResource(id = R.drawable.timer),
                                    contentDescription = "Timer",
                                    modifier = Modifier.size(22.dp),
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    duration,
                                    color = colorWhite,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.figtree))
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier.background(Color.Black, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ){
                            Row(
                                modifier = Modifier.padding(vertical = 6.dp, horizontal = 15.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Image(
                                    painter = painterResource(id = if(muscle.isNotEmpty()) R.drawable.muscle else R.drawable.rest),
                                    contentDescription = "Muscle",
                                    modifier = Modifier.size(20.dp),
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    muscle.ifEmpty { "Descanso" },
                                    color = colorWhite,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.figtree))
                                )
                            }
                        }
                    }

                    Column {
                        Box{
                            Text(
                                weekDayLabel.uppercase(),
                                color = Color.Black,
                                fontSize = 27.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.comfortaa)),
                                textAlign = TextAlign.Start,
                                style = TextStyle(
                                    drawStyle = Stroke(
                                        2f,
                                        join = StrokeJoin.Round
                                    ),
                                    letterSpacing = 0.05.em
                                )
                            )

                            Text(
                                weekDayLabel.uppercase(),
                                color = Color.Black,
                                fontSize = 27.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.comfortaa)),
                                letterSpacing = 0.05.em,
                                textAlign = TextAlign.Start,
                            )
                        }

                        Text(
                            trainName,
                            color = Color.Black,
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.figtree)),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }

                }

                Image(
                    painter = painterResource(id = R.drawable.model),
                    contentDescription = "Model",
                    modifier = Modifier
                        .size(250.dp)
                        .offset(x = (160).dp),
                    contentScale = ContentScale.Fit
                )
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

@Composable
fun ProgressBar(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(colorDarker)
    ){
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .clip(RoundedCornerShape(20.dp))
                .background(colorSec)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun durationForDate(date: LocalDate): String {
    return if(date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY)
        "Todo el día"
    else {
        val seed = date.toEpochDay().toInt()
        val random = Random(seed)
        "${random.nextInt(45, 96)} min"
    }
}