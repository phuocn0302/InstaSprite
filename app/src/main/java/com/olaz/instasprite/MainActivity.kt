package com.olaz.instasprite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.olaz.instasprite.data.database.AppDatabase
import com.olaz.instasprite.data.repository.ISpriteDatabaseRepository
import com.olaz.instasprite.ui.screens.homescreen.HomeScreen
import com.olaz.instasprite.ui.screens.homescreen.HomeScreenViewModel
import com.olaz.instasprite.ui.theme.Typography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val display = window.windowManager.defaultDisplay
        val modes = display.supportedModes
        val highest = modes.maxByOrNull { it.refreshRate }

        highest?.let {
            val params = window.attributes
            params.preferredDisplayModeId = it.modeId
            window.attributes = params
        }

        val database = AppDatabase.getInstance(applicationContext)
        val spriteDataRepository = ISpriteDatabaseRepository(database.spriteDataDao(), database.spriteMetaDataDao())
        val viewModel = HomeScreenViewModel(spriteDataRepository)

        setContent {
            MaterialTheme(
                typography = Typography
            ) {
                Surface(

                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(viewModel)
                }
            }
        }
    }
}