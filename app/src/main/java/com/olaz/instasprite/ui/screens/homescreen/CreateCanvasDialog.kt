package com.olaz.instasprite.ui.screens.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.olaz.instasprite.ui.theme.HomeScreenColor


@Composable
fun CreateCanvasDialog(
    onDismiss: () -> Unit,
    onCreateCanvas: (width: Int, height: Int) -> Unit
) {
    var heightVal by remember { mutableStateOf(TextFieldValue(text = "")) }
    var widthVal by remember { mutableStateOf(TextFieldValue(text = "")) }
    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(10.dp))
                .background(HomeScreenColor.BackgroundColor)
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "New Canvas",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(
                    modifier = Modifier
                        .height(25.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "Height",
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier
                            .weight(0.5f)
                    )
                    OutlinedTextField(
                        value = heightVal,
                        onValueChange = { heightVal = it },
                        placeholder = {
                            Text(
                                text = "16",
                                color = Color.White
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "Width",
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier
                            .weight(0.5f)
                    )
                    OutlinedTextField(
                        value = widthVal,
                        onValueChange = { widthVal = it },
                        placeholder = {
                            Text(
                                text = "16",
                                color = Color.White
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                        ),
                        modifier = Modifier
                            .weight(1f)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                ) {
                    Button(
                        onClick = {
                            try {
                                val width = widthVal.text.toIntOrNull() ?: 16
                                val height = heightVal.text.toIntOrNull() ?: 16
                                onCreateCanvas(width, height)
                            } catch (e: Exception) {
                                onCreateCanvas(16, 16)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HomeScreenColor.ButtonColor,
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .width(100.dp)
                    ) {
                        Text(
                            text = "Create",
                        )
                    }
                    Spacer(
                        modifier = Modifier.width(60.dp)
                    )
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HomeScreenColor.ButtonColor,
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .width(100.dp)
                    ) {
                        Text(
                            text = "Cancel",
                        )
                    }
                }
            }
        }
    }
}
