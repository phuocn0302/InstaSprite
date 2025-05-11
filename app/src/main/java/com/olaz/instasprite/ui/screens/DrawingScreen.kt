package com.olaz.instasprite.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.R
import com.olaz.instasprite.utils.UiUtils
import com.olaz.instasprite.utils.ColorPalette
import com.olaz.instasprite.ui.theme.DrawingScreenColor

@Composable
fun DrawingScreen() {
    UiUtils.SetStatusBarColor(DrawingScreenColor.PaletteBarColor)

    var selectedColor by remember { mutableStateOf(ColorPalette.Color1) }
    var selectedTool by remember { mutableStateOf("pencil") }

    val canvasSize = 16
    var canvasPixels by remember {
        mutableStateOf(List(canvasSize) { List(canvasSize) { DrawingScreenColor.DefaultCanvasColor } })
    }

    var scale by remember { mutableFloatStateOf(1f) }

    // For canvas section, somehow make the top and bottom bar always on top when zooming
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DrawingScreenColor.BackgroundColor)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top palette bar
            ColorPalette(
                onColorSelected = {
                    selectedColor = it
                    ColorPalette.activeColor = it
                },
                selectedColor = selectedColor,
                modifier = Modifier
                    .weight(0.3f)
                    .background(DrawingScreenColor.PaletteBarColor)
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            )

            // Canvas section
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                PixelCanvas(
                    pixels = canvasPixels,
                    onPixelClick = { row, col ->
                        val newCanvas = canvasPixels.toMutableList().apply {
                            val newRow = this[row].toMutableList().apply {
                                this[col] = selectedColor
                            }
                            this[row] = newRow
                        }
                        canvasPixels = newCanvas
                    },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(32.dp)
                        .aspectRatio(1f)
                        .fillMaxHeight(0.7f)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                        )
                )
            }

            Slider(
                value = scale,
                onValueChange = { newValue -> scale = newValue },
                valueRange = 0.5f..5f,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = DrawingScreenColor.SelectedToolColor,
                    inactiveTrackColor = DrawingScreenColor.SelectedToolColor

                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DrawingScreenColor.PaletteBarColor)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Bottom tool bar
            ToolSelector(
                selectedTool = selectedTool,
                onToolSelected = { selectedTool = it },
                modifier = Modifier
                    .background(DrawingScreenColor.PaletteBarColor)
                    .padding(horizontal = 5.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun ColorPalette(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
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
                var borderColor : Color = DrawingScreenColor.ColorItemBorder

                // Top row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (i in 0 until 8) {
                        val color : Color = ColorPalette.ColorsList[i]

                        borderColor = if (color == selectedColor) {
                            Color.White
                        } else {
                            DrawingScreenColor.ColorItemBorder
                        }

                        ColorItem(color = color,
                            onColorSelected = onColorSelected,
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
                        val color : Color = ColorPalette.ColorsList[i]

                        borderColor = if (color == selectedColor) {
                            Color.White
                        } else {
                            DrawingScreenColor.ColorItemBorder
                        }

                        ColorItem(color = color,
                            onColorSelected = onColorSelected,
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

@Composable
fun PixelCanvas(
    pixels: List<List<Color>>,
    onPixelClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val rows = pixels.size
    val cols = if (rows > 0) pixels[0].size else 0
    val checkerSize = 8

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .border(10.dp, DrawingScreenColor.CanvasBorderColor)
            .padding(10.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
        ) {
            for (row in 0 until rows) {
                Row(modifier = Modifier.weight(1f)) {
                    for (col in 0 until cols) {
                        val checkerBlockRow = row / checkerSize
                        val checkerBlockCol = col / checkerSize

                        val checkerBlockColor = if ((checkerBlockRow + checkerBlockCol) % 2 == 0) {
                            DrawingScreenColor.CheckerColor1
                        } else {
                            DrawingScreenColor.CheckerColor2
                        }

                        val displayColor = if (pixels[row][col] != DrawingScreenColor.DefaultCanvasColor) {
                            pixels[row][col]
                        } else {
                            checkerBlockColor
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(displayColor)
                                .clickable { onPixelClick(row, col) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ToolSelector(
    selectedTool: String,
    onToolSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ToolItem(
            iconResourceId = R.drawable.ic_pencil_tool,
            contentDescription = "Pencil Tool",
            selected = selectedTool == "pencil",
            onClick = { onToolSelected("pencil") }
        )

        ToolItem(
            iconResourceId = R.drawable.ic_eraser_tool,
            contentDescription = "Eraser Tool",
            selected = selectedTool == "eraser",
            onClick = { onToolSelected("eraser") }
        )

        ToolItem(
            iconResourceId = R.drawable.ic_fill_tool,
            contentDescription = "Fill Tool",
            selected = selectedTool == "fill",
            onClick = { onToolSelected("fill") }
        )

        ToolItem(
            iconResourceId = R.drawable.ic_eyedropper_tool,
            contentDescription = "Eyedropper Tool",
            selected = selectedTool == "eyedropper",
            onClick = { onToolSelected("eyedropper") }
        )
    }
}

@Composable
fun ToolItem(
    @DrawableRes iconResourceId: Int,
    contentDescription: String?,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(64.dp)
            .background(
                if (selected) DrawingScreenColor.SelectedToolColor else Color.Transparent,
                CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconResourceId),
            contentDescription = contentDescription,
            tint = Color.Unspecified,
            modifier = Modifier.size(32.dp)
        )
    }
}