package com.olaz.instasprite.ui.screens.drawingscreen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.repository.ColorPaletteRepository
import com.olaz.instasprite.data.repository.ISpriteDatabaseRepository
import com.olaz.instasprite.data.repository.LospecColorPaletteRepository
import com.olaz.instasprite.data.repository.PixelCanvasRepository
import com.olaz.instasprite.data.repository.StorageLocationRepository
import com.olaz.instasprite.domain.canvashistory.CanvasHistoryManager
import com.olaz.instasprite.domain.tool.PencilTool
import com.olaz.instasprite.domain.tool.Tool
import com.olaz.instasprite.domain.usecase.LoadFileUseCase
import com.olaz.instasprite.domain.usecase.PixelCanvasUseCase
import com.olaz.instasprite.domain.usecase.SaveFileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CanvasState(
    val width: Int,
    val height: Int,
    val pixels: List<Color>
)

data class DrawingScreenState(
    val selectedTool: Tool,
)

class DrawingScreenViewModel(
    private val spriteId: String,
    private val storageLocationRepository: StorageLocationRepository,
    private val pixelCanvasRepository: PixelCanvasRepository,
    private val spriteDataRepository: ISpriteDatabaseRepository,
    private val colorPaletteRepository: ColorPaletteRepository,
    private val lospecColorPaletteRepository: LospecColorPaletteRepository,
) : ViewModel() {
    private val canvasHistoryManager = CanvasHistoryManager<CanvasState>()
    private val saveFileUseCase = SaveFileUseCase()
    private val loadFileUseCase = LoadFileUseCase()
    private val pixelCanvasUseCase = PixelCanvasUseCase(
        pixelCanvasRepository = pixelCanvasRepository,
        colorPaletteRepository =  colorPaletteRepository
    )

    private val _uiState = MutableStateFlow(
        DrawingScreenState(
            selectedTool = PencilTool,
        )
    )
    val uiState: StateFlow<DrawingScreenState> = _uiState.asStateFlow()

    private val _canvasState = MutableStateFlow(
        CanvasState(
            width = pixelCanvasUseCase.getCanvasWidth(),
            height = pixelCanvasUseCase.getCanvasHeight(),
            pixels = pixelCanvasUseCase.getAllPixels()
        )
    )
    val canvasState: StateFlow<CanvasState> = _canvasState.asStateFlow()

    val pixelChangeTrigger = pixelCanvasUseCase.pixelChanged

    private val _lastSavedLocation = MutableStateFlow<Uri?>(null)
    val lastSavedLocation: StateFlow<Uri?> = _lastSavedLocation.asStateFlow()

    var colorPalette = colorPaletteRepository.colors
    var activeColor = colorPaletteRepository.activeColor
    var recentColors = colorPaletteRepository.recentColors

    fun setCanvasSize(width: Int, height: Int) {
        pixelCanvasUseCase.setCanvas(width, height)
        _canvasState.value = _canvasState.value.copy(width = width, height = height)
    }

    fun selectColor(color: Color) {
        colorPaletteRepository.setActiveColor(color)
    }

    fun selectTool(tool: Tool) {
        _uiState.value = _uiState.value.copy(selectedTool = tool)
    }

    fun applyTool(
        row: Int,
        col: Int,
    ) {
        val tool = _uiState.value.selectedTool
        val color = activeColor.value

        Log.d(
            "DrawingScreenViewModel",
            "Applying tool: ${tool.name} at row=$row, col=$col with color=${color.value}"
        )

        tool.apply(pixelCanvasUseCase, row, col, color)
    }

    fun getPixelData(row: Int, col: Int): Color {
        return pixelCanvasUseCase.getPixel(row, col)
    }

    fun saveState() {
        canvasHistoryManager.saveState(
            CanvasState(
                width = pixelCanvasUseCase.getCanvasWidth(),
                height = pixelCanvasUseCase.getCanvasHeight(),
                pixels = pixelCanvasUseCase.getAllPixels()
            )
        )
    }

    fun undo() {
        canvasHistoryManager.undo()?.let {
            pixelCanvasUseCase.setCanvas(it.width, it.height, it.pixels)
            _canvasState.value = _canvasState.value.copy(
                width = it.width,
                height = it.height,
            )
        }
    }

    fun redo() {
        canvasHistoryManager.redo()?.let {
            pixelCanvasUseCase.setCanvas(it.width, it.height, it.pixels)
            _canvasState.value = _canvasState.value.copy(
                width = it.width,
                height = it.height,
            )
        }
    }

    fun rotate() {
        pixelCanvasUseCase.rotateCanvas(pixelCanvasUseCase.getAllPixels())

        _canvasState.value = _canvasState.value.copy(
            width = pixelCanvasUseCase.getCanvasWidth(),
            height = pixelCanvasUseCase.getCanvasHeight()
        )

        saveState()
    }

    fun hFlip() {
        pixelCanvasUseCase.hFlipCanvas(pixelCanvasUseCase.getAllPixels())
        saveState()
    }

    fun vFlip() {
        pixelCanvasUseCase.vFlipCanvas(pixelCanvasUseCase.getAllPixels())
        saveState()
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
        folderUri: Uri,
        fileName: String,
        scalePercent: Int = 100
    ): Boolean {
        val result = saveFileUseCase.saveImageFile(
            context,
            pixelCanvasUseCase.getISpriteData(),
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

    fun saveISprite(
        context: Context,
        folderUri: Uri,
        fileName: String
    ): Boolean {
        val result = saveFileUseCase.saveISpriteFile(
            context,
            pixelCanvasUseCase.getISpriteData(),
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

    fun getISpriteDataFromFile(context: Context, fileUri: Uri): ISpriteData? {
        return loadFileUseCase.loadFile(context, fileUri)
    }

    fun loadISprite(spriteData: ISpriteData) {
        setCanvasSize(spriteData.width, spriteData.height)
        pixelCanvasUseCase.setCanvas(spriteData)
        canvasHistoryManager.reset()
        saveState()
    }

    fun saveToDB(spriteName: String? = null) {
        viewModelScope.launch {
            val spriteData = pixelCanvasUseCase.getISpriteData()
            spriteDataRepository.saveSprite(spriteData.copy(id = spriteId))
            spriteName?.let {
                spriteDataRepository.changeName(spriteId, it)
            }
        }
    }

    fun loadFromDB() {
        viewModelScope.launch {
            val spriteData = spriteDataRepository.loadSprite(spriteId)
            if (spriteData != null) {
                loadISprite(spriteData)
            }
        }
    }

    suspend fun importColorsFromFile(uri: Uri): List<Color> {
        return lospecColorPaletteRepository.importPaletteFromFile(uri)
            .map { it.colors }
            .getOrElse { emptyList() }
    }

    suspend fun importColorsFromLospecUrl(url: String): List<Color> {
        return lospecColorPaletteRepository.fetchPaletteFromUrl(url)
            .map { it.colors }
            .getOrElse { emptyList() }
    }

    fun updateColorPalette(colors: List<Color>) {
        if (colors.isNotEmpty()) {
            colorPaletteRepository.updatePalette(colors)
            colorPaletteRepository.setActiveColor(colors.first())
        }
    }
}