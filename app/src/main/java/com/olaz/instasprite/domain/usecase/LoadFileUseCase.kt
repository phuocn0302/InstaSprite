package com.olaz.instasprite.domain.usecase

import android.content.Context
import android.net.Uri
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.repository.LoadFileRepository

class LoadFileUseCase {
    fun loadFile(context: Context, fileUri: Uri): ISpriteData? {
        return LoadFileRepository.loadFile(context, fileUri)
    }
}
