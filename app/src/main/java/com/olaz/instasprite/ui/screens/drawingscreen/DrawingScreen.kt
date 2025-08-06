package com.olaz.instasprite.ui.screens.drawingscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.data.database.AppDatabase
import com.olaz.instasprite.data.model.PixelCanvasModel
import com.olaz.instasprite.data.repository.ColorPaletteRepository
import com.olaz.instasprite.data.repository.ISpriteDatabaseRepository
import com.olaz.instasprite.data.repository.LospecColorPaletteRepository
import com.olaz.instasprite.data.repository.PixelCanvasRepository
import com.olaz.instasprite.data.repository.StorageLocationRepository
import com.olaz.instasprite.domain.tool.EraserTool
import com.olaz.instasprite.domain.tool.PencilTool
import com.olaz.instasprite.ui.theme.CatppuccinUI
import com.olaz.instasprite.utils.UiUtils
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.ZoomState
import net.engawapg.lib.zoomable.zoomable

@SuppressLint("DefaultLocale", "ConfigurationScreenWidthHeight")
@Composable
fun DrawingScreen(viewModel: DrawingScreenViewModel) {
    UiUtils.SetStatusBarColor(CatppuccinUI.BackgroundColor)
    UiUtils.SetNavigationBarColor(CatppuccinUI.BackgroundColor)

    val viewModel = viewModel
    val canvasState by viewModel.canvasState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val maxScale by remember(canvasState.width, canvasState.height) {
        derivedStateOf {
            val canvasSize = maxOf(canvasState.width, canvasState.height).toFloat()
            canvasSize.div(8f).coerceAtLeast(2f).coerceAtMost(100f)
        }
    }

    val canvasZoomState = remember(maxScale) {
        ZoomState(maxScale = maxScale)
    }

    val layoutSize = remember { mutableStateOf(IntSize.Zero) }

    val coroutineScope = rememberCoroutineScope()

    var toolSizeValue by remember { mutableFloatStateOf(1f) } // Default brush size

    Scaffold(
        topBar = {
            Column {
                Box() {
                    ColorPalette(
                        modifier = Modifier
                            .background(CatppuccinUI.BackgroundColor)
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        viewModel = viewModel
                    )
                }
                Row() {
                    if (uiState.selectedTool in listOf(PencilTool, EraserTool)) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .height(35.dp)
                                .background(
                                    color = CatppuccinUI.BackgroundColor,
                                )

                        ) {
                            Text(
                                text = "${toolSizeValue.toInt()}px",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .width(86.dp)
                                    .padding(start = 32.dp)
                                    .align(alignment = Alignment.Top)
                            )

                            Slider(
                                value = toolSizeValue,
                                onValueChange = {
                                    toolSizeValue = it
                                    viewModel.setToolSize(toolSizeValue.toInt())
                                },
                                valueRange = 1f..10f,
                                steps = 8,
                                modifier = Modifier
                                    .height(30.dp)
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .align(alignment = Alignment.CenterVertically)
                                    .padding(start = 10.dp, end = 32.dp)
                            )
                        }
                    }
                }
            }
        },

        bottomBar = {
            Column {
                Slider(
                    value = canvasZoomState.scale,
                    onValueChange = {
                        coroutineScope.launch {
                            canvasZoomState.changeScale(
                                targetScale = it,
                                position = Offset(
                                    x = layoutSize.value.width / 2f,
                                    y = layoutSize.value.height / 2f
                                )
                            )
                        }
                    },
                    valueRange = 1f..maxScale,
                    colors = SliderDefaults.colors(
                        thumbColor = CatppuccinUI.SelectedColor,
                        activeTrackColor = CatppuccinUI.Foreground0Color,
                        inactiveTrackColor = CatppuccinUI.Foreground0Color

                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CatppuccinUI.BackgroundColor)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                ToolSelector(
                    modifier = Modifier
                        .background(CatppuccinUI.BackgroundColor)
                        .padding(horizontal = 5.dp, vertical = 8.dp),
                    viewModel = viewModel
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CatppuccinUI.BackgroundColorDarker)
                .zoomable(
                    zoomState = canvasZoomState,
                    enableOneFingerZoom = false,
                    onTap = null,
                    onDoubleTap = null,
                    onLongPress = null
                )
                .onSizeChanged {
                    layoutSize.value = it
                }
        ) {

            // Canvas section
            PixelCanvas(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(10.dp)
                    .fillMaxSize()
                    .fillMaxHeight(0.7f),
                viewModel = viewModel
            )
        }
    }
}

@Preview
@Composable
private fun DrawingScreenPreview() {

    val context = LocalContext.current

    val storageLocationRepository = StorageLocationRepository(context)
    val pixelCanvasRepository = PixelCanvasRepository(PixelCanvasModel(16, 16))

    val database = AppDatabase.getInstance(context)
    val spriteDataRepository = ISpriteDatabaseRepository(database.spriteDataDao(), database.spriteMetaDataDao())

    val colorPaletteRepository = ColorPaletteRepository(context)
    val lospecColorPaletteRepository = LospecColorPaletteRepository(context)

    val viewModel = DrawingScreenViewModel(
        spriteId = "dummy",
        storageLocationRepository = storageLocationRepository,
        pixelCanvasRepository = pixelCanvasRepository,
        spriteDataRepository = spriteDataRepository,
        colorPaletteRepository = colorPaletteRepository,
        lospecColorPaletteRepository = lospecColorPaletteRepository,
    )

    DrawingScreen(
        viewModel = viewModel
    )
}