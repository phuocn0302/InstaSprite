package com.olaz.instasprite.ui.screens.homescreen.dialog

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.olaz.instasprite.ui.components.dialog.CustomDialog
import com.olaz.instasprite.ui.theme.CatppuccinUI

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
                buildAnnotatedString {
                    append("Are you sure you want to delete ")
                    withStyle(style = SpanStyle(color = CatppuccinUI.DismissButtonColor)) {
                        append(spriteName)
                    }
                    append("?")
                }
            )
        }
    )
}