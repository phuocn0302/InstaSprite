package com.olaz.instasprite.domain.tool

import androidx.compose.ui.graphics.Color
import com.olaz.instasprite.R
import com.olaz.instasprite.domain.usecase.PixelCanvasUseCase

object FillTool : Tool {
    override val icon: Int = R.drawable.ic_fill_tool
    override val name: String = "Fill"
    override val description: String = "Fill canvas section with the selected color"

    // Scanline fill algorithm
    override fun apply(canvas: PixelCanvasUseCase, row: Int, col: Int, color: Color) {
        val width = canvas.getCanvasWidth()
        val height = canvas.getCanvasHeight()

        val targetColor = canvas.getPixel(row, col)
        if (targetColor == color) return

        val visited = Array(height) { BooleanArray(width) }
        val stack = ArrayDeque<Pair<Int, Int>>()
        stack.add(row to col)

        while (stack.isNotEmpty()) {
            val (startRow, startCol) = stack.removeLast()
            var x = startCol

            // Move left from the start point
            while (x >= 0 && canvas.getPixel(startRow, x) == targetColor && !visited[startRow][x]) {
                x--
            }
            val leftBound = x + 1

            // Move right from the start point
            x = startCol
            while (x < width && canvas.getPixel(startRow, x) == targetColor && !visited[startRow][x]) {
                x++
            }
            val rightBound = x - 1

            // Fill the scanline from leftBound to rightBound
            for (i in leftBound..rightBound) {
                canvas.setPixel(startRow, i, color)
                visited[startRow][i] = true

                // Check pixel above
                if (startRow > 0 &&
                    canvas.getPixel(startRow - 1, i) == targetColor &&
                    !visited[startRow - 1][i]
                ) {
                    stack.add(startRow - 1 to i)
                }

                // Check pixel below
                if (startRow < height - 1 &&
                    canvas.getPixel(startRow + 1, i) == targetColor &&
                    !visited[startRow + 1][i]
                ) {
                    stack.add(startRow + 1 to i)
                }
            }
        }
    }
}