package com.example.posecoach.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.posecoach.R
import com.example.posecoach.ui.theme.colorDark
import com.example.posecoach.ui.theme.colorWhite
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ExerciseCard(
    imageUrl: String,
    name: String,
    reps: String,
    rest: String,
    cameraClick: () -> Unit
){
    var isCompleted by remember { mutableStateOf(false) }

    SwipeableItemWithActions(
        isRevealed = false,
        onExpanded = {},
        onCollapsed = {},
        actions = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(80.dp)
                    .background(colorDark, RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .clickable { cameraClick() },
                contentAlignment = Alignment.Center
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Image(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "Camara",
                        modifier = Modifier.size(25.dp)
                    )
                    Text(
                        "Analizar",
                        color = colorWhite,
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.figtree)),
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
        }
    ){ offsetValue ->
        val maxOffset = 80f
        val cardShape = RoundedCornerShape(
            topStart = if (offsetValue >= maxOffset) 0.dp else 16.dp,
            bottomStart = if (offsetValue >= maxOffset) 0.dp else 16.dp,
            topEnd = 16.dp,
            bottomEnd = 16.dp
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(
                    color = colorWhite.copy(alpha = 0.2f),
                    shape = cardShape
                )
                .border(
                    1.dp,
                    color = colorWhite.copy(0.3f),
                    shape = cardShape
                )
                .clickable { isCompleted = !isCompleted },
            contentAlignment = Alignment.Center
        ){
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .clip(shape = RoundedCornerShape(16.dp))
            ){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .decoderFactory(coil.decode.ImageDecoderDecoder.Factory())
                        .crossfade(true)
                        .build(),
                    contentDescription = "Exercise",
                    modifier = Modifier
                        .width(100.dp)
                        .fillMaxHeight()
                        .clip(shape = RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 15.dp, vertical = 15.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        name,
                        color = colorWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.figtree))
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            reps,
                            color = colorWhite,
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.figtree))
                        )

                        Text(
                            rest,
                            color = colorWhite,
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.figtree))
                        )
                    }
                }
            }

            AnimatedVisibility(visible = isCompleted) {
                val scale by animateFloatAsState( targetValue = if(isCompleted) 1f else 0f )

                Image(
                    painter = painterResource(id = R.drawable.check),
                    contentDescription = "Complete",
                    modifier = Modifier
                        .size(100.dp)
                        .graphicsLayer{
                            scaleX = scale
                            scaleY = scale
                        }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(25.dp))
}
