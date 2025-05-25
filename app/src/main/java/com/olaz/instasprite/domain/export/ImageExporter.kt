package com.olaz.instasprite.domain.export

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import androidx.documentfile.provider.DocumentFile
import com.olaz.instasprite.data.model.PixelCanvasModel
import java.io.IOException

class ImageExporter {
    fun saveToFolder(
        canvasModel: PixelCanvasModel,
        context: Context,
        folderUri: Uri,
        fileName: String,
        scalePercent: Int = 100
    ): Boolean {
        val formatInfo = getFormatFromExtension(fileName) ?: return false
        val (compressFormat, mimeType) = formatInfo

        val folder = DocumentFile.fromTreeUri(context, folderUri)
        val file = folder?.createFile(mimeType, fileName) ?: return false
        val outputStream = context.contentResolver.openOutputStream(file.uri) ?: return false

        val pixelsData = canvasModel.getAllPixels()
        val canvasWidth = canvasModel.width
        val canvasHeight = canvasModel.height

        if (pixelsData.isEmpty()) {
            outputStream.close()
            return false
        }

        val argbPixels = IntArray(canvasWidth * canvasHeight) { i ->
            pixelsData[i].toArgb()
        }

        val bitmap = createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(argbPixels, 0, canvasWidth, 0, 0, canvasWidth, canvasHeight)

        val scale = scalePercent / 100f
        val scaledWidth = (canvasWidth * scale).toInt().coerceAtLeast(1)
        val scaledHeight = (canvasHeight * scale).toInt().coerceAtLeast(1)

        val scaledBitmap = bitmap.scale(scaledWidth, scaledHeight, false)

        return try {
            scaledBitmap.compress(compressFormat, 100, outputStream)
            outputStream.flush()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            outputStream.close()
        }
    }

    private fun getFormatFromExtension(fileName: String): Pair<Bitmap.CompressFormat, String>? {
        return when (fileName.substringAfterLast('.', "").lowercase()) {
            "png" -> Bitmap.CompressFormat.PNG to "image/png"
            "jpg", "jpeg" -> Bitmap.CompressFormat.JPEG to "image/jpeg"
            "webp" -> Bitmap.CompressFormat.WEBP to "image/webp"
            else -> null
        }
    }
}
