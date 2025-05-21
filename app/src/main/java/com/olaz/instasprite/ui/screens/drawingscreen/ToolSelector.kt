package com.olaz.instasprite.ui.screens.drawingscreen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    val tools = listOf(
        PencilTool,
        EraserTool,
        FillTool,
        EyedropperTool,
        MoveTool
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tools.forEach { tool ->
            ToolItem(
                iconResourceId = tool.icon,
                contentDescription = tool.name,
                selected = tool == uiState.selectedTool,
                onClick = { viewModel.selectTool(tool) }
            )
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
    Box(
        modifier = modifier
            .size(64.dp)
            .background(
                if (selected) DrawingScreenColor.SelectedToolColor else Color.Transparent,
                CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconResourceId),
            contentDescription = contentDescription,
            tint = Color.Unspecified,
            modifier = Modifier.size(32.dp)
        )
    }
}
