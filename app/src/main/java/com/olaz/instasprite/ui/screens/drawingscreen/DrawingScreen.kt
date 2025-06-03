package com.olaz.instasprite.ui.screens.drawingscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.ui.theme.DrawingScreenColor
import com.olaz.instasprite.utils.UiUtils
import kotlin.math.roundToInt
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Surface
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.foundation.layout.PaddingValues

@SuppressLint("DefaultLocale")
@Composable
fun DrawingScreen(viewModel: DrawingScreenViewModel) {
    UiUtils.SetStatusBarColor(DrawingScreenColor.PaletteBarColor)
    UiUtils.SetNavigationBarColor(DrawingScreenColor.PaletteBarColor)

    val viewModel = viewModel
    val uiState by viewModel.uiState.collectAsState()
    var showColorWheel by remember { mutableStateOf(false) }

    if (showColorWheel) {
        ColorWheelDialog(
            initialColor = uiState.selectedColor,
            onDismiss = { showColorWheel = false },
            onColorSelected = { color ->
                viewModel.selectColor(color)
                showColorWheel = false
            }
        )
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(DrawingScreenColor.PaletteBarColor)
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .fillMaxWidth(),
            ) {
                ColorPalette(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    viewModel = viewModel
                )


            }
        },

        bottomBar = {
            Column {
                Slider(
                    value = uiState.canvasScale,
                    onValueChange = { viewModel.setCanvasScale(it) },
                    valueRange = 0.5f..10f,
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
                .graphicsLayer(
                    scaleX = uiState.canvasScale,
                    scaleY = uiState.canvasScale,
                )
                .pointerInput(uiState.selectedTool) {
                    detectTransformGestures(
                        onGesture = { _, pan, zoom, _ ->
                            viewModel.setCanvasScale(uiState.canvasScale * zoom)
                            viewModel.setCanvasOffset(uiState.canvasOffset + pan)
                        }
                    )
                }
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { showColorWheel = true },
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .size(32.dp)
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(4.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DrawingScreenColor.PaletteBackgroundColor
                    ),
                    contentPadding = PaddingValues(4.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Color",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Canvas section
                PixelCanvas(
                    modifier = Modifier
                        .padding(10.dp)
                        .offset {
                            IntOffset(uiState.canvasOffset.x.roundToInt(), uiState.canvasOffset.y.roundToInt())
                        }
                        .fillMaxSize()
                        .fillMaxHeight(0.7f),
                    viewModel = viewModel
                )
            }
        }
    }
}
