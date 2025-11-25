package com.example.posecoach.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.posecoach.R
import com.example.posecoach.ui.theme.colorDark
import com.example.posecoach.ui.theme.colorPrin
import com.example.posecoach.ui.theme.colorWhite

@Composable
fun Dialog(
    onDismiss: () -> Unit,
    textTitle: String,
    textDesc: String,
    onClick1: () -> Unit,
    textOpt1: String,
    onClick2: () -> Unit,
    textOpt2: String = "Cancelar"
){
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                textTitle,
                color = colorDark,
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.figtree)),
                fontWeight = FontWeight.Bold,
                style = TextStyle( drawStyle = Stroke(2f) ),
                letterSpacing = 0.05.em,
                modifier = Modifier.offset(y = (1.4).dp)
            )

            Text(
                textTitle,
                color = colorDark,
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.figtree)),
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.05.em
            )
        },
        text = {
            Text(
                text = textDesc,
                color = Color.Black,
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.figtree)),
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onClick1() },
                modifier = Modifier
                    .background(
                        color = colorPrin,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding( vertical = 3.dp )
            ){
                Text(
                    textOpt1,
                    color = colorWhite,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.figtree))
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onClick2() },
                modifier = Modifier
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding( vertical = 3.dp )
            ){
                Text(
                    textOpt2,
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.figtree))
                )
            }
        }
    )
}