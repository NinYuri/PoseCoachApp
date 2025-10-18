package com.example.posecoach.userScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.posecoach.R
import com.example.posecoach.components.ContinueButton
import com.example.posecoach.ui.theme.colorPrin
import com.example.posecoach.ui.theme.colorWhite

@Composable
fun OTPScreen(navController: NavController) {
    var otpcode by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 270.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.otp),
                contentDescription = "OTP",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp, 0.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier.padding(top = 117.dp),
                contentAlignment = Alignment.Center
            ){
                // Stroke
                Text(
                    "VERIFICA",
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

                // Relleno
                Text(
                    "VERIFICA",
                    color = colorWhite,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa))
                )
            }

            Spacer(modifier = Modifier.height(5.dp))
            Box(contentAlignment = Alignment.Center){
                // Stroke
                Text(
                    "TU IDENTIDAD",
                    color = colorWhite,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    style = TextStyle(
                        drawStyle = Stroke(
                            width = 4f,
                            join = StrokeJoin.Round
                        )
                    )
                )

                // Relleno
                Text(
                    "TU IDENTIDAD",
                    color = colorWhite,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa))
                )
            }

            Spacer(modifier = Modifier.height(23.dp))
            Text(
                "Por favor, introduce el código que te envié",
                color = colorWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily(Font(R.font.figtree))
            )

            Spacer(modifier = Modifier.height(45.dp))
            OutlinedTextField(
                value = otpcode,
                onValueChange = {
                    if (it.length <= 6)
                        otpcode = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(10.dp),
                        clip = false,
                        ambientColor = Color.Black.copy(alpha = 0.7f),
                        spotColor = Color.Black.copy(alpha = 0.7f)
                    ),
                placeholder = {
                    Text(
                        "― ― ― ― ― ―",
                        color = colorWhite,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily(Font(R.font.figtree)),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                textStyle = LocalTextStyle.current.merge(
                    TextStyle(
                        color = colorWhite,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily(Font(R.font.figtree)),
                        textAlign = TextAlign.Center
                    )
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colorWhite,
                    unfocusedTextColor = colorWhite,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = colorWhite,
                    focusedContainerColor = colorPrin.copy(alpha = 0.6f),
                    unfocusedContainerColor = colorPrin.copy(alpha = 0.6f),
                ),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = null,
                leadingIcon = null
            )

            Spacer(modifier = Modifier.height(15.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                contentAlignment = Alignment.TopEnd
            ){
                Text(
                    "Reenviar código en 00:38",
                    color = colorWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    fontFamily = FontFamily(Font(R.font.figtree)),
                )
            }

            Spacer(modifier = Modifier.height(412.dp))
            ContinueButton(
                onClick = { navController.navigate("username") }
            )
        }
    }
}