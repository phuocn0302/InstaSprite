package com.olaz.instasprite.ui.screens.drawingscreen

import android.util.Log
import android.widget.Toast
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.olaz.instasprite.data.model.PixelCanvasModel
import com.olaz.instasprite.domain.canvashistory.CanvasHistoryManager
import com.olaz.instasprite.domain.tool.EyedropperTool
import com.olaz.instasprite.domain.tool.MoveTool
import com.olaz.instasprite.domain.tool.PencilTool
import com.olaz.instasprite.domain.tool.Tool
import com.olaz.instasprite.utils.ColorPalette
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DrawingScreenState(
    val selectedColor: Color,
    val selectedTool: Tool,

    val canvasWidth: Int,
    val canvasHeight: Int,

    val canvasOffset: Offset,
    val canvasScale: Float,
)

class DrawingScreenViewModel(
    canvasWidth: Int,
    canvasHeight: Int
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        DrawingScreenState(
            selectedColor = Color(0xFF040519),
            selectedTool = PencilTool,

            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight,

            canvasScale = 1f,
            canvasOffset = Offset.Zero
        )
    )
    val uiState: StateFlow<DrawingScreenState> = _uiState.asStateFlow()

    val canvasModel = PixelCanvasModel(canvasWidth, canvasHeight)
    val pixelChangeTrigger = canvasModel.pixelChanged

    val canvasHistoryManager = CanvasHistoryManager<List<Color>>()

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

    fun applyTool(canvasModel: PixelCanvasModel ,tool: Tool, x: Int, y: Int, color: Color = ColorPalette.activeColor) {
        Log.d("DrawingScreenViewModel", "Applying tool: ${tool.name} at x=$x, y=$y with color=$color")

        tool.apply(canvasModel, x, y, color)

        if (tool is EyedropperTool) {
            selectColor(ColorPalette.activeColor)
        }
    }

    fun undo() {
        canvasHistoryManager.undo()?.let {
            canvasModel.setAllPixels(it)
        }
    }

    fun redo() {
        canvasHistoryManager.redo()?.let {
            canvasModel.setAllPixels(it)
        }
    }
}