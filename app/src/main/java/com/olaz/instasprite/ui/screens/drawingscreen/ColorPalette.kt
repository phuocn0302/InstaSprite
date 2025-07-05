package com.olaz.instasprite.ui.screens.drawingscreen

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.ui.screens.drawingscreen.dialog.ColorWheelDialog
import com.olaz.instasprite.ui.theme.DrawingScreenColor
import com.olaz.instasprite.utils.toHexString
import kotlinx.coroutines.launch

@Composable
fun ColorPalette(
    modifier: Modifier = Modifier,
    viewModel: DrawingScreenViewModel
) {

    val colorPalette by viewModel.colorPalette.collectAsState()
    val activeColor by viewModel.activeColor.collectAsState()
    val recentColors by viewModel.recentColors.collectAsState()

    val colorPaletteListState = rememberLazyListState()
    val recentColorsListState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    LaunchedEffect(activeColor) {
        if (activeColor in colorPalette) {
            val index = colorPalette.indexOf(activeColor)
            val visibleIndices = colorPaletteListState.layoutInfo.visibleItemsInfo.map { it.index }

            if (index !in visibleIndices) {
                coroutineScope.launch {
                    colorPaletteListState.scrollItemToCenter(
                        index = index,
                        itemSizeDp = 40.dp,
                        itemSpacingDp = 6.dp,
                        density = density
                    )
                }
            }
        }
    }

    LaunchedEffect(recentColors.firstOrNull()) {
        coroutineScope.launch {
            recentColorsListState.animateScrollToItem(0)
        }
    }

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
        modifier = modifier,
    ) {
        ColorPaletteContent(
            lazyListState = colorPaletteListState,
            colors = colorPalette,
            activeColor = activeColor,
            onColorSelected = viewModel::selectColor,
            isInteractive = true
        )

        // ColorPaletteContent already has 4.dp padding; add 2.dp for consistency
        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActiveColor(
                activeColor = activeColor,
                onClick = {
                    if (activeColor in colorPalette) {
                        val index = colorPalette.indexOf(activeColor)
                        coroutineScope.launch {
                            colorPaletteListState.scrollItemToCenter(
                                index = index,
                                itemSizeDp = 40.dp,
                                itemSpacingDp = 6.dp,
                                density = density
                            )
                        }
                    }
                },
                modifier = Modifier
                    .height(40.dp)
                    .width(86.dp) // Equal two color item in palette + spacing
            )

            IconButton(
                onClick = { showColorWheel = true },
                modifier = Modifier
                    .size(45.dp)
                    .padding(horizontal = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Show color wheel",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Recent Colors section
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .background(DrawingScreenColor.BackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                ColorPaletteContent(
                    lazyListState = recentColorsListState,
                    colors = recentColors.toList(),
                    onColorSelected = viewModel::selectColor,
                    colorItemModifier = Modifier.size(30.dp),
                    modifier = Modifier.padding(horizontal = 5.dp),
                    itemSpacingDp = 0.dp,
                    isInteractive = true
                )
            }

            // Opt button: show a TODO: dialog with canvas option
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(45.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Show opts",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
private fun ActiveColor(
    activeColor: Color,
    onClick: (() -> Unit),
    modifier: Modifier = Modifier
) {
    val textColor = if (activeColor.luminance() < 0.4f) Color.White else Color.Black

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(activeColor)
            .border(
                width = 5.dp,
                color = DrawingScreenColor.BackgroundColor
            )
            .clickable(
                onClick = onClick
            )
    ) {
        Text(
            text = activeColor.toHexString(),
            color = textColor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Composable
fun ColorPaletteContent(
    colors: List<Color>,
    lazyListState: LazyListState = rememberLazyListState(),
    activeColor: Color? = null,
    onColorSelected: ((Color) -> Unit)? = null,
    modifier: Modifier = Modifier,
    colorItemModifier: Modifier? = null,
    itemSpacingDp: Dp = 6.dp,
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
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy(itemSpacingDp),
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

private suspend fun LazyListState.scrollItemToCenter(
    index: Int,
    itemSizeDp: Dp,
    itemSpacingDp: Dp,
    density: Density
) {
    val itemSizeWithSpacing = itemSizeDp + itemSpacingDp
    val itemSizePx = with(density) { itemSizeWithSpacing.toPx().toInt() }

    val centerOffset = -((layoutInfo.viewportEndOffset - itemSizePx) / 2)
    animateScrollToItem(index, centerOffset)
}
