package com.olaz.instasprite.ui.screens.drawingscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.ui.screens.drawingscreen.dialog.ColorWheelDialog
import com.olaz.instasprite.ui.theme.DrawingScreenColor

@Composable
fun ColorPalette(
    modifier: Modifier = Modifier,
    viewModel: DrawingScreenViewModel
) {

    val colorPalette by viewModel.colorPalette.collectAsState()
    val activeColor by viewModel.activeColor.collectAsState()

    var showColorWheel by remember { mutableStateOf(false) }
    if (showColorWheel) {
        ColorWheelDialog(
            onDismiss = { showColorWheel = false },
            initialColor = activeColor,
            onColorSelected = { color ->
                viewModel.selectColor(color)
                showColorWheel = false
            },
            viewModel = viewModel
        )
    }

    Column(
        modifier = modifier
    ) {
        ColorPaletteContent(
            colors = colorPalette,
            activeColor = activeColor,
            onColorSelected = viewModel::selectColor,
            isInteractive = true
        )

        IconButton(
            onClick = { showColorWheel = true }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Show color wheel",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }

}

@Composable
fun ColorPaletteContent(
    colors: List<Color>,
    activeColor: Color? = null,
    onColorSelected: ((Color) -> Unit)? = null,
    modifier: Modifier = Modifier,
    colorItemModifier: Modifier? = null,
    isInteractive: Boolean = false,
    showPreviewLabel: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
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
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            items(colors) { color ->
                val borderColor = if (isInteractive && color == activeColor) {
                    Color.White
                } else {
                    DrawingScreenColor.BackgroundColor
                }

                val modifier = colorItemModifier ?: Modifier
                    .size(40.dp)
                    .border(
                        width = 5.dp,
                        color = borderColor
                    )

                ColorItem(
                    color = color,
                    onColorSelected = if (isInteractive) onColorSelected else null,
                    modifier = modifier
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
            .background(color)
            .then(
                if (onColorSelected != null) {
                    Modifier.clickable { onColorSelected(color) }
                } else {
                    Modifier
                }
            )
    )
}