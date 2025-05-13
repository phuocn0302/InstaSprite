package com.olaz.instasprite.ui.canvas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.ui.theme.DrawingScreenColor

@Composable
fun PixelCanvas(
    modifier: Modifier = Modifier,
    viewModel: PixelCanvasViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val pixels = uiState.canvasPixels

    val rows = pixels.size
    val cols = if (rows > 0) pixels[0].size else 0
    val checkerSize = 8

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .border(10.dp, DrawingScreenColor.CanvasBorderColor)
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            for (row in 0 until rows) {
                Row(modifier = Modifier.weight(1f)) {
                    for (col in 0 until cols) {
                        val checkerBlockRow = row / checkerSize
                        val checkerBlockCol = col / checkerSize

                        val checkerBlockColor =
                            if ((checkerBlockRow + checkerBlockCol) % 2 == 0) {
                                DrawingScreenColor.CheckerColor1
                            } else {
                                DrawingScreenColor.CheckerColor2
                            }

                        val displayColor =
                            if (pixels[row][col] != DrawingScreenColor.DefaultCanvasColor) {
                                pixels[row][col]
                            } else {
                                checkerBlockColor
                            }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(displayColor)
                                .clickable { viewModel.drawPixel(row, col)}
                        )
                    }
                }
            }
        }
    }
}

