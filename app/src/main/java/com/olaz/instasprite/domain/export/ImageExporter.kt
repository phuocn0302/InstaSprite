package com.olaz.instasprite.domain.export

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale

object ImageExporter {
    fun convertToBitmap(
        pixelsData: List<Color>,
        canvasWidth: Int,
        canvasHeight: Int,
        scalePercent: Int = 100
    ): Bitmap? {
        if (pixelsData.isEmpty()) return null

        val argbPixels = IntArray(canvasWidth * canvasHeight) { i ->
            pixelsData[i].toArgb()
        }

        val bitmap = createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(argbPixels, 0, canvasWidth, 0, 0, canvasWidth, canvasHeight)

        val scale = scalePercent / 100f
        val scaledWidth = (canvasWidth * scale).toInt().coerceAtLeast(1)
        val scaledHeight = (canvasHeight * scale).toInt().coerceAtLeast(1)

        return bitmap.scale(scaledWidth, scaledHeight, false)
    }
}
