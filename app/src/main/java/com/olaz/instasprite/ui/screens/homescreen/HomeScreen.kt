package com.olaz.instasprite.ui.screens.homescreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.ui.screens.homescreen.dialog.CreateCanvasDialog
import com.olaz.instasprite.ui.theme.HomeScreenColor
import com.olaz.instasprite.utils.UiUtils

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {
    UiUtils.SetStatusBarColor(HomeScreenColor.TopbarColor)
    UiUtils.SetNavigationBarColor(HomeScreenColor.BottombarColor)
    val context = LocalContext.current

    val sprites by viewModel.sprites.collectAsState()

    var selectedItem by remember { mutableIntStateOf(0) }

    if (selectedItem == 1) {
        CreateCanvasDialog(
            onDismiss = { selectedItem = 0 },
        )
    }

    val lazyListState = LazyListState()

    Box {
        Scaffold(
            topBar = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(HomeScreenColor.TopbarColor)
                        .height(50.dp)
                ) {
                    Text(
                        text = "Home",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp)
                    )
                    Text(
                        text = "Feed",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp)
                    )
                }
            },
            bottomBar = {
                HomeBottomBar(
                    selectedItem = selectedItem,
                    onItemSelected = { selectedItem = it },
                    lazyListState = lazyListState
                )
            },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(HomeScreenColor.BackgroundColor)
            ) {
                Box(
                    modifier = Modifier.padding(10.dp, 50.dp, 10.dp, 0.dp)
                ) {
                    SpriteList(
                        spritesWithMetaData = sprites,
                        lazyListState = lazyListState,
                        onSpriteClick = { sprite -> },
                        onSpriteDelete = { sprite ->
                            viewModel.deleteSpriteByIdDelay(sprite.id, 300)
                        },
                        onSpriteEdit = { sprite -> viewModel.openDrawingActivity(context, sprite) }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center,
        ) {
            HomeFab(
                onClick = { selectedItem = 1 },
                lazyListState = lazyListState
            )
        }
    }
}