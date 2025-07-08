package com.olaz.instasprite.ui.screens.drawingscreen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.R
import com.olaz.instasprite.domain.tool.EraserTool
import com.olaz.instasprite.domain.tool.EyedropperTool
import com.olaz.instasprite.domain.tool.FillTool
import com.olaz.instasprite.domain.tool.MoveTool
import com.olaz.instasprite.domain.tool.PencilTool
import com.olaz.instasprite.ui.screens.drawingscreen.dialog.LoadISpriteDialog
import com.olaz.instasprite.ui.screens.drawingscreen.dialog.SaveISpriteDialog
import com.olaz.instasprite.ui.screens.drawingscreen.dialog.SaveImageDialog
import com.olaz.instasprite.ui.theme.CatppuccinUI

@Composable
fun ToolSelector(
    modifier: Modifier = Modifier,
    viewModel: DrawingScreenViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var toolListVisible by remember { mutableStateOf(false) }
    var menuListVisible by remember { mutableStateOf(false) }
    var saveImageDialogVisible by remember { mutableStateOf(false) }
    var saveISpriteDialogVisible by remember { mutableStateOf(false) }
    var loadISpriteDialogVisible by remember { mutableStateOf(false) }

    when {
        saveImageDialogVisible -> SaveImageDialog(
            onDismiss = {
                saveImageDialogVisible = false
                menuListVisible = false
            },
            viewModel = viewModel
        )

        saveISpriteDialogVisible -> SaveISpriteDialog(
            onDismiss = {
                saveISpriteDialogVisible = false
                menuListVisible = false
            },
            viewModel = viewModel
        )

        loadISpriteDialogVisible -> LoadISpriteDialog(
            onDismiss = {
                loadISpriteDialogVisible = false
                menuListVisible = false
            },
            viewModel = viewModel
        )
    }

    val tools = listOf(
        PencilTool,
        EraserTool,
        FillTool,
        EyedropperTool,
        MoveTool
    )

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Box {
            ToolItem(
                iconResourceId = uiState.selectedTool.icon,
                contentDescription = uiState.selectedTool.name,
                selected = true,
                onClick = { toolListVisible = true }
            )

            DropdownMenu(
                expanded = toolListVisible,
                containerColor = CatppuccinUI.DropDownMenuColor,
                onDismissRequest = { toolListVisible = false }
            ) {
                tools.reversed().forEach { tool ->
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                painter = painterResource(tool.icon),
                                contentDescription = tool.description,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(32.dp)
                            )
                        },
                        text = { Text(tool.name) },
                        onClick = {
                            viewModel.selectTool(tool)
                            toolListVisible = false
                        }
                    )
                }
            }

        }

        ToolItem(
            iconResourceId = R.drawable.ic_undo,
            contentDescription = "Undo last change",
            selected = false,
            onClick = {
                viewModel.undo()
            }
        )

        ToolItem(
            iconResourceId = R.drawable.ic_redo,
            contentDescription = "Redo last change",
            selected = false,
            onClick = {
                viewModel.redo()
            }
        )

        Box {
            ToolItem(
                iconResourceId = R.drawable.ic_menu,
                contentDescription = "Menu",
                selected = false,
                onClick = {
                    menuListVisible = true
                }
            )

            DropdownMenu(
                expanded = menuListVisible,
                containerColor = CatppuccinUI.DropDownMenuColor,
                onDismissRequest = { menuListVisible = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = "Save") },
                    onClick = {
                        saveISpriteDialogVisible = true
                    }
                )

                DropdownMenuItem(
                    text = { Text(text = "Load") },
                    onClick = {
                        loadISpriteDialogVisible = true
                    }
                )

                DropdownMenuItem(
                    text = { Text(text = "Export image") },
                    onClick = {
                        saveImageDialogVisible = true
                    }
                )

                DropdownMenuItem(
                    text = { Text(text = "Settings") },
                    onClick = {
                        // TODO: Handle settings
                    }
                )
            }
        }
    }
}


@Composable
fun ToolItem(
    @DrawableRes iconResourceId: Int,
    contentDescription: String?,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) CatppuccinUI.Foreground0Color else Color.Transparent
        ),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier.size(64.dp),
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(id = iconResourceId),
            contentDescription = contentDescription,
            tint = Color.Unspecified,
            modifier = Modifier.size(32.dp)
        )
    }
}