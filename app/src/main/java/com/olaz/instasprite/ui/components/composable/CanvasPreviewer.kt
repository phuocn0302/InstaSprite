package com.olaz.instasprite.ui.components.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.domain.export.ImageExporter
import com.olaz.instasprite.ui.theme.DrawingScreenColor

@Composable
fun CanvasPreviewer(
    spriteData: ISpriteData,
    modifier: Modifier = Modifier,
    showBorder: Boolean = false,
) {
    if (spriteData.width == 0 || spriteData.height == 0) return

    val spriteAspectRatio = spriteData.width.toFloat() / spriteData.height.toFloat()

    val bitmapImage by remember(spriteData) {
        mutableStateOf(
            ImageExporter.convertToBitmap(
                spriteData.pixelsData.map { Color(it) },
                spriteData.width,
                spriteData.height,
            )?.asImageBitmap()
        )
    }
    if (bitmapImage == null) return

    var modifier = modifier

    if (showBorder) {
        modifier = modifier.border(5.dp, DrawingScreenColor.PaletteBackgroundColor)
    }

    Image(
        bitmap = bitmapImage!!,
        contentDescription = "Sprite Preview",
        contentScale = ContentScale.FillBounds,
        filterQuality = FilterQuality.None,
        modifier = modifier
            .aspectRatio(spriteAspectRatio)
    )
}