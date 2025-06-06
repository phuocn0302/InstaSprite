package com.olaz.instasprite.ui.screens.homescreen.dialog

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.olaz.instasprite.ui.components.dialog.CustomDialog

@Composable
fun DeleteSpriteConfirmDialog(
    spriteName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    CustomDialog(
        title = "Delete sprite",
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        confirmButtonText = "Delete",
        dismissButtonText = "Cancel",
        content = {
            Text(
                text = "Are you sure you want to delete $spriteName?",
                color = Color.White
            )
        }
    )
}