package com.olaz.instasprite.ui.screens.drawingscreen

import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.olaz.instasprite.data.model.InputField
import com.olaz.instasprite.data.repository.ColorPaletteRepository
import com.olaz.instasprite.ui.components.dialog.InputDialog
import com.olaz.instasprite.ui.theme.HomeScreenColor
import kotlinx.coroutines.launch

@Composable
fun ImportColorPalettesDialog(
    onDismiss: () -> Unit,
    onImportPalette: (List<Color>) -> Unit,
    viewModel: DrawingScreenViewModel
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
            onImportPalette = onImportPalette,
            viewModel = viewModel
        )
    }

    if (showFileImport) {
        FileImportDialog(
            onDismiss = {
                showFileImport = false
                showImportOptions = true
            },
            onImportPalette = { colors ->
                onImportPalette(colors)
                if (colors.isNotEmpty()) {
                    viewModel.selectColor(colors.first())
                }
            }
        )
    }
}

@Composable
fun ImportOptionsDialog(
    onDismiss: () -> Unit,
    onLospecSelected: () -> Unit,
    onFileSelected: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .background(
                    color = HomeScreenColor.BackgroundColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Import Color Palettes",
                    fontSize = 18.sp,
                    color = Color.White
                )

                Button(
                    onClick = onLospecSelected,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HomeScreenColor.ButtonColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Import from Lospec")
                }

                Button(
                    onClick = onFileSelected,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HomeScreenColor.ButtonColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Import from File")
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HomeScreenColor.ButtonColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
private fun ColorPalettePreview(
    colors: List<Color>?,
) {
    if (colors != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Preview",
                color = Color.White,
                fontSize = 14.sp
            )
            
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(colors) { color ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(
                                color = color,
                                shape = RoundedCornerShape(8.dp)
                            )
                    )
                }
            }

            }
        }
    }

@Composable
private fun FileImportDialog(
    onDismiss: () -> Unit,
    onImportPalette: (List<Color>) -> Unit
) {
    val context = LocalContext.current
    val repository = remember { ColorPaletteRepository() }
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
                
                // Get the file name from the URI using ContentResolver
                context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    selectedFileName = cursor.getString(nameIndex)
                }

                scope.launch {
                    try {
                        repository.importPaletteFromFile(context, uri)
                            .onSuccess { colors ->
                                if (colors.isEmpty()) {
                                    Toast.makeText(context, "No colors found in file", Toast.LENGTH_SHORT).show()
                                    return@launch
                                }
                                previewColors = colors
                                Toast.makeText(context, "Colors loaded successfully!", Toast.LENGTH_SHORT).show()
                            }
                            .onFailure {
                                previewColors = null
                                Toast.makeText(context, "Failed to import palette from file", Toast.LENGTH_SHORT).show()
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
                onImportPalette(previewColors!!)
                onDismiss()
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
                    label = { Text("Import Location", color = Color.White) },
                    readOnly = true,
                    enabled = false,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Choose File",
                            tint = Color.White,
                            modifier = Modifier.clickable { 
                                filePickerLauncher.launch(arrayOf("text/plain")) 
                            }
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.White,
                        disabledBorderColor = Color.Gray,
                        disabledLabelColor = Color.White,
                        disabledTrailingIconColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        extraBottomContent = {
            if (previewColors != null) {
                ColorPalettePreview(
                    colors = previewColors,
                )
            }
        }
    )
}

@Composable
fun LospecImportDialog(
    onDismiss: () -> Unit,
    onImportPalette: (List<Color>) -> Unit,
    viewModel: DrawingScreenViewModel
) {
    var paletteUrl by remember { mutableStateOf("") }
    var previewColors by remember { mutableStateOf<List<Color>?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { ColorPaletteRepository() }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .width(380.dp)
                .background(
                    color = HomeScreenColor.BackgroundColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Import from Lospec",
                    fontSize = 18.sp,
                    color = Color.White
                )

                OutlinedTextField(
                    value = paletteUrl,
                    onValueChange = { newUrl ->
                        paletteUrl = newUrl
                    },
                    label = { Text("Lospec URL", color = Color.White) },
                    placeholder = {
                        Text(
                            "https://lospec.com/palette-list/example",
                            color = Color.Gray
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (previewColors != null) {
                    ColorPalettePreview(
                        colors = previewColors,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HomeScreenColor.ButtonColor
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Back")
                    }

                    Button(
                        onClick = {
                            if (previewColors != null && previewColors!!.isNotEmpty()) {
                                onImportPalette(previewColors!!)

                                viewModel.selectColor(previewColors!!.first())
                                Toast.makeText(
                                    context,
                                    "Palette imported successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onDismiss()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Please fetch a palette first",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HomeScreenColor.ButtonColor
                        ),
                        enabled = previewColors != null && previewColors!!.isNotEmpty(),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Import")
                    }

                    Button(
                        onClick = {
                            scope.launch {

                                val trimmedUrl = paletteUrl.trim()

                                if (trimmedUrl.isBlank()) {
                                    Toast.makeText(
                                        context,
                                        "Please enter a Lospec URL",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@launch
                                }


                                if (!trimmedUrl.contains("lospec.com") ||
                                    !trimmedUrl.contains("palette-list")) {
                                    Toast.makeText(
                                        context,
                                        "Please enter a valid Lospec palette URL",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@launch
                                }

                                try {
                                    repository.fetchPaletteFromUrl(trimmedUrl)
                                        .onSuccess { colors ->
                                            if (colors.isEmpty()) {
                                                Toast.makeText(
                                                    context,
                                                    "No colors found in the palette",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                previewColors = null
                                                return@launch
                                            }
                                            previewColors = colors
                                            Toast.makeText(
                                                context,
                                                "Palette fetched successfully!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        .onFailure { error ->
                                            previewColors = null
                                            val message = when {
                                                error.message?.contains("404") == true ->
                                                    "Palette not found. Please check the URL."
                                                error.message?.contains("network") == true ->
                                                    "Network error. Please check your connection."
                                                else ->
                                                    "Failed to fetch palette. Please check the URL and try again."
                                            }
                                            Toast.makeText(
                                                context,
                                                message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                } catch (e: Exception) {
                                    previewColors = null
                                    Toast.makeText(
                                        context,
                                        "An error occurred while fetching the palette",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HomeScreenColor.ButtonColor
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Fetch")
                    }
                }
            }
        }
    }
}
