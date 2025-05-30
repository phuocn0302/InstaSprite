package com.olaz.instasprite.ui.components.dialog

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.data.model.InputField


@Composable
fun InputDialog(
    title: String,
    fields: List<InputField>,
    onDismiss: () -> Unit,
    onConfirm: (List<String>) -> Unit,
    confirmButtonText: String = "OK",
    dismissButtonText: String = "Cancel",
    extraTopContent: @Composable () -> Unit = {},
    extraBottomContent: @Composable () -> Unit = {}
) {
    val context = LocalContext.current

    val inputStates = remember {
        fields.map { field ->
            mutableStateOf(field.defaultValue)
        }
    }

    CustomDialog(
        title = title,
        onDismiss = onDismiss,
        onConfirm = {
            val allValid = fields.withIndex().all { (i, field) ->
                field.validator(inputStates[i].value)
            }

            if (allValid) {
                onConfirm(inputStates.map { it.value })
            } else {
                Toast.makeText(context, "Input errors", Toast.LENGTH_SHORT).show()
            }
        },
        confirmButtonText = confirmButtonText,
        dismissButtonText = dismissButtonText,
        content = {
            Column {
                extraTopContent()

                fields.forEachIndexed { index, field ->
                    OutlinedTextField(
                        value = inputStates[index].value,
                        onValueChange = { inputStates[index].value = it },
                        label = { Text(field.label, color = Color.White) },
                        placeholder = {
                            if (field.placeholder.isNotBlank())
                                Text(field.placeholder, color = Color.Gray)
                        },
                        trailingIcon = {
                            field.suffix?.let {
                                Text(
                                    it,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 14.dp)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = field.keyboardType),
                        singleLine = true,
                        isError = !field.validator(inputStates[index].value),
                        supportingText = {
                            if (!field.validator(inputStates[index].value)) {
                                Text(field.errorMessage, color = Color.Red)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,

                            errorTextColor = Color.Red,
                            errorBorderColor = Color.Red,
                            errorLabelColor = Color.Red,
                            errorCursorColor = Color.Red,
                            errorTrailingIconColor = Color.Red,
                            errorSupportingTextColor = Color.Red
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                extraBottomContent()
            }
        }
    )
}

@Composable
@Preview
fun InputDialogPreview() {
    InputDialog(
        title = "Title",
        fields = listOf(
            InputField(
                label = "Label 1",
                placeholder = "Placeholder 1",
                keyboardType = KeyboardType.Text,
                suffix = "Suffix 1",
                validator = { it.isNotBlank() },
            ),
            InputField(
                label = "Label 2",
                placeholder = "Placeholder 2",
                keyboardType = KeyboardType.Number,
                validator = { it.toIntOrNull() != null },
                errorMessage = "Must be a number"
            )
        ),
        onConfirm = {},
        onDismiss = {},
        extraTopContent = {
            Text("Extra content at the top", color = Color.White)
        },
        extraBottomContent = {
            Text("Extra content at the bottom", color = Color.Gray)
        }
    )
}