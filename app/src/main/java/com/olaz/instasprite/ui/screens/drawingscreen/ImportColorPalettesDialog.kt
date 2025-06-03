package com.olaz.instasprite.ui.screens.drawingscreen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.olaz.instasprite.data.repository.ColorPaletteRepository
import com.olaz.instasprite.ui.theme.HomeScreenColor
import kotlinx.coroutines.launch

@Composable
fun ImportColorPalettesDialog(
    onDismiss: () -> Unit,
    onImportPalette: (List<Color>) -> Unit
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
            onImportPalette = onImportPalette
        )
    }

    if (showFileImport) {
        FileImportDialog(
            onDismiss = {
                showFileImport = false
                showImportOptions = true
            },
            onImportPalette = onImportPalette
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
fun LospecImportDialog(
    onDismiss: () -> Unit,
    onImportPalette: (List<Color>) -> Unit
) {
    var paletteUrl by remember { mutableStateOf("") }
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
                    onValueChange = { paletteUrl = it },
                    label = { Text("Lospec URL", color = Color.White) },
                    placeholder = { Text("https://lospec.com/palette-list/example", color = Color.Gray) },
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                if (paletteUrl.isBlank()) {
                                    Toast.makeText(context, "Please enter a Lospec URL", Toast.LENGTH_SHORT).show()
                                    return@launch
                                }

                                if (!paletteUrl.contains("lospec.com")) {
                                    Toast.makeText(context, "Please enter a valid Lospec URL", Toast.LENGTH_SHORT).show()
                                    return@launch
                                }

                                repository.fetchPaletteFromUrl(paletteUrl)
                                    .onSuccess { colors ->
                                        onImportPalette(colors)
                                        Toast.makeText(context, "Palette imported successfully!", Toast.LENGTH_SHORT).show()
                                        onDismiss()
                                    }
                                    .onFailure {
                                        Toast.makeText(context, "Failed to import palette. Please check the URL and try again.", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HomeScreenColor.ButtonColor
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Import")
                    }

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HomeScreenColor.ButtonColor
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Back")
                    }
                }
            }
        }
    }
}

@Composable
fun FileImportDialog(
    onDismiss: () -> Unit,
    onImportPalette: (List<Color>) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { ColorPaletteRepository() }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                repository.importPaletteFromFile(context, uri)
                    .onSuccess { colors ->
                        onImportPalette(colors)
                        Toast.makeText(context, "Palette imported successfully!", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    }
                    .onFailure {
                        Toast.makeText(context, "Failed to import palette from file.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

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
                    text = "Import from File",
                    fontSize = 18.sp,
                    color = Color.White
                )

//                Text(
//                    text = "Select file contain color values",
//                    fontSize = 14.sp,
//                    color = Color.White
//                )

                Button(
                    onClick = { filePickerLauncher.launch("text/plain") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HomeScreenColor.ButtonColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Choose File")
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HomeScreenColor.ButtonColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back")
                }
            }
        }
    }
} 