package com.olaz.instasprite.ui.screens.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.model.ISpriteWithMetaData
import com.olaz.instasprite.data.repository.ISpriteDatabaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val spriteDatabaseRepository: ISpriteDatabaseRepository
) : ViewModel() {

    init {
        loadSprites()
    }

    private val _sprites = MutableStateFlow<List<ISpriteData>>(emptyList())
    val sprites: StateFlow<List<ISpriteWithMetaData>> =
        spriteDatabaseRepository.getAllSpritesWithMeta()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    private fun loadSprites() {
        viewModelScope.launch {
            val spriteList = spriteDatabaseRepository.getSpriteList()
            _sprites.value = spriteList.first
        }
    }
}
