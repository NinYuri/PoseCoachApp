package com.example.posecoach.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.posecoach.R
import com.example.posecoach.ui.theme.colorDark
import com.example.posecoach.ui.theme.colorWhite

@Composable
fun ScreenLoader(
    isLoading: Boolean,
    loadingText: String = "Cargando...",
){
    if(isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .zIndex(1000f),
                contentAlignment = Alignment.Center
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = colorDark,
                    strokeWidth = 4.dp
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    loadingText,
                    color = colorWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily(Font(R.font.figtree))
                )
            }
        }
    }
}