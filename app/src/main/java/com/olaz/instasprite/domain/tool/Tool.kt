package com.olaz.instasprite.domain.tool

import androidx.compose.ui.graphics.Color
import com.olaz.instasprite.data.model.PixelCanvasModel

interface Tool {
    val icon: Int
    val name: String
    val description: String
    fun apply(canvas: PixelCanvasModel, row: Int, col: Int, color: Color) : Unit
}