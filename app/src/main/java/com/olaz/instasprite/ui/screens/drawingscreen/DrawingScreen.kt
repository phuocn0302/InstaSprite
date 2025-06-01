package com.olaz.instasprite.ui.screens.drawingscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.ui.theme.DrawingScreenColor
import com.olaz.instasprite.utils.UiUtils
import kotlin.math.roundToInt

@SuppressLint("DefaultLocale")
@Composable
fun DrawingScreen(viewModel: DrawingScreenViewModel) {
    UiUtils.SetStatusBarColor(DrawingScreenColor.PaletteBarColor)
    UiUtils.SetNavigationBarColor(DrawingScreenColor.PaletteBarColor)

    val viewModel = viewModel
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ColorPalette(
                modifier = Modifier
                    .background(DrawingScreenColor.PaletteBarColor)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                viewModel = viewModel
            )
        },

        bottomBar = {
            Column {
                Slider(
                    value = uiState.canvasScale,
                    onValueChange = { viewModel.setCanvasScale(it) },
                    valueRange = 0.5f..10f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = DrawingScreenColor.SelectedToolColor,
                        inactiveTrackColor = DrawingScreenColor.SelectedToolColor

                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DrawingScreenColor.PaletteBarColor)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                ToolSelector(
                    modifier = Modifier
                        .background(DrawingScreenColor.PaletteBarColor)
                        .padding(horizontal = 5.dp, vertical = 8.dp),
                    viewModel = viewModel
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DrawingScreenColor.BackgroundColor)
                .pointerInput(uiState.selectedTool) {
                    detectTransformGestures(
                        onGesture = { _, pan, zoom, _ ->
                            viewModel.setCanvasScale(uiState.canvasScale * zoom)
                            viewModel.setCanvasOffset(uiState.canvasOffset + pan)
                        }
                    )
                }
        ) {

            // Canvas section
            PixelCanvas(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(10.dp)
                    .offset {
                        IntOffset(uiState.canvasOffset.x.roundToInt(), uiState.canvasOffset.y.roundToInt())
                    }
                    .graphicsLayer(
                        scaleX = uiState.canvasScale,
                        scaleY = uiState.canvasScale,
                    )
                    .fillMaxSize()
                    .fillMaxHeight(0.7f),
                viewModel = viewModel
            )
        }
    }
}
