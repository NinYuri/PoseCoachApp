package com.example.posecoach.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.posecoach.R
import com.example.posecoach.ui.theme.colorPrin
import com.example.posecoach.ui.theme.colorSec
import com.example.posecoach.ui.theme.colorWhite

@Composable
fun OptionSelector(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if(isSelected) colorPrin else colorWhite,
        animationSpec = tween(durationMillis = 300),
        label = "borderColor"
    )

    Box(
        modifier = modifier
            .clickable { onClick() }
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(20.dp, 10.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ){
            // Círculo doble
            Box(
                modifier = Modifier.size(20.dp),
                contentAlignment = Alignment.Center
            ){
                // Exterior
                Box(
                    modifier = Modifier
                        .size(19.dp)
                        .border(
                            width = 2.dp,
                            color = borderColor,
                            shape = CircleShape
                        )
                )

                // Interior
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .border(
                            width = 2.dp,
                            color = borderColor,
                            shape = CircleShape
                        )
                        .background(
                            color = borderColor,
                            shape = CircleShape
                        )
                )
            }

            Text(
                text,
                color = colorWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily(Font(R.font.figtree))
            )
        }
    }
}

@Composable
fun OptionSelectorDer(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if(isSelected) colorPrin else colorWhite,
        animationSpec = tween(durationMillis = 300),
        label = "borderColor"
    )

    Box(
        modifier = modifier
            .clickable { onClick() }
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(20.dp, 10.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ){
            Text(
                text,
                color = colorWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily(Font(R.font.figtree)),
                modifier = Modifier.padding(end = 7.dp)
            )

            // Círculo doble
            Box(
                modifier = Modifier.size(20.dp),
                contentAlignment = Alignment.Center
            ){
                // Exterior
                Box(
                    modifier = Modifier
                        .size(19.dp)
                        .border(
                            width = 2.dp,
                            color = borderColor,
                            shape = CircleShape
                        )
                )

                // Interior
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .border(
                            width = 2.dp,
                            color = borderColor,
                            shape = CircleShape
                        )
                        .background(
                            color = borderColor,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}