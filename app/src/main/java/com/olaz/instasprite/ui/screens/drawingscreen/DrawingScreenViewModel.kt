package com.olaz.instasprite.ui.screens.drawingscreen

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.olaz.instasprite.domain.tool.PencilTool
import com.olaz.instasprite.domain.tool.Tool
import com.olaz.instasprite.utils.ColorPalette
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DrawingScreenState(
    val selectedColor: Color,
    val selectedTool: Tool,

    val canvasSize: Int = 16,
    val canvasPixels: List<List<Color>>,

    val canvasOffset: Offset,
    val canvasScale: Float,
)

class DrawingScreenViewModel(
    canvasSize: Int,
    canvasPixels: List<List<Color>>
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        DrawingScreenState(
            selectedColor = ColorPalette.Color1,
            selectedTool = PencilTool,

            canvasSize = canvasSize,
            canvasPixels = canvasPixels,

            canvasScale = 1f,
            canvasOffset = Offset.Zero
        )
    )
    val uiState: StateFlow<DrawingScreenState> = _uiState.asStateFlow()


    // Just for scaling and offsetting the canvas
    fun setCanvasScale(scale: Float) {
        _uiState.value = _uiState.value.copy(canvasScale = scale)
    }

    fun setCanvasOffset(offset: Offset) {
        _uiState.value = _uiState.value.copy(canvasOffset = offset)
    }

    fun selectColor(color: Color) {
        _uiState.value = _uiState.value.copy(selectedColor = color)
        ColorPalette.activeColor = color
    }

    fun selectTool(tool: Tool) {
        _uiState.value = _uiState.value.copy(selectedTool = tool)
    }

    fun drawPixel(x: Int, y: Int, color: Color = ColorPalette.activeColor) {

    }

    fun applyTool(tool: Tool, x: Int, y: Int, color: Color = ColorPalette.activeColor) {
        tool.apply(x, y)
    }
}