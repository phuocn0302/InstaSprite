package com.olaz.instasprite.ui.screens.drawingscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.ui.theme.DrawingScreenColor

@Composable
fun ColorPalette(
    modifier: Modifier = Modifier,
    viewModel: DrawingScreenViewModel
) {
    val colorPaletteState by viewModel.uiState.collectAsState()
    
    ColorPaletteContent(
        colors = colorPaletteState.colorPalette,
        activeColor = colorPaletteState.selectedColor,
        onColorSelected = viewModel::selectColor,
        modifier = modifier,
        isInteractive = true
    )
}

@Composable
fun ColorPaletteContent(
    colors: List<Color>,
    activeColor: Color? = null,
    onColorSelected: ((Color) -> Unit)? = null,
    modifier: Modifier = Modifier,
    isInteractive: Boolean = false,
    showPreviewLabel: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .padding(vertical = 4.dp, horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(if (showPreviewLabel) 16.dp else 0.dp)
    ) {
        if (showPreviewLabel) {
            Text(
                text = "Preview",
                color = Color.White,
                fontSize = 14.sp
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp),
            modifier = if (!isInteractive) Modifier.height(40.dp) else Modifier
        ) {
            items(colors) { color ->
                val borderColor = if (isInteractive && color == activeColor) {
                    Color.White
                } else {
                    DrawingScreenColor.ColorItemBorder
                }
                
                ColorItem(
                    color = color,
                    onColorSelected = if (isInteractive) onColorSelected else null,
                    modifier = Modifier
                        .size(if (isInteractive) 32.dp else 40.dp)
                        .border(
                            width = if (isInteractive) 3.dp else 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(if (isInteractive) 4.dp else 8.dp)
                        )
                )
            }
        }
    }
}

@Composable
fun ColorItem(
    color: Color,
    onColorSelected: ((Color) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .border(1.dp, DrawingScreenColor.ColorItemBorderOverlay, RoundedCornerShape(4.dp))
            .then(
                if (onColorSelected != null) {
                    Modifier.clickable { onColorSelected(color) }
                } else {
                    Modifier
                }
            )
    )
}