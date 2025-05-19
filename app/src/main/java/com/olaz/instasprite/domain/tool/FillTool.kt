package com.olaz.instasprite.domain.tool

import androidx.compose.ui.graphics.Color
import com.olaz.instasprite.R
import com.olaz.instasprite.data.model.PixelCanvasModel

object FillTool : Tool {
    override val icon: Int = R.drawable.ic_fill_tool
    override val name: String = "Fill"
    override val description: String = "Fill canvas section with the selected color"

    // DFS
    override fun apply(canvas: PixelCanvasModel, row: Int, col: Int, color: Color) {
        val targetColor = canvas.getPixel(row, col)
        if (targetColor == color) return

        // we'll push (row, col) pairs onto this stack
        val stack = ArrayDeque<Pair<Int, Int>>().apply {
            add(row to col)
        }

        while (stack.isNotEmpty()) {
            val (r, c) = stack.removeLast()

            if (r < 0 || r >= canvas.height || c < 0 || c >= canvas.width) continue

            // only fill pixels matching the original color
            if (canvas.getPixel(r, c) != targetColor) continue

            canvas.setPixel(r, c, color)

            // push 4‑neighbors
            stack.add(r + 1 to c)
            stack.add(r - 1 to c)
            stack.add(r to c + 1)
            stack.add(r to c - 1)
        }
    }
}