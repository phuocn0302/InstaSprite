package com.olaz.instasprite.domain.usecase

import android.content.Context
import androidx.compose.ui.graphics.Color
import android.net.Uri
import androidx.compose.ui.graphics.toArgb
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.repository.SaveFileRepository
import com.olaz.instasprite.domain.export.ImageExporter

class SaveFileUseCase {
    fun saveImageFile(
        context: Context,
        isprite: ISpriteData,
        scalePercent: Int = 100,
        folderUri: Uri,
        fileName: String
    ): Result<Unit> {
        if (fileName.isBlank()) {
            return Result.failure(IllegalArgumentException("File name cannot be blank"))
        }

        val bitmap =
            ImageExporter.convertToBitmap(
                isprite.pixelsData.map { Color(it) },
                isprite.width,
                isprite.height,
                scalePercent
            )
        if (bitmap == null) {
            return Result.failure(IllegalArgumentException("Failed to convert image"))
        }

        val success = SaveFileRepository.saveFile(context, bitmap, folderUri, fileName)
        return if (success) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to save file"))
        }
    }

    fun saveISpriteFile(
        context: Context,
        isprite: ISpriteData,
        folderUri: Uri,
        fileName: String
    ): Result<Unit> {
        if (fileName.isBlank()) {
            return Result.failure(IllegalArgumentException("File name cannot be blank"))
        }

        val success = SaveFileRepository.saveFile(context, isprite, folderUri, fileName)
        return if (success) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to save file"))
        }
    }
}