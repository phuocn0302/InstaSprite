package com.olaz.instasprite.ui.screens.drawingscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.data.model.PixelCanvasModel
import com.olaz.instasprite.ui.theme.DrawingScreenColor

@Composable
fun PixelCanvas(
    model: PixelCanvasModel,
    modifier: Modifier = Modifier,
    viewModel: DrawingScreenViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .aspectRatio(uiState.canvasSize.second.toFloat() / uiState.canvasSize.first.toFloat())
            .border(10.dp, DrawingScreenColor.CanvasBorderColor)
            .padding(10.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val canvasWidth = size.width.toFloat()
                        val canvasHeight = size.height.toFloat()
                        val cellWidth = canvasWidth / uiState.canvasSize.second
                        val cellHeight = canvasHeight / uiState.canvasSize.first

                        val gridX = (offset.x / cellWidth).toInt().coerceIn(0, uiState.canvasSize.second - 1)
                        val gridY = (offset.y / cellHeight).toInt().coerceIn(0, uiState.canvasSize.first - 1)

                        viewModel.applyTool(model, uiState.selectedTool, gridY, gridX, uiState.selectedColor)
                    }
                }
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val cellWidth = canvasWidth / uiState.canvasSize.second
            val cellHeight = canvasHeight / uiState.canvasSize.first

            // Draw grid
            for (row in 0 until uiState.canvasSize.first) {
                for (col in 0 until uiState.canvasSize.second) {
                    val topLeft = Offset(col * cellWidth, row * cellHeight)
                    val cellSize = Size(cellWidth, cellHeight)

                    // Checkerboard
                    drawRect(
                        color = if ((row + col) % 2 == 0) DrawingScreenColor.CheckerColor1 else DrawingScreenColor.CheckerColor2,
                        topLeft = topLeft,
                        size = cellSize
                    )

                    // Pixel
                    drawRect(
                        color = model.getPixel(col, row),
                        topLeft = topLeft,
                        size = cellSize
                    )

                    // Grid
                    drawRect(
                        color = Color.LightGray.copy(alpha = 0.1f),
                        topLeft = topLeft,
                        size = cellSize,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1f)
                    )
                }
            }
        }
    }
}