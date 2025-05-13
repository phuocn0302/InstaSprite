package com.olaz.instasprite.ui.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.R
import com.olaz.instasprite.ui.theme.DrawingScreenColor

@Composable
fun ToolSelector(
    selectedTool: String,
    onToolSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ToolItem(
            iconResourceId = R.drawable.ic_pencil_tool,
            contentDescription = "Pencil Tool",
            selected = selectedTool == "pencil",
            onClick = { onToolSelected("pencil") }
        )

        ToolItem(
            iconResourceId = R.drawable.ic_eraser_tool,
            contentDescription = "Eraser Tool",
            selected = selectedTool == "eraser",
            onClick = { onToolSelected("eraser") }
        )

        ToolItem(
            iconResourceId = R.drawable.ic_fill_tool,
            contentDescription = "Fill Tool",
            selected = selectedTool == "fill",
            onClick = { onToolSelected("fill") }
        )

        ToolItem(
            iconResourceId = R.drawable.ic_eyedropper_tool,
            contentDescription = "Eyedropper Tool",
            selected = selectedTool == "eyedropper",
            onClick = { onToolSelected("eyedropper") }
        )
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
