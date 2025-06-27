package com.olaz.instasprite.ui.screens.drawingscreen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.olaz.instasprite.data.model.PixelCanvasModel
import com.olaz.instasprite.domain.canvashistory.CanvasHistoryManager
import com.olaz.instasprite.domain.export.ImageExporter
import com.olaz.instasprite.domain.tool.EyedropperTool
import com.olaz.instasprite.domain.tool.PencilTool
import com.olaz.instasprite.domain.tool.Tool
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.olaz.instasprite.data.model.ColorPaletteModel
import com.olaz.instasprite.data.repository.ColorPaletteRepository
import com.olaz.instasprite.data.repository.ColorPaletteRepository.Companion.createDefaultPalette
import com.olaz.instasprite.data.repository.StorageLocationRepository
import kotlinx.coroutines.launch

data class DrawingScreenState(
    val colorPalette: List<Color>,
    val selectedColor: Color,
    val selectedTool: Tool,

    val canvasWidth: Int,
    val canvasHeight: Int,

    val canvasOffset: Offset,
    val canvasScale: Float,
)

class DrawingScreenViewModel(
    canvasWidth: Int,
    canvasHeight: Int,
    private val storageLocationRepository: StorageLocationRepository,
    private val colorPaletteRepository: ColorPaletteRepository,
    context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        DrawingScreenState(
            colorPalette = createDefaultPalette(context).colors,
            selectedColor = createDefaultPalette(context).activeColor,

            selectedTool = PencilTool,

            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight,

            canvasScale = 1f,
            canvasOffset = Offset.Zero
        )
    )
    val uiState: StateFlow<DrawingScreenState> = _uiState.asStateFlow()

    val canvasModel = PixelCanvasModel(canvasWidth, canvasHeight)
    val pixelChangeTrigger = canvasModel.pixelChanged

    val canvasHistoryManager = CanvasHistoryManager<List<Color>>()

    private val exporter = ImageExporter()

    private val _lastSavedLocation = MutableStateFlow<Uri?>(null)
    val lastSavedLocation: StateFlow<Uri?> = _lastSavedLocation.asStateFlow()


    // Just for scaling and offsetting the canvas
    fun setCanvasScale(scale: Float) {
        _uiState.value = _uiState.value.copy(canvasScale = scale)
    }

    fun setCanvasOffset(offset: Offset) {
        _uiState.value = _uiState.value.copy(canvasOffset = offset)
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

    fun applyTool(canvasModel: PixelCanvasModel ,tool: Tool, x: Int, y: Int, color: Color = _uiState.value.selectedColor) {
        Log.d("DrawingScreenViewModel", "Applying tool: ${tool.name} at x=$x, y=$y with color=$color")

        tool.apply(canvasModel, x, y, color)

        if (tool is EyedropperTool) {
            selectColor(color)
        }
    }

    fun undo() {
        canvasHistoryManager.undo()?.let {
            canvasModel.setAllPixels(it)
        }
    }

    fun redo() {
        canvasHistoryManager.redo()?.let {
            canvasModel.setAllPixels(it)
        }
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

    fun saveFile(
        context: Context,
        folderUri: Uri,
        fileName: String,
        scalePercent: Int = 100
    ): Boolean {
        return exporter.saveToFolder(canvasModel, context, folderUri, fileName, scalePercent)
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