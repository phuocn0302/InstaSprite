package com.olaz.instasprite.ui.screens.homescreen

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olaz.instasprite.DrawingActivity
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.model.ISpriteWithMetaData
import com.olaz.instasprite.data.repository.ISpriteDatabaseRepository
import com.olaz.instasprite.data.repository.SortSettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class SpriteListOrder {
    Name,
    NameDesc,
    DateCreated,
    DateCreatedDesc,
    LastModified,
    LastModifiedDesc
}

class HomeScreenViewModel(
    private val spriteDatabaseRepository: ISpriteDatabaseRepository,
    private val sortSettingRepository: SortSettingRepository
) : ViewModel() {

    val sprites: StateFlow<List<ISpriteWithMetaData>> =
        spriteDatabaseRepository.getAllSpritesWithMeta()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    var lastEditedSpriteId by mutableStateOf<String?>(null)
    var spriteListOrder by mutableStateOf(SpriteListOrder.LastModifiedDesc)

    init {
        viewModelScope.launch {
            sortSettingRepository.getLastSortSetting()?.let {
                spriteListOrder = it
            }
        }
    }

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
        lastEditedSpriteId = sprite.id
        val intent = Intent(context, DrawingActivity::class.java)
        intent.putExtra(DrawingActivity.EXTRA_SPRITE_ID, sprite.id)
        context.startActivity(intent)
    }

    fun renameSprite(spriteId: String, newName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            spriteDatabaseRepository.changeName(spriteId, newName)
        }
    }

    fun saveSortSetting(spriteListOrder: SpriteListOrder) {
        viewModelScope.launch {
            sortSettingRepository.setLastSortSetting(spriteListOrder)
        }
    }
}