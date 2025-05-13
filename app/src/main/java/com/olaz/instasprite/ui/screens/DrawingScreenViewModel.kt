package com.olaz.instasprite.ui.screens

import androidx.compose.ui.geometry.Offset
import com.olaz.instasprite.utils.ColorPalette
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DrawingScreenState(
    val canvasOffset: Offset = Offset.Zero,
    val canvasScale: Float = 1f,
)

class DrawingScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        DrawingScreenState(
            canvasScale = 1f
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
}