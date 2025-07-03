package com.olaz.instasprite.ui.screens.drawingscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.ui.theme.DrawingScreenColor
import com.olaz.instasprite.utils.UiUtils
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.ZoomState
import net.engawapg.lib.zoomable.zoomable

@SuppressLint("DefaultLocale", "ConfigurationScreenWidthHeight")
@Composable
fun DrawingScreen(viewModel: DrawingScreenViewModel) {
    UiUtils.SetStatusBarColor(DrawingScreenColor.PaletteBarColor)
    UiUtils.SetNavigationBarColor(DrawingScreenColor.PaletteBarColor)

    val viewModel = viewModel
    val uiState by viewModel.uiState.collectAsState()

    val maxScale by remember(uiState.canvasWidth, uiState.canvasHeight) {
        derivedStateOf {
            val canvasSize = maxOf(uiState.canvasWidth, uiState.canvasHeight).toFloat()
            canvasSize.div(8f).coerceAtLeast(2f).coerceAtMost(100f)
        }
    }

    val canvasZoomState = remember(maxScale) {
        ZoomState(maxScale = maxScale)
    }

    val layoutSize = remember { mutableStateOf(IntSize.Zero) }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            ColorPalette(
                modifier = Modifier
                    .background(DrawingScreenColor.PaletteBarColor)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                viewModel = viewModel
            )
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
                        thumbColor = Color.White,
                        activeTrackColor = DrawingScreenColor.SelectedToolColor,
                        inactiveTrackColor = DrawingScreenColor.SelectedToolColor

                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DrawingScreenColor.PaletteBarColor)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                ToolSelector(
                    modifier = Modifier
                        .background(DrawingScreenColor.PaletteBarColor)
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
                .background(DrawingScreenColor.BackgroundColor)
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
