package com.olaz.instasprite.ui.screens.drawingscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.ui.theme.DrawingScreenColor


@Composable
fun ColorPalette(
    modifier: Modifier = Modifier,
    viewModel: DrawingScreenViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val colorPalette by viewModel.colorPalette.collectAsState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .padding(vertical = 4.dp, horizontal = 4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(colorPalette) { color ->
                val borderColor = if (color == uiState.selectedColor) {
                    Color.White
                } else {
                    DrawingScreenColor.ColorItemBorder
                }
                ColorItem(
                    color = color,
                    onColorSelected = viewModel::selectColor,
                    modifier = Modifier
                        .size(32.dp)
                        .border(3.dp, borderColor, RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

@Composable
fun ColorItem(
    color: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .border(1.dp, DrawingScreenColor.ColorItemBorderOverlay, RoundedCornerShape(4.dp))
            .clickable { onColorSelected(color) }
    )
}