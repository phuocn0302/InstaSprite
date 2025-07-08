package com.olaz.instasprite.ui.screens.drawingscreen.dialog

import android.content.Intent
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.olaz.instasprite.ui.components.composable.ColorPaletteList
import com.olaz.instasprite.ui.components.composable.ColorPaletteListOptions
import com.olaz.instasprite.ui.components.dialog.InputDialog
import com.olaz.instasprite.ui.screens.drawingscreen.DrawingScreenViewModel
import com.olaz.instasprite.ui.theme.CatppuccinUI
import kotlinx.coroutines.launch

@Composable
fun FileImportDialog(
    onDismiss: () -> Unit,
    onImportSuccess: (List<Color>) -> Unit,
    viewModel: DrawingScreenViewModel
) {
    val context = LocalContext.current
    var previewColors by remember { mutableStateOf<List<Color>?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    selectedFileName = cursor.getString(nameIndex)
                }

                scope.launch {
                    try {
                        val colors = viewModel.importColorsFromFile(it)
                        if (colors.isEmpty()) {
                            Toast.makeText(context, "No colors found in file", Toast.LENGTH_SHORT).show()
                            previewColors = null
                        } else {
                            previewColors = colors
                            Toast.makeText(context, "Colors loaded successfully!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        previewColors = null
                        Toast.makeText(context, "An error occurred while importing the palette", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    )

    InputDialog(
        title = "Import from File",
        fields = listOf(),
        onDismiss = onDismiss,
        onConfirm = {
            if (previewColors != null && previewColors!!.isNotEmpty()) {
                viewModel.updateColorPalette(previewColors!!)
                Toast.makeText(context, "Palette imported successfully!", Toast.LENGTH_SHORT).show()
                onImportSuccess(previewColors!!)
            } else {
                Toast.makeText(context, "Please select a file first", Toast.LENGTH_SHORT).show()
            }
        },
        confirmButtonText = "Import",
        dismissButtonText = "Back",
        extraTopContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { filePickerLauncher.launch(arrayOf("text/plain")) }
            ) {
                OutlinedTextField(
                    value = selectedFileName ?: "No file selected",
                    onValueChange = {},
                    label = { Text("Import Location", color = CatppuccinUI.SelectedColor) },
                    readOnly = true,
                    enabled = false,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Choose File",
                            tint = CatppuccinUI.CurrentPalette.Blue,
                            modifier = Modifier.clickable {
                                filePickerLauncher.launch(arrayOf("text/plain"))
                            }
                        )
                    },
                    colors = CatppuccinUI.OutlineTextFieldColors.colors(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        extraBottomContent = {
            if (previewColors != null) {
                ColorPaletteList(
                    colorPaletteListOptions = ColorPaletteListOptions(
                        colors = previewColors!!,
                        isInteractive = false
                    )
                )
            }
        }
    )
}
