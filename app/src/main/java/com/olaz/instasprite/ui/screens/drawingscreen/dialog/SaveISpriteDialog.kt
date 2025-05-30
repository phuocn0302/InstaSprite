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
fun SaveISpriteDialog(
    onDismiss: () -> Unit,
    viewModel: DrawingScreenViewModel
) {
    val context = LocalContext.current
    var fileName by remember { mutableStateOf("Sprite") }
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
                suffix = ".isprite",
                validator = { it.isNotBlank() },
                errorMessage = "Name cannot be blank",
                defaultValue = "Sprite"
            ),
        ),
        lastSavedUri = folderUri,
        onFolderSelected = { uri ->
            folderUri = uri
            viewModel.setLastSavedLocation(uri)
        },
        onSave = {
            folderUri?.let { uri ->
                val success = viewModel.saveISprite(context, folderUri!!, fileName)
                Toast.makeText(
                    context,
                    if (success) "$fileName saved successfully!" else "Failed to file",
                    Toast.LENGTH_SHORT
                ).show()
                if (success) onDismiss()
            }
        },
        onDismiss = onDismiss,
        onValuesChanged = { values, _ ->
            fileName = values.getOrNull(0)?.takeIf { it.isNotBlank() } ?: "Sprite"
        }
    )
}