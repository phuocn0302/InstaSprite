package com.olaz.instasprite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.olaz.instasprite.data.model.PixelCanvasModel
import com.olaz.instasprite.data.repository.PixelCanvasRepository
import com.olaz.instasprite.data.repository.StorageLocationRepository
import com.olaz.instasprite.ui.screens.drawingscreen.DrawingScreen
import com.olaz.instasprite.ui.screens.drawingscreen.DrawingScreenViewModel
import com.olaz.instasprite.ui.theme.Typography
import androidx.lifecycle.lifecycleScope
import com.olaz.instasprite.data.database.AppDatabase
import com.olaz.instasprite.data.repository.ISpriteDatabaseRepository
import kotlinx.coroutines.launch

class DrawingActivity : ComponentActivity() {

    private lateinit var viewModel: DrawingScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val spriteId = intent?.getStringExtra(EXTRA_SPRITE_ID)
        require(spriteId != "") { "Sprite ID must be passed to DrawingActivity" }

        val canvasWidth = intent?.getIntExtra(EXTRA_CANVAS_WIDTH, 16) ?: 16
        val canvasHeight = intent?.getIntExtra(EXTRA_CANVAS_HEIGHT, 16) ?: 16

        val storageLocationRepository = StorageLocationRepository(applicationContext)
        val pixelCanvasRepository = PixelCanvasRepository(PixelCanvasModel(canvasWidth, canvasHeight))

        val database = AppDatabase.getInstance(applicationContext)
        val spriteDataRepository = ISpriteDatabaseRepository(database.spriteDataDao(), database.spriteMetaDataDao())

        viewModel = DrawingScreenViewModel(spriteId!!, storageLocationRepository, pixelCanvasRepository, spriteDataRepository)

        lifecycleScope.launch {
            viewModel.loadFromDB()
            viewModel.saveToDB()
        }

        setContent {
            MaterialTheme(typography = Typography) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DrawingScreen(viewModel)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            viewModel.saveToDB()
        }
    }

    companion object {
        const val EXTRA_CANVAS_WIDTH = "com.olaz.instasprite.CANVAS_WIDTH"
        const val EXTRA_CANVAS_HEIGHT = "com.olaz.instasprite.CANVAS_HEIGHT"
        const val EXTRA_SPRITE_ID = "com.olaz.instasprite.SPRITE_ID"
    }
}
