package com.example.posecoach.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.posecoach.R
import com.example.posecoach.ui.theme.colorDarker
import com.example.posecoach.ui.theme.colorSec
import com.example.posecoach.ui.theme.colorWhite
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

data class CalendarDay(
    val date: LocalDate,
    val dayNumber: Int,
    val weekDay: String,
    val isToday: Boolean = false
)

@SuppressLint("FrequentlyChangingValue")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Calendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
){
    var currentWeekStart by remember { mutableStateOf(getWeekStart(LocalDate.now())) }
    val currentWeekDays = remember(currentWeekStart) {
        generateWeekDays(currentWeekStart)
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            currentWeekDays.forEach { day ->
                WeekDayItem(
                    day = day,
                    isSelected = day.date == selectedDate,
                    onDaySelected = { onDateSelected(day.date) }
                )
            }
        }
    }
}

@Composable
fun WeekDayItem(
    day: CalendarDay,
    isSelected: Boolean,
    onDaySelected: () -> Unit
){
    Column(
        modifier = Modifier
            .width(45.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                when {
                    isSelected -> colorSec
                    day.isToday -> colorDarker
                    else -> Color.Transparent
                }
            )
            .clickable { onDaySelected() }
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            day.dayNumber.toString(),
            color = when {
                isSelected -> Color.Black
                day.isToday -> colorWhite
                else -> colorWhite
            },
            fontWeight = if (isSelected || day.isToday) FontWeight.Bold else FontWeight.Normal,
            fontSize = 17.sp,
            fontFamily = FontFamily(Font(R.font.figtree))
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            day.weekDay,
            color = when {
                isSelected -> Color.Black
                day.isToday -> colorWhite
                else -> colorWhite
            },
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.figtree))
        )

        /*if(day.isToday && !isSelected) {
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(colorSec)
            )
        }*/
    }
}

// Obtener el domingo de la semana actual
@RequiresApi(Build.VERSION_CODES.O)
private fun getWeekStart(date: LocalDate): LocalDate {
    val locale = Locale("es", "ES")
    val dayOfWeek = date.dayOfWeek.value // 1 = Lunes, 2 = Martes, ..., 7 = Domingo
    val daysToSubtract = if (dayOfWeek == 7) 0 else dayOfWeek

    return date.minusDays(daysToSubtract.toLong())
}

// Generar los 7 días de la semana
@RequiresApi(Build.VERSION_CODES.O)
private fun generateWeekDays(weekStart: LocalDate): List<CalendarDay> {
    val locale = Locale("es", "ES")

    return (0 until 7).map { offset ->
        val date = weekStart.plusDays(offset.toLong())
        CalendarDay(
            date = date,
            dayNumber = date.dayOfMonth,
            weekDay = date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale).replace(".", ""),
            isToday = date == LocalDate.now()
        )
    }
}