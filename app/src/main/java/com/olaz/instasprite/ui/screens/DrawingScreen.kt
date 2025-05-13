package com.olaz.instasprite.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.ui.canvas.PixelCanvas
import com.olaz.instasprite.ui.canvas.PixelCanvasViewModel
import com.olaz.instasprite.ui.components.ColorPalette
import com.olaz.instasprite.ui.components.ColorPaletteViewModel
import com.olaz.instasprite.ui.components.ToolSelector
import com.olaz.instasprite.ui.components.ToolSelectorViewModel
import com.olaz.instasprite.ui.theme.DrawingScreenColor
import com.olaz.instasprite.utils.UiUtils

@Composable
fun DrawingScreen(viewModel: DrawingScreenViewModel = DrawingScreenViewModel()) {
    UiUtils.SetStatusBarColor(DrawingScreenColor.PaletteBarColor)
    val uiState by viewModel.uiState.collectAsState()

    val canvasSize = 16
    var canvasPixels by remember {
        mutableStateOf(List(canvasSize) { List(canvasSize) { DrawingScreenColor.DefaultCanvasColor } })
    }

    val pixelCanvasViewModel =
        PixelCanvasViewModel(canvasSize, canvasPixels)
    val colorPaletteViewModel = ColorPaletteViewModel()
    val toolSelectorViewModel = ToolSelectorViewModel()

    val pixelCanvasState = pixelCanvasViewModel.uiState.collectAsState()
    val colorPaletteState = colorPaletteViewModel.uiState.collectAsState()
    val toolSelectorState = toolSelectorViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ColorPalette(
                modifier = Modifier
                    .background(DrawingScreenColor.PaletteBarColor)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                viewModel = colorPaletteViewModel
            )
        },

        bottomBar = {
            Column {
                Slider(
                    value = uiState.canvasScale,
                    onValueChange = { viewModel.setCanvasScale(it) },
                    valueRange = 0.5f..5f,
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
                    viewModel = toolSelectorViewModel
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DrawingScreenColor.BackgroundColor)
        ) {

            // Canvas section
            PixelCanvas(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp)
                    .aspectRatio(1f)
                    .fillMaxSize()
                    .fillMaxHeight(0.7f)
                    .graphicsLayer(
                        scaleX = uiState.canvasScale,
                        scaleY = uiState.canvasScale,
                    ),
                viewModel = pixelCanvasViewModel
            )
        }
    }
}
