package com.example.posecoach.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.posecoach.R
import com.example.posecoach.ui.theme.colorDark
import com.example.posecoach.ui.theme.colorError
import com.example.posecoach.ui.theme.colorErrorText
import com.example.posecoach.ui.theme.colorPrin
import com.example.posecoach.ui.theme.colorSec
import com.example.posecoach.ui.theme.colorWhite

@Composable
fun HomeMenu(
    navController: NavController,
    selectedOpt: String,
    isRecording: Boolean,
    onRecordClick: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(if(selectedOpt == "camera") colorDark else Color.Transparent)
                .drawBehind {
                    val strokeWidth = 2.dp.toPx()
                    val y = 0f + strokeWidth / 2
                    drawLine(
                        color = colorDark,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth
                    )
                }
                .padding(top = 10.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // HOME
                Image(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home",
                    modifier = Modifier
                        .size(33.dp)
                        .clickable { navController.navigate("home") },
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(
                        if (selectedOpt == "home") colorDark else colorWhite
                    )
                )

                // CAMERA / RECORD / STOP
                Image(
                    painter =
                        if(isRecording) painterResource(id = R.drawable.stop)
                        else if(selectedOpt == "camera")
                            painterResource(id = R.drawable.record)
                        else
                            painterResource(id = R.drawable.camera),
                    contentDescription = "Record Button",
                    modifier = Modifier
                        .size(if(selectedOpt == "camera") 37.dp else 33.dp)
                        .clickable {
                            if(selectedOpt == "camera") onRecordClick()
                            else navController.navigate("camera")
                        },
                    contentScale = ContentScale.Fit,
                    colorFilter =
                        if(isRecording) null
                        else ColorFilter.tint(
                            if(selectedOpt == "camera") colorErrorText else colorWhite
                        )
                )

                // PROFILE
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { navController.navigate("profile") },
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(
                        if (selectedOpt == "profile") colorDark else colorWhite
                    )
                )
            }
        }
    }
}