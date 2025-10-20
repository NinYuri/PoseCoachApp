package com.example.posecoach.userScreens

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
import com.example.posecoach.data.viewModel.RegistroViewModel
import com.example.posecoach.ui.theme.colorDarker
import com.example.posecoach.ui.theme.colorWhite
import com.example.posecoach.ui.theme.colorSec
import java.util.Calendar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DatePicker(onDateSelected: (Int, Int, Int) -> Unit ) {
    var selectedYear by remember { mutableStateOf(2005) }
    var selectedMonth by remember { mutableStateOf(6) }
    var selectedDay by remember { mutableStateOf(6) }

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
fun BirthdayScreen(navController: NavController, registroViewModel: RegistroViewModel) {
    var selectedDate by remember { mutableStateOf(Triple(1998,4,4)) }

    LaunchedEffect(selectedDate) {
        registroViewModel.updateBirthday(
            selectedDate.first,
            selectedDate.second,
            selectedDate.third
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 250.dp)
        ){
            Image(
                painter = painterResource(R.drawable.onb_w),
                contentDescription = "Woman Onboarding",
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = (65).dp),
                contentScale = ContentScale.Fit
            )
        }

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
                        .clickable { navController.navigate("gender") },
                    contentScale = ContentScale.Fit
                )
            }

            Box(modifier = Modifier.padding(top = 17.dp)) {
                Text(
                    "TU CUMPLEAÑOS",
                    color = colorWhite,
                    fontSize = 39.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    style = TextStyle(
                        drawStyle = Stroke(
                            width = 4f,
                            join = StrokeJoin.Round
                        )
                    )
                )

                Text(
                    "TU CUMPLEAÑOS",
                    color = colorWhite,
                    fontSize = 39.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa))
                )
            }

            Spacer(modifier = Modifier.height(7.dp))
            Box(modifier = Modifier.padding(start = 5.dp))
            {
                // Stroke
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("Me ayuda a ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorSec,
                                fontWeight = FontWeight.Bold,
                                drawStyle = Stroke(width = 2f)
                            )
                        ){
                            append("personalizar ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("tus cálculos y recomendaciones.")
                        }
                    },
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.figtree)),
                    lineHeight = 22.sp
                )

                // Relleno
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("Me ayuda a ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorSec,
                                fontWeight = FontWeight.Bold
                            )
                        ){
                            append("personalizar ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("tus cálculos y recomendaciones.")
                        }
                    },
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.figtree)),
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(108.dp))
            DatePicker { year, month, day -> selectedDate = Triple(year, month, day) }

            Spacer(modifier = Modifier.height(328.dp))
            ContinueButton(
                onClick = {
                    registroViewModel.updateBirthday(
                        selectedDate.first,
                        selectedDate.second,
                        selectedDate.third
                    )
                    navController.navigate("height")
                          },
                modifier = Modifier.fillMaxWidth(0.965f)
            )
        }
    }
}