package com.olaz.instasprite.ui.screens.drawingscreen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.domain.tool.EraserTool
import com.olaz.instasprite.domain.tool.EyedropperTool
import com.olaz.instasprite.domain.tool.FillTool
import com.olaz.instasprite.domain.tool.MoveTool
import com.olaz.instasprite.domain.tool.PencilTool
import com.olaz.instasprite.ui.theme.DrawingScreenColor

@Composable
fun ToolSelector(
    modifier: Modifier = Modifier,
    viewModel: DrawingScreenViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val expanded by viewModel.isToolListExpanded.collectAsState()

    val tools = listOf(
        PencilTool,
        EraserTool,
        FillTool,
        EyedropperTool,
        MoveTool
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Box(
            modifier = modifier
                .size(64.dp)
                .background(
                    DrawingScreenColor.SelectedToolColor,
                    CircleShape
                )
                .clickable { viewModel.toggleToolList() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = uiState.selectedTool.icon),
                contentDescription = uiState.selectedTool.name,
                tint = Color.Unspecified,
                modifier = Modifier.size(32.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            containerColor = DrawingScreenColor.PaletteBarColor,
            onDismissRequest = { viewModel.toggleToolList() }
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
                    text = { Text(tool.name, color = Color.White) },
                    onClick = {
                        viewModel.selectTool(tool)
                        viewModel.toggleToolList()
                    }
                )
            }
        }
    }
}
