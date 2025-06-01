package com.olaz.instasprite.ui.screens.drawingscreen.dialog

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.model.PixelCanvasModel
import com.olaz.instasprite.data.repository.PixelCanvasRepository
import com.olaz.instasprite.data.repository.StorageLocationRepository
import com.olaz.instasprite.ui.components.composable.CanvasPreviewer
import com.olaz.instasprite.ui.components.dialog.CustomDialog
import com.olaz.instasprite.ui.screens.drawingscreen.DrawingScreenViewModel
import com.olaz.instasprite.utils.getFileName

@Composable
fun LoadISpriteDialog(
    onDismiss: () -> Unit, viewModel: DrawingScreenViewModel
) {
    val context = LocalContext.current

    var fileUri by remember { mutableStateOf<Uri?>(null) }
    var spriteData by remember { mutableStateOf<ISpriteData?>(null) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(), onResult = { uri: Uri? ->
            uri?.let {
                val fileName = getFileName(context, it)
                if (fileName?.endsWith(".isprite") == true) {
                    fileUri = it
                    spriteData = viewModel.getISpriteDataFromFile(context, it)
                } else {
                    Toast.makeText(context, "Invalid file type", Toast.LENGTH_SHORT).show()
                }
            }
        })

    val displayPath = fileUri?.let { getFileName(context, it) } ?: "Tap to select file"
    val spriteWidth = spriteData?.width ?: ""
    val spriteHeight = spriteData?.height ?: ""

    CustomDialog(title = "Import ISprite", onDismiss = onDismiss, onConfirm = {
        spriteData?.let {
            viewModel.loadISprite(spriteData!!)
            onDismiss()
        } ?: Toast.makeText(
            context, "No file loaded", Toast.LENGTH_SHORT
        ).show()
    }, confirmButtonText = "Load", dismissButtonText = "Cancel", content = {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { filePickerLauncher.launch(arrayOf("*/*")) }) {
                OutlinedTextField(
                    value = displayPath,
                    onValueChange = {},
                    label = { Text("File", color = Color.White) },
                    readOnly = true,
                    enabled = false,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Choose File",
                            tint = Color.White
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

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Width: $spriteWidth", color = Color.White)
                Text("Height: $spriteHeight", color = Color.White)
            }

            spriteData?.let {
                Spacer(Modifier.height(12.dp))
                CanvasPreviewer(spriteData!!)
            }
        }
    })
}

@Composable
@Preview
fun LoadISpriteDialogPreview() {
    LoadISpriteDialog(
        onDismiss = {}, viewModel = DrawingScreenViewModel(
            storageLocationRepository = StorageLocationRepository(LocalContext.current),
            pixelCanvasRepository = PixelCanvasRepository(
                PixelCanvasModel(16,16)
            )
        )
    )
}
