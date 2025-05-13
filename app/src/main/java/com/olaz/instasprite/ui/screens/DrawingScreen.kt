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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.ui.canvas.PixelCanvas
import com.olaz.instasprite.ui.components.ColorPalette
import com.olaz.instasprite.ui.components.ToolSelector
import com.olaz.instasprite.ui.theme.DrawingScreenColor
import com.olaz.instasprite.utils.ColorPalette
import com.olaz.instasprite.utils.UiUtils

@Composable
fun DrawingScreen() {
    UiUtils.SetStatusBarColor(DrawingScreenColor.PaletteBarColor)

    var selectedColor by remember { mutableStateOf(ColorPalette.Color1) }
    var selectedTool by remember { mutableStateOf("pencil") }

    val canvasSize = 16
    var canvasPixels by remember {
        mutableStateOf(List(canvasSize) { List(canvasSize) { DrawingScreenColor.DefaultCanvasColor } })
    }

    var scale by remember { mutableFloatStateOf(1f) }

    Scaffold(
        topBar = {
            ColorPalette(
                onColorSelected = {
                    selectedColor = it
                    ColorPalette.activeColor = it
                },
                selectedColor = selectedColor,
                modifier = Modifier
                    .background(DrawingScreenColor.PaletteBarColor)
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            )
        },

        bottomBar = {
            Column {
                Slider(
                    value = scale,
                    onValueChange = { newValue -> scale = newValue },
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
                    selectedTool = selectedTool,
                    onToolSelected = { selectedTool = it },
                    modifier = Modifier
                        .background(DrawingScreenColor.PaletteBarColor)
                        .padding(horizontal = 5.dp, vertical = 8.dp)
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
                pixels = canvasPixels,
                onPixelClick = { row, col ->
                    val newCanvas = canvasPixels.toMutableList().apply {
                        val newRow = this[row].toMutableList().apply {
                            this[col] = selectedColor
                        }
                        this[row] = newRow
                    }
                    canvasPixels = newCanvas
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp)
                    .aspectRatio(1f)
                    .fillMaxSize()
                    .fillMaxHeight(0.7f)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                    )
            )
        }
    }
}
