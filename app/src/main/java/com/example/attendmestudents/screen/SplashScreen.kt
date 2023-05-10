package com.example.attendmestudents.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.attendmestudents.navigation.Screens
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navHostController: NavHostController) {
    val image = painterResource(id = com.example.attendmestudents.R.drawable.login_image)
    var animationStarted by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        animationStarted = true
        delay(2000)

        if(Firebase.auth.currentUser != null)
            navHostController.navigate(Screens.HomeScreen.route){
                popUpTo(Screens.SplashScreen.route){
                    inclusive = true
                }
            }
        else{
            navHostController.navigate(Screens.LoginScreen.route){
                popUpTo(Screens.SplashScreen.route){
                    inclusive = true
                }
            }
        }
    }

    val alpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(1000)
    )
    val enterOffset by animateDpAsState(
        targetValue = if (animationStarted) 0.dp else 100.dp,
        animationSpec = tween(1000)
    )

    Surface() {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = image,
                    tint = Color.Unspecified,
                    contentDescription = "",
                    modifier = Modifier
                        .size(250.dp)
                        .alpha(alpha)
                        .offset(y = enterOffset),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Attend Me",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.alpha(alpha),
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}