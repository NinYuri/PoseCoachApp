package com.example.posecoach.profileScreens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.posecoach.R
import com.example.posecoach.components.ContinueButton
import com.example.posecoach.components.OptionSelectorDer
import com.example.posecoach.data.model.UpdateRequest
import com.example.posecoach.data.viewModel.ProfileViewModel
import com.example.posecoach.ui.theme.colorPrin
import com.example.posecoach.ui.theme.colorWhite

@Composable
fun EquipmentChange(navController: NavController, profileViewModel: ProfileViewModel) {
    val selEquip = profileViewModel.selectedEquip.value
    val context = LocalContext.current

    var manc by remember { mutableStateOf(Manc(selEquip)) }
    var cuerpo by remember { mutableStateOf(Cuerpo(selEquip)) }
    var bandas by remember { mutableStateOf(Bandas(selEquip)) }
    var gym by remember { mutableStateOf(Gym(selEquip)) }

    LaunchedEffect(selEquip) {
        manc = Manc(selEquip)
        cuerpo = Cuerpo(selEquip)
        bandas = Bandas(selEquip)
        gym = Gym(selEquip)
    }

    fun selcOption(option: String) {
        manc = option == "mancuernas"
        cuerpo = option == "cuerpo"
        bandas = option == "bandas"
        gym = option == "gimnasio"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 190.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Experience Change",
                modifier = Modifier
                    .size(600.dp)
                    .offset(x = (-17).dp, y = (80).dp),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp, 0.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End
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
                        .clickable { navController.navigate("profile") },
                    contentScale = ContentScale.Fit
                )
            }

            Box( modifier = Modifier.padding(top = 17.dp, end = 13.dp) ){
                Text(
                    "CAMBIAR",
                    color = colorWhite,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.End,
                    style = TextStyle(
                        drawStyle = Stroke(
                            4f,
                            join = StrokeJoin.Round
                        )
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "CAMBIAR",
                    color = colorWhite,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Box( modifier = Modifier.padding(top = 10.dp, end = 10.dp) ){
                Text(
                    "EQUIPO",
                    color = colorPrin,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.End,
                    style = TextStyle(
                        drawStyle = Stroke(
                            4f,
                            join = StrokeJoin.Round
                        )
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "EQUIPO",
                    color = colorPrin,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.comfortaa)),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer( modifier = Modifier.height(30.dp) )
            OptionSelectorDer(
                text = "Máquinas de gimnasio",
                isSelected = gym,
                onClick = { selcOption("gimnasio") },
                modifier = Modifier.padding(end = 15.dp)
            )

            Spacer( modifier = Modifier.height(12.dp) )
            OptionSelectorDer(
                text = "Bandas de resistencia",
                isSelected = bandas,
                onClick = { selcOption("bandas") },
                modifier = Modifier.padding(end = 15.dp)
            )

            Spacer( modifier = Modifier.height(12.dp) )
            OptionSelectorDer(
                text = "Sólo mi cuerpo",
                isSelected = cuerpo,
                onClick = { selcOption("cuerpo") },
                modifier = Modifier.padding(end = 15.dp)
            )

            Spacer( modifier = Modifier.height(12.dp) )
            OptionSelectorDer(
                text = "Mancuernas",
                isSelected = manc,
                onClick = { selcOption("mancuernas") },
                modifier = Modifier.padding(end = 15.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 62.dp),
            contentAlignment = Alignment.BottomCenter
        ){
            ContinueButton(
                text = "ACTUALIZAR",
                onClick = {
                    val selEquip = when {
                        manc -> "mancuernas"
                        cuerpo -> "cuerpo"
                        bandas -> "bandas"
                        gym -> "gimnasio"
                        else -> selEquip
                    }

                    profileViewModel.updateProfile(
                        UpdateRequest(
                            equipment = selEquip
                    ))
                    Toast.makeText(context, "Equipo actualizado exitosamente", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth(0.885f)
                    .padding(end = 10.dp)
            )
        }
    }
}

private fun Manc(option: String): Boolean { return option == "mancuernas" }

private fun Cuerpo(option: String): Boolean { return option == "cuerpo" }

private fun Bandas(option: String): Boolean { return option == "bandas" }

private fun Gym(option: String): Boolean { return option == "gimnasio" }