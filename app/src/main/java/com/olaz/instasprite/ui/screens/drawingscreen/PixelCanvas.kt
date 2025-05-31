package com.olaz.instasprite.ui.screens.drawingscreen

import android.annotation.SuppressLint
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.domain.tool.EraserTool
import com.olaz.instasprite.domain.tool.FillTool
import com.olaz.instasprite.domain.tool.PencilTool
import com.olaz.instasprite.ui.theme.DrawingScreenColor

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PixelCanvas(
    modifier: Modifier = Modifier,
    viewModel: DrawingScreenViewModel
) {
    val model = viewModel.canvasModel
    val canvasHistoryManager = viewModel.canvasHistoryManager

    var canvasWidth by remember { mutableIntStateOf(viewModel.uiState.value.canvasWidth) }
    var canvasHeight by remember { mutableIntStateOf(viewModel.uiState.value.canvasHeight) }
    var selectedTool = viewModel.uiState.value.selectedTool
    var selectedColor = viewModel.uiState.value.selectedColor

    LaunchedEffect(Unit) {
        viewModel.uiState.collect { state ->
            canvasWidth = state.canvasWidth
            canvasHeight = state.canvasHeight
            selectedTool = state.selectedTool
            selectedColor = state.selectedColor
        }
    }

    val pixelChangeTrigger by viewModel.pixelChangeTrigger.collectAsState()

    Box(
        modifier = modifier
            .aspectRatio(canvasWidth.toFloat() / canvasHeight.toFloat())
            .border(10.dp, DrawingScreenColor.CanvasBorderColor)
            .padding(10.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    awaitEachGesture {
                        if (selectedTool in listOf(PencilTool, EraserTool, FillTool)) {
                            canvasHistoryManager.saveState(model.getAllPixels())
                        }

                        val down = awaitFirstDown()
                        val startCell = down.position.toGridCell(
                            size.width, size.height,
                            canvasWidth, canvasHeight
                        )

                        viewModel.applyTool(
                            model,
                            selectedTool,
                            startCell.y,
                            startCell.x,
                            selectedColor
                        )

                        if (selectedTool in listOf(PencilTool, EraserTool)) {
                            drag(down.id) { change ->
                                change.consume()
                                val dragCell = change.position.toGridCell(
                                    size.width, size.height,
                                    canvasWidth, canvasHeight
                                )
                                viewModel.applyTool(
                                    model,
                                    selectedTool,
                                    dragCell.y,
                                    dragCell.x,
                                    selectedColor
                                )
                            }
                        }
                    }
                }
        ) {
            val _canvasWidth = size.width
            val _canvasHeight = size.height
            val cellWidth = _canvasWidth / canvasWidth
            val cellHeight = _canvasHeight / canvasHeight

            // To recompose when pixelChangeTrigger changes
            pixelChangeTrigger.hashCode()
            Log.d("RecomposeCheck", "PixelCanvas recomposed")

            // Draw grid
            for (row in 0 until canvasHeight) {
                for (col in 0 until canvasWidth) {
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
