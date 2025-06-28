package com.olaz.instasprite.ui.screens.drawingscreen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.repository.ColorPaletteRepository
import com.olaz.instasprite.data.repository.ColorPaletteRepository.Companion.createDefaultPalette
import com.olaz.instasprite.data.repository.ISpriteDatabaseRepository
import com.olaz.instasprite.data.repository.PixelCanvasRepository
import com.olaz.instasprite.data.repository.StorageLocationRepository
import com.olaz.instasprite.domain.canvashistory.CanvasHistoryManager
import com.olaz.instasprite.domain.tool.EyedropperTool
import com.olaz.instasprite.domain.tool.PencilTool
import com.olaz.instasprite.domain.tool.Tool
import com.olaz.instasprite.domain.usecase.LoadFileUseCase
import com.olaz.instasprite.domain.usecase.PixelCanvasUseCase
import com.olaz.instasprite.domain.usecase.SaveFileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DrawingScreenState(
    val colorPalette: List<Color>,
    val selectedColor: Color,
    val selectedTool: Tool,

    val canvasWidth: Int,
    val canvasHeight: Int,
)

class DrawingScreenViewModel(
    private val spriteId: String,
    private val storageLocationRepository: StorageLocationRepository,
    private val pixelCanvasRepository: PixelCanvasRepository,
    private val spriteDataRepository: ISpriteDatabaseRepository,
    private val colorPaletteRepository: ColorPaletteRepository,
    context: Context
) : ViewModel() {
    private val canvasHistoryManager = CanvasHistoryManager<List<Color>>()
    private val saveFileUseCase = SaveFileUseCase()
    private val loadFileUseCase = LoadFileUseCase()
    private val pixelCanvasUseCase = PixelCanvasUseCase(repo = pixelCanvasRepository)

    private val _uiState = MutableStateFlow(
        DrawingScreenState(
            colorPalette = createDefaultPalette(context).colors,
            selectedColor = createDefaultPalette(context).activeColor,

            selectedTool = PencilTool,

            canvasWidth = pixelCanvasUseCase.getCanvasWidth(),
            canvasHeight = pixelCanvasUseCase.getCanvasHeight(),
        )
    )
    val uiState: StateFlow<DrawingScreenState> = _uiState.asStateFlow()
    val pixelChangeTrigger = pixelCanvasUseCase.pixelChanged

    private val _lastSavedLocation = MutableStateFlow<Uri?>(null)
    val lastSavedLocation: StateFlow<Uri?> = _lastSavedLocation.asStateFlow()

    fun setCanvasSize(width: Int, height: Int) {
        pixelCanvasUseCase.setCanvas(width, height)
        _uiState.value = _uiState.value.copy(canvasWidth = width, canvasHeight = height)
    }

    fun selectColor(color: Color) {
        _uiState.value = _uiState.value.copy(selectedColor = color)

        val currentState = _uiState.value
        if (!currentState.colorPalette.contains(color)) {
            _uiState.value = currentState.copy(
                colorPalette = listOf(color) + currentState.colorPalette
            )
        }
    }

    fun selectTool(tool: Tool) {
        _uiState.value = _uiState.value.copy(selectedTool = tool)
    }

    fun applyTool(
        row: Int,
        col: Int,
    ) {
        val tool = _uiState.value.selectedTool
        val color = _uiState.value.selectedColor

        Log.d(
            "DrawingScreenViewModel",
            "Applying tool: ${tool.name} at row=$row, col=$col with color=$color"
        )

        tool.apply(pixelCanvasUseCase, row, col, color)

        if (tool is EyedropperTool) {
            selectColor(color)
        }
    }

    fun getPixelData(row: Int, col: Int): Color {
        return pixelCanvasUseCase.getPixel(row, col)
    }

    fun saveState() {
        canvasHistoryManager.saveState(pixelCanvasUseCase.getAllPixels())
    }

    fun undo() {
        canvasHistoryManager.undo()?.let {
            pixelCanvasUseCase.setAllPixels(it)
        }
    }

    fun redo() {
        canvasHistoryManager.redo()?.let {
            pixelCanvasUseCase.setAllPixels(it)
        }
    }

    fun rotate() {
        pixelCanvasUseCase.rotateCanvas(pixelCanvasUseCase.getAllPixels())
    }

    fun hFlip() {
        pixelCanvasUseCase.hFlipCanvas(pixelCanvasUseCase.getAllPixels())
    }

    fun vFlip() {
        pixelCanvasUseCase.vFlipCanvas(pixelCanvasUseCase.getAllPixels())
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

    suspend fun importFromFile(context: Context, uri: Uri): List<Color> {
        return colorPaletteRepository.importPaletteFromFile(context, uri)
            .map { it.colors }
            .getOrElse { emptyList() }
    }

    suspend fun importFromUrl(url: String): List<Color> {
        return colorPaletteRepository.fetchPaletteFromUrl(url)
            .map { it.colors }
            .getOrElse { emptyList() }
    }

    fun updateColorPalette(colors: List<Color>) {
        if (colors.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(
                colorPalette = colors,
                selectedColor = colors.first()
            )
        }
    }
}