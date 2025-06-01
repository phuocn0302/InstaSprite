package com.olaz.instasprite.ui.components.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.ui.theme.DrawingScreenColor
import kotlin.math.min

@Composable
fun CanvasPreviewer(
    spriteData: ISpriteData,
    maxWidth: Dp = 300.dp,
    maxHeight: Dp = 300.dp,
    showBorder: Boolean = true
) {
    if (spriteData.width == 0 || spriteData.height == 0) return

    val spriteAspectRatio = spriteData.width.toFloat() / spriteData.height.toFloat()

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = maxHeight)
            .widthIn(max = maxWidth)
            .aspectRatio(spriteAspectRatio, matchHeightConstraintsFirst = true)
            .border(
                if (showBorder) 5.dp else 0.dp,
                DrawingScreenColor.PaletteBackgroundColor
            )
            .clipToBounds()
    ) {
        val pixelSize = min(
            size.width / spriteData.width,
            size.height / spriteData.height
        )

        spriteData.pixelsData.forEachIndexed { index, colorInt ->
            val x = index % spriteData.width
            val y = index / spriteData.width

            val color = Color(colorInt)

            drawRect(
                color = color,
                topLeft = Offset(
                    x = x * pixelSize,
                    y = y * pixelSize
                ),
                size = Size(pixelSize, pixelSize)
            )
        }
    }
}