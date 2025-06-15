package com.olaz.instasprite.ui.screens.homescreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.R
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.model.ISpriteWithMetaData
import com.olaz.instasprite.data.model.SpriteMetaData
import com.olaz.instasprite.ui.components.composable.CanvasPreviewer
import com.olaz.instasprite.ui.screens.homescreen.dialog.DeleteSpriteConfirmDialog
import com.olaz.instasprite.ui.screens.homescreen.dialog.RenameDialog
import com.olaz.instasprite.ui.theme.HomeScreenColor
import kotlinx.coroutines.launch


@Composable
fun SpriteList(
    viewModel: HomeScreenViewModel,
    spritesWithMetaData: List<ISpriteWithMetaData>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val order = remember(viewModel.spriteListOrder) { viewModel.spriteListOrder }

    val filteredSprites = remember(viewModel.searchQuery, spritesWithMetaData) {
        spritesWithMetaData.filter {
            val name = it.meta?.spriteName ?: ""
            name.contains(viewModel.searchQuery, ignoreCase = true)
        }
    }

    val sortedSprites = remember(order, filteredSprites) {
        when (order) {
            SpriteListOrder.Name ->
                filteredSprites.sortedBy { it.meta?.spriteName?.lowercase() ?: "" }

            SpriteListOrder.NameDesc ->
                filteredSprites.sortedByDescending { it.meta?.spriteName?.lowercase() ?: "" }

            SpriteListOrder.DateCreated ->
                filteredSprites.sortedBy { it.meta?.createdAt ?: 0L }

            SpriteListOrder.DateCreatedDesc ->
                filteredSprites.sortedByDescending { it.meta?.createdAt ?: 0L }

            SpriteListOrder.LastModified ->
                filteredSprites.sortedBy { it.meta?.lastModifiedAt ?: 0L }

            SpriteListOrder.LastModifiedDesc ->
                filteredSprites.sortedByDescending { it.meta?.lastModifiedAt ?: 0L }
        }
    }
    viewModel.spriteList = sortedSprites

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(spritesWithMetaData, sortedSprites) {
        viewModel.lastEditedSpriteId?.let { editedId ->
            val index = sortedSprites.indexOfFirst { it.sprite.id == editedId }
            if (index != -1) {
                coroutineScope.launch {
                    lazyListState.animateScrollToItem(index)
                }
            }
        }
        viewModel.lastEditedSpriteId = null
    }

    LaunchedEffect(viewModel.searchQuery) {
        coroutineScope.launch {
            lazyListState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(viewModel.lastSpriteSeenInPager) {
        viewModel.lastSpriteSeenInPager?.let { sprite ->
            val index = sortedSprites.indexOfFirst { it.sprite.id == sprite.id }
            if (index != -1) {
                coroutineScope.launch {
                    lazyListState.animateScrollToItem(index)
                }
            }
            viewModel.lastSpriteSeenInPager = null
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier
    ) {
        items(
            items = sortedSprites,
            key = { it.sprite.id }
        ) { (sprite, meta) ->
            SpriteCard(
                sprite = sprite,
                meta = meta,
                viewModel = viewModel,
                modifier = Modifier.animateItem()
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpriteCard(
    sprite: ISpriteData,
    meta: SpriteMetaData?,
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var showDropdown by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(true) }

    when {
        showRenameDialog -> RenameDialog(
            viewModel = viewModel,
            spriteId = sprite.id,
            onDismiss = { showRenameDialog = false },
        )

        showDeleteDialog -> DeleteSpriteConfirmDialog(
            spriteName = meta?.spriteName ?: "Untitled",
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                coroutineScope.launch {
                    isVisible = false
                    viewModel.deleteSpriteByIdDelay(sprite.id, 0)
                }
                showDeleteDialog = false
            }
        )
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically(),
        exit = shrinkVertically(),
        modifier = modifier
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = HomeScreenColor.TopbarColor,
                contentColor = Color.White
            ),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .combinedClickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {},
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
                            viewModel.openDrawingActivity(context, sprite)
                            showDropdown = false
                        },
                        onRename = {
                            showRenameDialog = true
                        }
                    )
                }
            }

            CanvasPreviewer(
                spriteData = sprite,
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(12.dp)),
                onClick = { viewModel.toggleImagePager(sprite) }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SpriteDropdownMenu(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {},
    onRename: () -> Unit = {},
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        containerColor = HomeScreenColor.TopbarColor,
        modifier = modifier
    ) {
        DropdownMenuItem(
            text = { Text("Rename", color = Color.White) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Rename",
                    tint = Color.White
                )
            },
            onClick = onRename,
        )
        DropdownMenuItem(
            text = { Text("Edit", color = Color.White) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.White
                )
            },
            onClick = onEdit,
        )
        DropdownMenuItem(
            text = { Text("Delete", color = Color.White) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            },
            onClick = onDelete,
        )
    }
}