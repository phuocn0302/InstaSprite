package com.olaz.instasprite.ui.screens.homescreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.R
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.model.ISpriteWithMetaData
import com.olaz.instasprite.data.model.SpriteMetaData
import com.olaz.instasprite.ui.components.composable.CanvasPreviewer
import com.olaz.instasprite.ui.screens.homescreen.dialog.DeleteSpriteConfirmDialog
import com.olaz.instasprite.ui.theme.HomeScreenColor
import kotlinx.coroutines.launch


@Composable
fun SpriteList(
    spritesWithMetaData: List<ISpriteWithMetaData>,
    lazyListState: LazyListState = rememberLazyListState(),
    onSpriteClick: (ISpriteData) -> Unit = {},
    onSpriteDelete: suspend (ISpriteData) -> Unit = {},
    onSpriteEdit: (ISpriteData) -> Unit = {}
) {
    // for deleting sprite with animation
    val hiddenSprites = remember { mutableStateMapOf<String, Boolean>() }
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.padding(8.dp)
    ) {
        items(
            items = spritesWithMetaData,
            key = { it.sprite.id }
        ) { (sprite, meta) ->
            val isVisible = hiddenSprites[sprite.id] != true

            AnimatedVisibility(
                visible = isVisible,
                exit = shrinkVertically()
            ) {
                SpriteCard(
                    sprite = sprite,
                    meta = meta,
                    onClick = { onSpriteClick(sprite) },
                    onDelete = {
                        hiddenSprites[sprite.id] = true
                        coroutineScope.launch {
                            onSpriteDelete(sprite)
                        }
                    },
                    onEdit = { onSpriteEdit(sprite) }
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpriteCard(
    sprite: ISpriteData,
    meta: SpriteMetaData?,
    onClick: () -> Unit,
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    var showDropdown by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        DeleteSpriteConfirmDialog(
            spriteName = meta?.spriteName ?: "Untitled",
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                onDelete()
                showDeleteDialog = false
            }
        )
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = HomeScreenColor.TopbarColor,
            contentColor = Color.White
        ),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = { showDropdown = true }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = meta?.spriteName ?: "Untitled",
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Box {
                IconButton(
                    onClick = { showDropdown = true },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_three_dots),
                        contentDescription = "Options",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                }

                SpriteDropdownMenu(
                    expanded = showDropdown,
                    onDismissRequest = { showDropdown = false },
                    onDelete = {
                        showDeleteDialog = true
                        showDropdown = false
                    },
                    onEdit = {
                        onEdit()
                        showDropdown = false
                    }
                )
            }
        }

        CanvasPreviewer(
            spriteData = sprite,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SpriteDropdownMenu(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {},
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        containerColor = HomeScreenColor.TopbarColor,
        modifier = modifier
    ) {
        DropdownMenuItem(
            text = { Text("Edit", color = Color.White) },
            onClick = onEdit,
        )
        DropdownMenuItem(
            text = { Text("Delete", color = Color.White) },
            onClick = onDelete,
        )
    }
}