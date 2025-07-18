package com.olaz.instasprite.ui.screens.homescreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.data.database.AppDatabase
import com.olaz.instasprite.data.repository.ISpriteDatabaseRepository
import com.olaz.instasprite.data.repository.SortSettingRepository
import com.olaz.instasprite.data.repository.StorageLocationRepository
import com.olaz.instasprite.ui.components.composable.JumpToTopButton
import com.olaz.instasprite.ui.screens.homescreen.dialog.CreateCanvasDialog
import com.olaz.instasprite.ui.screens.homescreen.dialog.SelectSortOptionDialog
import com.olaz.instasprite.ui.theme.CatppuccinUI
import com.olaz.instasprite.ui.theme.InstaSpriteTheme
import com.olaz.instasprite.utils.UiUtils

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {
    UiUtils.SetStatusBarColor(CatppuccinUI.TopBarColor)
    UiUtils.SetNavigationBarColor(CatppuccinUI.BottomBarColor)

    val uiState by viewModel.uiState.collectAsState()

    if (uiState.showCreateCanvasDialog) {
        CreateCanvasDialog(
            onDismiss = {
                viewModel.toggleCreateCanvasDialog()
            },
        )
    }

    if (uiState.showSelectSortOptionDialog) {
        SelectSortOptionDialog(
            viewModel = viewModel,
            onDismiss = {
                viewModel.toggleSelectSortOptionDialog()
            },
        )
    }

    if (uiState.showImagePager) {
        ImagePagerOverlay(
            viewModel = viewModel,
            onDismiss = { lastSpriteSeen ->
                viewModel.toggleImagePager(null)
                viewModel.lastSpriteSeenInPager = lastSpriteSeen
            }
        )
    }

    val sprites by viewModel.sprites.collectAsState()

    val lazyListState = rememberLazyListState()
    val firstItemVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex > 0 } }

    Box {
        Scaffold(
            topBar = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(CatppuccinUI.TopBarColor)
                        .height(56.dp)
                ) {
                    Text(
                        text = "Home",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp)
                    )
                }

                AnimatedVisibility(
                    visible = uiState.showSearchBar,
                    enter = slideInVertically(initialOffsetY = { -it }),
                    exit = slideOutVertically(targetOffsetY = { -it }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    SearchBar(
                        viewModel = viewModel,
                    )
                }
            },
            bottomBar = {
                HomeBottomBar(
                    viewModel = viewModel,
                    lazyListState = lazyListState,
                    modifier = Modifier.height(56.dp)
                )
            },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(CatppuccinUI.BackgroundColorDarker)
                    .animateContentSize()
            ) {
                SpriteList(
                    viewModel = viewModel,
                    spritesWithMetaData = sprites,
                    lazyListState = lazyListState,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
        }

        AnimatedVisibility(
            visible = firstItemVisible,
            enter = scaleIn(),
            exit = scaleOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 64.dp) // TopBar (56dp) + 8dp spacing
        ) {
            JumpToTopButton(
                listState = lazyListState
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 21.dp)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center,
        ) {
            HomeFab(
                onClick = { viewModel.toggleCreateCanvasDialog() },
                lazyListState = lazyListState
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    val spriteDataRepository =
        ISpriteDatabaseRepository(database.spriteDataDao(), database.spriteMetaDataDao())
    val sortSettingRepository = SortSettingRepository(context)
    val storageLocationRepository = StorageLocationRepository(context)

    val viewModel = HomeScreenViewModel(
        spriteDatabaseRepository = spriteDataRepository,
        sortSettingRepository = sortSettingRepository,
        storageLocationRepository = storageLocationRepository
    )

    InstaSpriteTheme {
        HomeScreen(viewModel)
    }
}