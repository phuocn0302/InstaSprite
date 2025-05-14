package com.olaz.instasprite.ui.screens.drawingscreen

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.data.model.PixelCanvas
import com.olaz.instasprite.ui.theme.DrawingScreenColor

@Composable
fun PixelCanvas(
    canvas: PixelCanvas,
    modifier: Modifier = Modifier,
    viewModel: DrawingScreenViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

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
            for (row in 0 until canvas.height) {
                Row(modifier = Modifier.weight(1f)) {
                    for (col in 0 until canvas.width) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(color = Color.Blue)
                                .clickable { viewModel.drawPixel(row, col)}
                        )
                    }
                }
            }
        }
    }
}

