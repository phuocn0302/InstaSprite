package com.olaz.instasprite.ui.screens.homescreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olaz.instasprite.DrawingActivity
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.model.ISpriteWithMetaData
import com.olaz.instasprite.data.repository.ISpriteDatabaseRepository
import com.olaz.instasprite.data.repository.SortSettingRepository
import com.olaz.instasprite.data.repository.StorageLocationRepository
import com.olaz.instasprite.domain.usecase.SaveFileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

data class HomeScreenState(
    val showCreateCanvasDialog: Boolean = false,
    val showSelectSortOptionDialog: Boolean = false,
    val showSearchBar: Boolean = false,
    val showImagePager: Boolean = false
)

class HomeScreenViewModel(
    private val spriteDatabaseRepository: ISpriteDatabaseRepository,
    private val sortSettingRepository: SortSettingRepository,
    private val storageLocationRepository: StorageLocationRepository,
) : ViewModel() {
    private val saveFileUseCase = SaveFileUseCase()

    private val _uiState = MutableStateFlow(
        HomeScreenState()
    )
    val uiState: StateFlow<HomeScreenState> = _uiState.asStateFlow()

    val sprites: StateFlow<List<ISpriteWithMetaData>> =
        spriteDatabaseRepository.getAllSpritesWithMeta()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    var searchQuery by mutableStateOf("")
        private set

    private val _lastSavedLocation = MutableStateFlow<Uri?>(null)

    var lastEditedSpriteId by mutableStateOf<String?>(null)
    var spriteListOrder by mutableStateOf(SpriteListOrder.LastModifiedDesc)
    var spriteList by mutableStateOf(emptyList<ISpriteWithMetaData>())
    var currentSelectedSpriteIndex by mutableIntStateOf(0)
    var lastSpriteSeenInPager by mutableStateOf<ISpriteData?>(null)

    init {
        viewModelScope.launch {
            sortSettingRepository.getLastSortSetting()?.let {
                spriteListOrder = it
            }
        }
    }

    fun toggleCreateCanvasDialog() {
        _uiState.value = _uiState.value.copy(
            showCreateCanvasDialog = !_uiState.value.showCreateCanvasDialog
        )
    }

    fun toggleSelectSortOptionDialog() {
        _uiState.value = _uiState.value.copy(
            showSelectSortOptionDialog = !_uiState.value.showSelectSortOptionDialog
        )
    }

    fun toggleSearchBar() {
        _uiState.value = _uiState.value.copy(
            showSearchBar = !_uiState.value.showSearchBar
        )
    }

    fun toggleImagePager(selectedSprite: ISpriteData?) {
        _uiState.value = _uiState.value.copy(
            showImagePager = !_uiState.value.showImagePager
        )
        currentSelectedSpriteIndex = spriteList.map { it.sprite }.indexOf(selectedSprite)
    }

    suspend fun getLastSavedLocation(): Uri? {
        _lastSavedLocation.value = storageLocationRepository.getLastSavedLocation()
        return _lastSavedLocation.value
    }

    fun setLastSavedLocation(uri: Uri) {
        _lastSavedLocation.value = uri
        viewModelScope.launch {
            storageLocationRepository.setLastSavedLocation(uri)
        }
    }

    fun saveImage(
        context: Context,
        ispriteData: ISpriteData,
        folderUri: Uri,
        fileName: String,
        scalePercent: Int = 100
    ): Boolean {
        val result = saveFileUseCase.saveImageFile(
            context,
            ispriteData,
            scalePercent,
            folderUri,
            fileName
        )

        result.fold(
            onSuccess = { return true },
            onFailure = { exception ->
                Log.e("SaveFile", "Failed to save file", exception)
                return false
            }
        )
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

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }
}