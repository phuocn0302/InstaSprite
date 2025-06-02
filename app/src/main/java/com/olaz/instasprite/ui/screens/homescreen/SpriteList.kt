package com.olaz.instasprite.ui.screens.homescreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.model.ISpriteWithMetaData
import com.olaz.instasprite.data.model.SpriteMetaData
import com.olaz.instasprite.ui.components.composable.CanvasPreviewer
import com.olaz.instasprite.ui.theme.HomeScreenColor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SpriteList(
    spritesWithMetaData: List<ISpriteWithMetaData>,
    lazyListState: LazyListState = rememberLazyListState(),
    onSpriteClick: (ISpriteData) -> Unit = {}
) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.padding(8.dp)
    ) {
        items(
            items = spritesWithMetaData,
            key = { it.sprite.id }
        ) { (sprite, meta) ->
            SpriteCard(sprite = sprite, meta = meta) {
                onSpriteClick(sprite)
            }
        }
    }
}

@Composable
fun SpriteCard(
    sprite: ISpriteData,
    meta: SpriteMetaData?,
    onClick: () -> Unit
) {
    val dateCreated = remember(meta?.createdAt) {
        meta?.createdAt?.let { longToDateFormat(it) } ?: "Unknown"
    }
    val dateModified = remember(meta?.lastModifiedAt) {
        meta?.lastModifiedAt?.let { longToDateFormat(it) } ?: "Unknown"
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = HomeScreenColor.TopbarColor,
            contentColor = Color.White
        ),
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = "Sprite ID: ${sprite.id}",
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        CanvasPreviewer(spriteData = sprite, showBorder = true)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Date created: $dateCreated",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Text(
            text = "Last modified: $dateModified",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

fun longToDateFormat(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return formatter.format(Date(timestamp))
}