package com.example.posecoach.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.posecoach.R
import com.example.posecoach.ui.theme.colorSec
import com.example.posecoach.ui.theme.colorWhite

@Composable
fun GlowItem(
    text: String,
    options: Boolean,
    onClick: () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
            .shadow(
                elevation = if(isPressed) 8.dp else 0.dp,
                shape = RoundedCornerShape(10.dp),
                clip = false,
                spotColor = colorSec,
                ambientColor = colorSec
            )
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if(isPressed) 13.dp else 0.dp,
                    shape = RoundedCornerShape(10.dp),
                    clip = false,
                    spotColor = colorSec,
                    ambientColor = colorSec
                )
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Black)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ){
                    onClick()
                }
                .padding(15.dp, 10.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = if(options) 0.dp else 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text,
                    fontSize = 16.sp,
                    color = colorWhite,
                    fontFamily = FontFamily(Font(R.font.figtree)),
                    fontWeight = FontWeight.Medium
                )

                if(options) {
                    Text(
                        ">",
                        fontSize = 24.sp,
                        color = colorWhite,
                        fontFamily = FontFamily(Font(R.font.figtree)),
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}