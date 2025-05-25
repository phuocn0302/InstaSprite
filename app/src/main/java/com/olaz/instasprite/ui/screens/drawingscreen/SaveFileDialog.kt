package com.olaz.instasprite.ui.screens.drawingscreen

import android.net.Uri
import android.provider.DocumentsContract
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.ui.theme.DrawingScreenColor

@Composable
fun SaveFileDialog(
    onDismiss: () -> Unit,
    viewModel: DrawingScreenViewModel
) {
    val context = LocalContext.current
    var fileName by remember { mutableStateOf("sprite.png") }
    var scalePercent by remember { mutableStateOf("100") }

    val lastSavedUri by viewModel.lastSavedLocation.collectAsState()
    var folderUri by remember { mutableStateOf<Uri?>(lastSavedUri) }

    LaunchedEffect(Unit) {
        folderUri = viewModel.getLastSavedLocation()
    }

    val folderPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree(),
        onResult = { uri ->
            if (uri != null) {
                folderUri = uri
                viewModel.setLastSavedLocation(uri)
            }
        }
    )
    val displayPath = folderUri?.let { getFullPathFromTreeUri(it) } ?: "Tap to select folder"

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DrawingScreenColor.PaletteBarColor,
        title = {
            Text(text = "Save Image", color = Color.White)
        },
        text = {
            Column {

                // Path selection
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { folderPickerLauncher.launch(null) }
                ) {
                    OutlinedTextField(
                        value = displayPath,
                        onValueChange = {},
                        label = {
                            Text(text = "Save Location", color = Color.White)
                        },
                        readOnly = true,
                        enabled = false,
                        trailingIcon = {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Choose Folder",
                                tint = Color.White
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            disabledTextColor = Color.White,
                            disabledBorderColor = Color.Gray,
                            disabledLabelColor = Color.White,
                            disabledTrailingIconColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // File name section
                OutlinedTextField(
                    value = fileName,
                    onValueChange = { newValue ->
                        val sanitized = newValue.filter { char ->
                            char.isLetterOrDigit() || char in "._-"
                        }
                        fileName = sanitized
                    },
                    label = {
                        Text(text = "File Name", color = Color.White)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Scale section
                OutlinedTextField(
                    value = scalePercent,
                    onValueChange = { newValue ->
                        when {
                            newValue.isEmpty() -> scalePercent = ""
                            newValue.all { it.isDigit() } -> {
                                val scale = newValue.toIntOrNull()
                                if (scale != null && scale <= 2000) {
                                    scalePercent = newValue
                                }
                            }
                        }
                    },
                    label = {
                        Text(text = "Scale (%)", color = Color.White)
                    },
                    placeholder = {
                        Text(text = "100", color = Color.Gray)
                    },
                    trailingIcon = {
                        Text(text = "%", color = Color.White)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = {
                        Text(
                            text = "Range: 25-2000%",
                            color = Color.Gray
                        )
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    when {
                        folderUri == null -> {
                            Toast.makeText(context, "Please select a folder", Toast.LENGTH_SHORT)
                                .show()
                        }

                        fileName.isBlank() -> {
                            Toast.makeText(context, "Please enter a file name", Toast.LENGTH_SHORT)
                                .show()
                        }

                        else -> {
                            val scale = scalePercent.toIntOrNull()?.coerceIn(25, 2000) ?: 100
                            val success = viewModel.saveFile(context, folderUri!!, fileName, scale)
                            Toast.makeText(
                                context,
                                if (success) "Image saved successfully!" else "Failed to save image",
                                Toast.LENGTH_SHORT
                            ).show()
                            if (success) onDismiss()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DrawingScreenColor.ButtonColor,
                )
            ) {
                Text(text = "Save", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel", color = Color.White)
            }
        }
    )
}

fun getFullPathFromTreeUri(treeUri: Uri): String {
    return try {
        val docId = DocumentsContract.getTreeDocumentId(treeUri)
        val parts = docId.split(":")

        if (parts.size < 2) return "Selected folder"

        val storageType = parts[0]
        val pathPart = parts[1]

        when (storageType) {
            "primary" -> {
                if (pathPart.isEmpty() || pathPart == "/") {
                    "Internal Storage"
                } else {
                    "Internal Storage/$pathPart"
                }
            }

            else -> {
                if (pathPart.isEmpty() || pathPart == "/") {
                    "External Storage ($storageType)"
                } else {
                    "External Storage ($storageType)/$pathPart"
                }
            }
        }
    } catch (_: Exception) {
        "Selected folder"
    }
}