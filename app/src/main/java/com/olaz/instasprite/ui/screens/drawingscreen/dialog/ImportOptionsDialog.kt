package com.olaz.instasprite.ui.screens.drawingscreen.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.olaz.instasprite.ui.theme.HomeScreenColor

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