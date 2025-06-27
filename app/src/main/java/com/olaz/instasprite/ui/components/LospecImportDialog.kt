package com.olaz.instasprite.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.olaz.instasprite.data.model.ColorPaletteModel
import com.olaz.instasprite.ui.screens.drawingscreen.ColorPaletteContent
import com.olaz.instasprite.ui.screens.drawingscreen.DrawingScreenViewModel
import com.olaz.instasprite.ui.theme.HomeScreenColor
import kotlinx.coroutines.launch

@Composable
fun LospecImportDialog(
    onDismiss: () -> Unit,
    onImportSuccess: (List<Color>) -> Unit,
    viewModel: DrawingScreenViewModel
) {
    var paletteUrl by remember { mutableStateOf("") }
    var previewColors by remember { mutableStateOf<List<Color>?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
                    ColorPaletteContent(
                        colors = previewColors!!,
                        showPreviewLabel = true
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
                                viewModel.updateColorPalette(previewColors!!)
                                Toast.makeText(
                                    context,
                                    "Palette imported successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onImportSuccess(previewColors!!)
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
                                    val colors = viewModel.importFromUrl(trimmedUrl)
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