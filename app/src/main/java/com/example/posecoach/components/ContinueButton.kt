package com.example.posecoach.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.posecoach.R
import com.example.posecoach.ui.theme.colorWhite
import com.example.posecoach.ui.theme.colorPrin

@Composable
fun ContinueButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "CONTINUAR",
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(10.dp),
                clip = false,
                ambientColor = Color.Black.copy(alpha = 0.7f),
                spotColor = Color.Black.copy(alpha = 0.7f)
            ),
        colors = ButtonDefaults.buttonColors( containerColor = colorWhite ),
        shape = RoundedCornerShape(10.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        ),
        enabled = enabled
    ){
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            // Barra Izquierda
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(colorPrin, RoundedCornerShape(50.dp, 0.dp, 0.dp, 50.dp))
            )

            Box(contentAlignment = Alignment.Center) {
                Text(
                    text,
                    color = Color.Black,
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        drawStyle = Stroke(
                            width = 4.5f,
                            join = StrokeJoin.Round
                        )
                    ),
                    modifier = Modifier.offset(x = (-0.57).dp)
                )
                Text(
                    text,
                    color = Color.Black,
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    fontWeight = FontWeight.Bold
                )
            }

            // Barra Derecha
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(colorPrin, RoundedCornerShape(0.dp, 50.dp, 50.dp, 0.dp))
            )
        }
    }
}