package com.olaz.instasprite.ui.screens.drawingscreen

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.domain.tool.EraserTool
import com.olaz.instasprite.domain.tool.PencilTool
import com.olaz.instasprite.ui.theme.DrawingScreenColor

@Composable
fun PixelCanvas(
    modifier: Modifier = Modifier,
    viewModel: DrawingScreenViewModel
) {
    val model = viewModel.canvasModel
    val uiState by viewModel.uiState.collectAsState()
    val pixelChangeTrigger by viewModel.pixelChangeTrigger.collectAsState()

    Log.d("RecomposeCheck", "PixelCanvas recomposed")

    Box(
        modifier = modifier
            .aspectRatio(uiState.canvasWidth.toFloat() / uiState.canvasHeight.toFloat())
            .border(10.dp, DrawingScreenColor.CanvasBorderColor)
            .padding(10.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(uiState.selectedTool) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        val startCell = down.position.toGridCell(
                            size.width, size.height,
                            uiState.canvasWidth, uiState.canvasHeight
                        )

                        viewModel.applyTool(
                            model,
                            uiState.selectedTool,
                            startCell.y,
                            startCell.x,
                            uiState.selectedColor
                        )

                        if (uiState.selectedTool is PencilTool || uiState.selectedTool is EraserTool) {
                            drag(down.id) { change ->
                                change.consume()
                                val dragCell = change.position.toGridCell(
                                    size.width, size.height,
                                    uiState.canvasWidth, uiState.canvasHeight
                                )
                                viewModel.applyTool(
                                    model,
                                    uiState.selectedTool,
                                    dragCell.y,
                                    dragCell.x,
                                    uiState.selectedColor
                                )
                            }
                        }
                    }
                }
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val cellWidth = canvasWidth / uiState.canvasWidth
            val cellHeight = canvasHeight / uiState.canvasHeight

            // To recompose when pixelChangeTrigger changes
            pixelChangeTrigger.hashCode()

            // Draw grid
            for (row in 0 until uiState.canvasHeight) {
                for (col in 0 until uiState.canvasWidth) {
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
                        color = model.getPixel(row, col),
                        topLeft = topLeft,
                        size = cellSize
                    )

                    // Grid
                    drawRect(
                        color = Color.LightGray.copy(alpha = 0.2f),
                        topLeft = topLeft,
                        size = cellSize,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1f)
                    )
                }
            }
        }
    }

}

fun Offset.toGridCell(canvasWidth: Int, canvasHeight: Int, cols: Int, rows: Int): IntOffset {
    val cellWidth = canvasWidth / cols
    val cellHeight = canvasHeight / rows

    val gridX = (x / cellWidth).toInt().coerceIn(0, cols - 1)
    val gridY = (y / cellHeight).toInt().coerceIn(0, rows - 1)
    return IntOffset(gridX, gridY)
}
