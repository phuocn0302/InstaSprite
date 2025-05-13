package com.olaz.instasprite.ui.components

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.olaz.instasprite.utils.ColorPalette
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ColorPaletteState(
    val selectedColor: Color = ColorPalette.Color1,
)

class ColorPaletteViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        ColorPaletteState(
            selectedColor = ColorPalette.Color1
        )
    )
    val uiState: StateFlow<ColorPaletteState> = _uiState.asStateFlow()

    fun selectColor(color: Color) {
        _uiState.value = _uiState.value.copy(selectedColor = color)
        ColorPalette.activeColor = color
    }
}