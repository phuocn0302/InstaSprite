package com.olaz.instasprite.ui.screens.drawingscreen.dialog


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.input.KeyboardType
import com.olaz.instasprite.data.model.InputField
import com.olaz.instasprite.ui.components.dialog.InputDialog
import com.olaz.instasprite.ui.screens.drawingscreen.DrawingScreenViewModel


@Composable
fun ResizeCanvasDialog(
    onDismiss: () -> Unit,
    viewModel: DrawingScreenViewModel
) {

    val canvasState = viewModel.canvasState.collectAsState()

    val canvasWidth = canvasState.value.width
    val canvasHeight = canvasState.value.height

    InputDialog(
        title = "Resize canvas",
        fields = listOf(
            InputField(
                label = "Width",
                placeholder = "Width",
                suffix = "px",
                defaultValue = canvasWidth.toString(),
                keyboardType = KeyboardType.Number,
                validator = { it.toIntOrNull() != null && it.toInt() > 0 },
                errorMessage = "Must be a number larger than 0"
            ),
            InputField(
                label = "Height",
                placeholder = "Height",
                suffix = "px",
                defaultValue = canvasHeight.toString(),
                keyboardType = KeyboardType.Number,
                validator = { it.toIntOrNull() != null && it.toInt() > 0 },
                errorMessage = "Must be a number larger than 0"
            )
        ),
        onDismiss = onDismiss,
        onConfirm = {
            viewModel.resizeCanvas(it[0].toInt(), it[1].toInt())
            onDismiss()
        },
    )
}