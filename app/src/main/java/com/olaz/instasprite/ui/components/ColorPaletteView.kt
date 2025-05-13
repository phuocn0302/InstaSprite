package com.olaz.instasprite.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.olaz.instasprite.utils.ColorPalette

@Composable
fun ColorPalette(
    modifier: Modifier = Modifier,
    viewModel: ColorPaletteViewModel = ColorPaletteViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .background(DrawingScreenColor.PaletteBackgroundColor)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                var borderColor: Color = DrawingScreenColor.ColorItemBorder

                // Top row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (i in 0 until 8) {
                        val color: Color = ColorPalette.ColorsList[i]

                        borderColor = if (color == uiState.selectedColor) {
                            Color.White
                        } else {
                            DrawingScreenColor.ColorItemBorder
                        }

                        ColorItem(
                            color = color,
                            onColorSelected = viewModel::selectColor,
                            modifier = Modifier
                                .size(36.dp)
                                .border(5.dp, borderColor, RoundedCornerShape(4.dp))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (i in 8 until 16) {
                        val color: Color = ColorPalette.ColorsList[i]

                        borderColor = if (color == uiState.selectedColor) {
                            Color.White
                        } else {
                            DrawingScreenColor.ColorItemBorder
                        }

                        ColorItem(
                            color = color,
                            onColorSelected = viewModel::selectColor,
                            modifier = Modifier
                                .size(36.dp)
                                .border(5.dp, borderColor, RoundedCornerShape(4.dp))
                        )
                    }
                }
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
            .size(32.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .border(1.dp, DrawingScreenColor.ColorItemBorderOverlay, RoundedCornerShape(4.dp))
            .clickable { onColorSelected(color) }
    )
}