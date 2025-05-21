package com.olaz.instasprite.domain.tool

import androidx.compose.ui.graphics.Color
import com.olaz.instasprite.R
import com.olaz.instasprite.data.model.PixelCanvasModel

object MoveTool : Tool {
    override val icon: Int = R.drawable.ic_move_tool
    override val name: String = "Move"
    override val description: String = "Move the canvas"

    override fun apply(canvas: PixelCanvasModel, row: Int, col: Int, color: Color) {
        // Just declare for implement Tool interface, logic is in viewModel
    }
}