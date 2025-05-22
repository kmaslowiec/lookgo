package com.kmaslowiec.lookgo.location.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LocationDisplay(latitude: Double, longitude: Double) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Current Location",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedContent(
                targetState = latitude,
                transitionSpec = {
                    (slideInHorizontally() { height -> height } + fadeIn()).togetherWith(
                        slideOutHorizontally() { height -> -height } + fadeOut())
                }
            ) { lat ->
                Text(
                    text = "Latitude: %.6f".format(lat),
                    fontSize = 18.sp
                )
            }
            AnimatedContent(
                targetState = longitude,
                transitionSpec = {
                    (slideInHorizontally() { height -> height } + fadeIn()).togetherWith(
                        slideOutHorizontally() { height -> -height } + fadeOut())
                }
            ) { lon ->
                Text(
                    text = "Longitude: %.6f".format(lon),
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun LocationDisplayPreview(){
    LocationDisplay(
        latitude = 12.555,
        longitude = 14.666
    )
}
