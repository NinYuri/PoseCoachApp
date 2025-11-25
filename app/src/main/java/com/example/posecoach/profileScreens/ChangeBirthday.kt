package com.example.posecoach.profileScreens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import com.example.posecoach.data.model.UpdateRequest
import com.example.posecoach.data.viewModel.ProfileViewModel
import com.example.posecoach.data.viewModel.RegistroViewModel
import com.example.posecoach.ui.theme.colorDarker
import com.example.posecoach.ui.theme.colorPrin
import com.example.posecoach.ui.theme.colorWhite
import com.example.posecoach.ui.theme.colorSec
import java.util.Calendar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DatePicker(iniYear: Int, iniMonth: Int, iniDay: Int,  onDateSelected: (Int, Int, Int) -> Unit ) {
    var selectedYear by remember { mutableStateOf(iniYear) }
    var selectedMonth by remember { mutableStateOf(iniMonth) }
    var selectedDay by remember { mutableStateOf(iniDay) }

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = ((currentYear - 85)..currentYear).toList()
    val months = listOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
        "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )
    val days = (1..31).toList()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .align(Alignment.Center)
                .offset(y = (-6).dp)
                .background(colorDarker.copy(alpha = 0.8f), RoundedCornerShape(10.dp))
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Year Selector
            DatePickerColumn(
                items = years.map { it.toString() },
                selectedItem = selectedYear.toString(),
                onItemSelected = {
                    selectedYear = it.toInt()
                    onDateSelected(selectedYear, selectedMonth, selectedDay)
                }
            )

            // Month Selector
            DatePickerColumn(
                items = months,
                selectedItem = months[selectedMonth],
                onItemSelected = {
                    selectedMonth = months.indexOf(it)
                    onDateSelected(selectedYear, selectedMonth, selectedDay)
                }
            )

            // Day Selector
            DatePickerColumn(
                items = days.map { it.toString() },
                selectedItem = selectedDay.toString(),
                onItemSelected = {
                    selectedDay = it.toInt()
                    onDateSelected(selectedYear, selectedMonth, selectedDay)
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DatePickerColumn(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
){
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = items.indexOf(selectedItem).coerceAtLeast(0)
    )

    // Detectar elemento centrado automáticamente
    val coroutineScope = rememberCoroutineScope()
    val itemHeightPx = with(LocalDensity.current) { 48.dp.toPx() }

    val visibleIndex = remember {
        derivedStateOf {
            val scrollOffset = listState.firstVisibleItemScrollOffset
            val centerOffset = itemHeightPx / 2

            val middleIndex = listState.firstVisibleItemIndex + if(scrollOffset > centerOffset) 1 else 0
            middleIndex.coerceIn(0, items.lastIndex)
        }
    }

    // Notifica cambio automático
    LaunchedEffect(visibleIndex.value) {
        onItemSelected(items[visibleIndex.value])
    }

    LazyColumn(
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.height(200.dp)
    ){
        item { Spacer(modifier = Modifier.height(52.dp)) }

        itemsIndexed(items) { _, item ->
            Text(
                text = item,
                color = if (item == items[visibleIndex.value]) colorWhite else colorWhite.copy(alpha = 0.5f),
                fontSize = 18.sp,
                fontWeight = if (item == items[visibleIndex.value]) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .height(32.dp),
                textAlign = TextAlign.Center
            )
        }

        item { Spacer(modifier = Modifier.height(52.dp)) }
    }
}


@Composable
fun ChangeBirthday(navController: NavController, profileViewModel: ProfileViewModel) {
    val selDate = profileViewModel.selectedDate.value
    val context = LocalContext.current

    val (savedYear, savedMonth, savedDay) = remember(selDate) {
        val parts = selDate.split("-")
        Triple(
            parts[0].toInt(),
            parts[1].toInt() - 1,
            parts[2].toInt()
        )
    }

    var selectedDate by remember { mutableStateOf(Triple(1998,4,4)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 160.dp)
        ){
            Image(
                painter = painterResource(R.drawable.otp_pass),
                contentDescription = "Birthday Change",
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
                        .clickable { navController.navigate("profile") },
                    contentScale = ContentScale.Fit
                )
            }

            Box(modifier = Modifier.padding(top = 17.dp, end = 13.dp)) {
                Text(
                    "CAMBIAR",
                    color = colorWhite,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.End,
                    style = TextStyle(
                        drawStyle = Stroke(
                            width = 4f,
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
                    "CUMPLEAÑOS",
                    color = colorPrin,
                    fontSize = 45.sp,
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
                    "CUMPLEAÑOS",
                    color = colorPrin,
                    fontSize = 45.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(160.dp))
            DatePicker(
                iniYear = savedYear,
                iniMonth = savedMonth,
                iniDay = savedDay
            ){ year, month, day -> selectedDate = Triple(year, month, day) }

            Spacer(modifier = Modifier.height(284.dp))
            ContinueButton(
                text = "ACTUALIZAR",
                onClick = {
                    val dateString = formatDate(selectedDate)

                    profileViewModel.updateProfile(
                        UpdateRequest(
                            date_birth = dateString
                    ))
                    Toast.makeText(context, "Fecha de cumpleaños actualizada exitosamente", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(0.965f)
            )
        }
    }
}

fun formatDate(triple: Triple<Int, Int, Int>): String {
    val (year, month, day) = triple
    val mm = (month + 1).toString().padStart(2, '0')
    val dd = day.toString().padStart(2, '0')
    return "$year-$mm-$dd"
}