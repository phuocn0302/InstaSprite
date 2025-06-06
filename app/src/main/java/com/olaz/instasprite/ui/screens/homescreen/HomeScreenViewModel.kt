package com.olaz.instasprite.ui.screens.homescreen

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olaz.instasprite.DrawingActivity
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.model.ISpriteWithMetaData
import com.olaz.instasprite.data.repository.ISpriteDatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class HomeScreenViewModel(
    private val spriteDatabaseRepository: ISpriteDatabaseRepository
) : ViewModel() {

    val sprites: StateFlow<List<ISpriteWithMetaData>> =
        spriteDatabaseRepository.getAllSpritesWithMeta()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )


    fun deleteSpriteById(spriteId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                spriteDatabaseRepository.deleteSpriteById(spriteId)
            } catch (e: Exception) {
                Log.e("HomeScreenViewModel", "Error deleting sprite", e)
            }
        }
    }

    fun deleteSpriteByIdDelay(spriteId: String, duration: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(duration)
            deleteSpriteById(spriteId)
        }
    }

    fun openDrawingActivity(context: Context, sprite: ISpriteData) {
        val intent = Intent(context, DrawingActivity::class.java)
        intent.putExtra(DrawingActivity.EXTRA_SPRITE_ID, sprite.id)
        context.startActivity(intent)
    }
}