package com.olaz.instasprite.ui.screens.drawingscreen.dialog

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.olaz.instasprite.data.model.InputField
import com.olaz.instasprite.ui.components.dialog.SaveFileDialog
import com.olaz.instasprite.ui.screens.drawingscreen.DrawingScreenViewModel

@Composable
fun SaveImageDialog(
    onDismiss: () -> Unit,
    viewModel: DrawingScreenViewModel
) {
    val context = LocalContext.current
    var fileName by remember { mutableStateOf("Sprite") }
    var scalePercent by remember { mutableStateOf("100") }
    var folderUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        folderUri = viewModel.getLastSavedLocation()
    }

    SaveFileDialog(
        title = "Export Image",
        fields = listOf(
            InputField(
                label = "Name",
                placeholder = "Sprite",
                keyboardType = KeyboardType.Text,
                suffix = ".png",
                validator = { it.isNotBlank() },
                errorMessage = "Name cannot be blank",
                defaultValue = "Sprite"
            ),
            InputField(
                label = "Scale",
                placeholder = "100",
                keyboardType = KeyboardType.Number,
                suffix = "%",
                validator = { it.toIntOrNull() != null && it.toInt() in 25..20000 },
                errorMessage = "Must be a number between 25 and 20000",
                defaultValue = "100"
            )
        ),
        lastSavedUri = folderUri,
        onFolderSelected = { uri ->
            folderUri = uri
            viewModel.setLastSavedLocation(uri)
        },
        onSave = {
            folderUri?.let { uri ->
                val scale = scalePercent.toIntOrNull()?.coerceIn(25, 20000) ?: 100
                val success = viewModel.saveImage(context, uri, "$fileName.png", scale)
                Toast.makeText(
                    context,
                    if (success) "Image saved successfully!" else "Failed to save image",
                    Toast.LENGTH_SHORT
                ).show()
                if (success) onDismiss()
            }
        },
        onDismiss = onDismiss,
        onValuesChanged = { values, _ ->
            fileName = values.getOrNull(0)?.takeIf { it.isNotBlank() } ?: "Sprite"
            scalePercent = values.getOrNull(1)?.takeIf { it.isNotBlank() } ?: "100"
        }
    )
}