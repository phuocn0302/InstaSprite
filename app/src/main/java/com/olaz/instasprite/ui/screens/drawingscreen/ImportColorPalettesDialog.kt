package com.olaz.instasprite.ui.screens.drawingscreen

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.olaz.instasprite.ui.components.FileImportDialog
import com.olaz.instasprite.ui.components.ImportOptionsDialog
import com.olaz.instasprite.ui.components.LospecImportDialog

@Composable
fun ImportColorPalettesDialog(
    onDismiss: () -> Unit,
    onImportPalette: (List<Color>) -> Unit,
    onImportFromUrl: suspend (String) -> List<Color>,
    onImportFromFile: suspend (android.content.Context, android.net.Uri) -> List<Color>,
    onUpdateColorPalette: (List<Color>) -> Unit
) {
    var showImportOptions by remember { mutableStateOf(true) }
    var showLospecImport by remember { mutableStateOf(false) }
    var showFileImport by remember { mutableStateOf(false) }

    if (showImportOptions) {
        ImportOptionsDialog(
            onDismiss = onDismiss,
            onLospecSelected = {
                showImportOptions = false
                showLospecImport = true
            },
            onFileSelected = {
                showImportOptions = false
                showFileImport = true
            }
        )
    }

    if (showLospecImport) {
        LospecImportDialog(
            onDismiss = {
                showLospecImport = false
                showImportOptions = true
            },
            onImportSuccess = { colors ->
                onImportPalette(colors)
            },
            onImportFromUrl = onImportFromUrl,
            onUpdateColorPalette = onUpdateColorPalette
        )
    }

    if (showFileImport) {
        FileImportDialog(
            onDismiss = {
                showFileImport = false
                showImportOptions = true
            },
            onImportSuccess = { colors ->
                onImportPalette(colors)
            },
            onImportFromFile = onImportFromFile,
            onUpdateColorPalette = onUpdateColorPalette
        )
    }
}




