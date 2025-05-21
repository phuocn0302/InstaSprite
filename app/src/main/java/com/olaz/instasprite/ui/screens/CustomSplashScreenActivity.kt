package com.olaz.instasprite.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.olaz.instasprite.MainActivity
import com.olaz.instasprite.R

@SuppressLint("RestrictedApi", "CustomSplashScreen")
class CustomSplashScreenActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_InstaSprite) // No semicolon needed in Kotlin
        super.onCreate(savedInstanceState)
        setContent{
            SplashScreen()
        }

        // Optionally delay or animate
        // Note: Using Handler().postDelayed is a legacy way.
        // Consider using coroutines for more modern Android development.
        lifecycleScope.launch {
            delay(1000L)
            startActivity(Intent(this@CustomSplashScreenActivity, MainActivity::class.java))
            finish()
        }// Delay in ms
    }
}

@Composable @Preview
fun SplashScreen(){
     Box(
         modifier = Modifier
             .fillMaxSize()
             .background(Color(0xFF112118)),
         contentAlignment = Alignment.Center
     )
     {
         Image(
             painter = painterResource(R.drawable.ic_launcher),
             modifier = Modifier
                 .fillMaxWidth(0.3f)
                 .fillMaxHeight(0.3f),
             contentDescription = "Splash Screen",
         )
     }
}