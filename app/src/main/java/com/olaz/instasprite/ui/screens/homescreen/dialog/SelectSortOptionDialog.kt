package com.olaz.instasprite.ui.screens.homescreen.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.ui.screens.homescreen.HomeScreenViewModel
import com.olaz.instasprite.ui.screens.homescreen.SpriteListOrder
import com.olaz.instasprite.ui.theme.DrawingScreenColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectSortOptionDialog(
    onDismiss: () -> Unit,
    viewModel: HomeScreenViewModel,
) {
    val options = mapOf(
        "A - Z" to SpriteListOrder.Name,
        "Z - A" to SpriteListOrder.NameDesc,
        "Date Created" to SpriteListOrder.DateCreated,
        "Date Created Desc" to SpriteListOrder.DateCreatedDesc,
        "Date Modified" to SpriteListOrder.LastModified,
        "Date Modified Desc" to SpriteListOrder.LastModifiedDesc
    )

    val selectedOption = remember { mutableStateOf(viewModel.spriteListOrder) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DrawingScreenColor.PaletteBarColor,
        title = {
            Text(text = "Sort By", color = Color.White)
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .selectableGroup()
            ) {
                options.forEach { (label, sortOrder) ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .selectable(
                                selected = (sortOrder == selectedOption.value),
                                onClick = {
                                    selectedOption.value = sortOrder
                                    viewModel.spriteListOrder = sortOrder
                                    viewModel.saveSortSetting(sortOrder)
                                    onDismiss()
                                },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (sortOrder == selectedOption.value),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.White,
                                unselectedColor = Color.Gray
                            ),
                            onClick = null
                        )
                        Text(
                            text = label,
                            color = Color.White,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}
