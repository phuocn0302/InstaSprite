package com.olaz.instasprite.ui.screens.drawingscreen.dialog

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.olaz.instasprite.ui.components.composable.ColorPaletteList
import com.olaz.instasprite.ui.components.composable.ColorPaletteListOptions
import com.olaz.instasprite.ui.components.dialog.CustomDialog
import com.olaz.instasprite.ui.screens.drawingscreen.DrawingScreenViewModel
import com.olaz.instasprite.ui.theme.CatppuccinUI
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

    CustomDialog(
        title = "Import from Lospec",
        onDismiss = onDismiss,
        onConfirm = {
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
        confirmButtonText = "Import",
        dismissButtonText = "Back",
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = paletteUrl,
                    onValueChange = { newUrl ->
                        paletteUrl = newUrl
                    },
                    label = { Text("Lospec URL", color = CatppuccinUI.SelectedColor) },
                    placeholder = {
                        Text(
                            "https://lospec.com/palette-list/example",
                             color = CatppuccinUI.Subtext1Color
                        )
                    },
                    singleLine = true,
                    colors = CatppuccinUI.OutlineTextFieldColors.colors(),
                    modifier = Modifier.fillMaxWidth()
                )

                if (previewColors != null) {
                    ColorPaletteList(
                        colorPaletteListOptions = ColorPaletteListOptions(
                            colors = previewColors!!,
                            isInteractive = false
                        )
                    )
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
                                val colors = viewModel.importColorsFromLospecUrl(trimmedUrl)
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
                        containerColor = CatppuccinUI.AccentButtonColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Fetch Palette", color = CatppuccinUI.TextColorDark)
                }
            }
        }
    )
}
