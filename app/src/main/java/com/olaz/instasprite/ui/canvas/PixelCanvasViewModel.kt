package com.olaz.instasprite.ui.canvas

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.olaz.instasprite.domain.tool.Tool
import com.olaz.instasprite.utils.ColorPalette
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PixelCanvasState(
    val canvasSize: Int = 16,
    val canvasPixels: List<List<Color>>,
)

class PixelCanvasViewModel(
    canvasSize: Int,
    canvasPixels: List<List<Color>>,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        PixelCanvasState(
            canvasSize = canvasSize,
            canvasPixels = canvasPixels,
        )
    )
    val uiState: StateFlow<PixelCanvasState> = _uiState.asStateFlow()

    fun drawPixel(x: Int, y: Int, color: Color = ColorPalette.activeColor) {

    }

    fun applyTool(tool: Tool, x: Int, y: Int, color: Color = ColorPalette.activeColor) {
        tool.apply(x, y)
    }

}