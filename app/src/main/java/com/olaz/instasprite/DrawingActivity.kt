package com.olaz.instasprite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.olaz.instasprite.ui.screens.drawingscreen.DrawingScreen
import com.olaz.instasprite.ui.theme.Typography

class DrawingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val canvasWidth = intent.getIntExtra(EXTRA_CANVAS_WIDTH, 16)
        val canvasHeight = intent.getIntExtra(EXTRA_CANVAS_HEIGHT, 16)

        setContent {
            MaterialTheme(
                typography = Typography
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DrawingScreen(canvasWidth, canvasHeight)
                }
            }
        }
    }

    companion object {
        const val EXTRA_CANVAS_WIDTH = "com.olaz.instasprite.CANVAS_WIDTH"
        const val EXTRA_CANVAS_HEIGHT = "com.olaz.instasprite.CANVAS_HEIGHT"
    }
}