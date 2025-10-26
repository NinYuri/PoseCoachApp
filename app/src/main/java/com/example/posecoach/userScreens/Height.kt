package com.example.posecoach.userScreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
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
import com.example.posecoach.ui.theme.colorWhite
import com.example.posecoach.ui.theme.colorSec

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeightPicker(onHeightSelected: (Int) -> Unit) {
    var selectedCm by remember { mutableStateOf(160) }
    val heights = (140..200).toList()

    Box(
        modifier = Modifier
            .width(140.dp)
            .height(96.dp)
    ){
        Box(
            modifier = Modifier
                .width(140.dp)
                .height(40.dp)
                .align(Alignment.Center)
                .border(2.dp, colorWhite, RoundedCornerShape(10.dp)),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PickerColumn(
                items = heights.map { it.toString() },
                selectedItem = selectedCm.toString(),
                onItemSelected = {
                    selectedCm = it.toInt()
                    onHeightSelected(selectedCm)
                }
            )

            Box(
                modifier = Modifier.fillMaxWidth(0.5f),
                contentAlignment = Alignment.TopEnd
            ){
                Text(
                    "cm",
                    color = colorWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PickerColumn(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
){
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = items.indexOf(selectedItem).coerceAtLeast(0)
    )

    val itemHeightPx = with(LocalDensity.current) { 86.dp.toPx() }
    val coroutineScope = rememberCoroutineScope()

    val visibleIndex by remember {
        derivedStateOf {
            val scrollOffset = listState.firstVisibleItemScrollOffset
            val centerOffset = itemHeightPx / 2

            val middleIndex = listState.firstVisibleItemIndex + if(scrollOffset > centerOffset) 1 else 0
            middleIndex.coerceIn(0, items.lastIndex)
        }
    }

    LaunchedEffect(visibleIndex) {
        onItemSelected(items[visibleIndex])
    }

    LazyColumn(
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.height(120.dp)
    ){
        item{ Spacer(modifier = Modifier.height(32.dp)) }

        itemsIndexed(items) { _, item ->
            val isSelected = item == items[visibleIndex]
            Text(
                text = item,
                color = if (isSelected) colorWhite else colorWhite.copy(alpha = 0.5f),
                fontSize = 18.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier
                    .height(32.dp)
                    .padding(vertical = 6.dp),
                textAlign = TextAlign.Center
            )
        }

        item{ Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
fun HeightScreen(navController: NavController, registroViewModel: RegistroViewModel) {
    var selectedHeight by remember { mutableStateOf(160) }

    LaunchedEffect(selectedHeight) {
        registroViewModel.usuario.value = registroViewModel.usuario.value.copy(
            height = selectedHeight
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
                        .clickable { navController.navigate("birthday") },
                    contentScale = ContentScale.Fit
                )
            }

            Box(modifier = Modifier.padding(top = 17.dp)) {
                Text(
                    "TU ESTATURA",
                    color = colorWhite,
                    fontSize = 50.sp,
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
                    "TU ESTATURA",
                    color = colorWhite,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa))
                )
            }

            Spacer(modifier = Modifier.height(7.dp))
            Box( modifier = Modifier.padding(start = 5.dp) )
            {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("Me sirve para ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorSec,
                                fontWeight = FontWeight.Bold,
                                drawStyle = Stroke(width = 2f)
                            )
                        ){
                            append("ajustar ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("tus metas y cálculos de progreso.")
                        }
                    },
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.figtree)),
                    lineHeight = 22.sp
                )

                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("Me sirve para ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorSec,
                                fontWeight = FontWeight.Bold
                            )
                        ){
                            append("ajustar ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = colorWhite,
                                fontWeight = FontWeight.Normal
                            )
                        ){
                            append("tus metas y cálculos de progreso.")
                        }
                    },
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.figtree)),
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(65.dp))
            HeightPicker { height -> selectedHeight = height }

            Spacer(modifier = Modifier.height(412.dp))
            ContinueButton(
                onClick = {
                    registroViewModel.usuario.value = registroViewModel.usuario.value.copy(
                        height = selectedHeight
                    )
                    navController.navigate("goal")
                },
                modifier = Modifier.fillMaxWidth(0.965f)
            )
        }
    }
}