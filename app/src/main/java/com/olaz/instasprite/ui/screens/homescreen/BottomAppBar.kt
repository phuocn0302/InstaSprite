package com.olaz.instasprite.ui.screens.homescreen

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.R
import com.olaz.instasprite.ui.theme.CatppuccinUI
import com.olaz.instasprite.utils.rememberBottomBarVisibleState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBottomBar(
    viewModel: HomeScreenViewModel,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {
    val isBottomBarVisible by rememberBottomBarVisibleState(lazyListState)
    AnimatedVisibility(
        visible = isBottomBarVisible,
        enter = slideInVertically { fullHeight -> fullHeight },
        exit = slideOutVertically { fullHeight -> fullHeight }
    ) {

        BottomAppBar(
            containerColor = CatppuccinUI.BottomBarColor,
            modifier = modifier
                .clip(
                    BottomNavShape(
                        dockRadius = with(LocalDensity.current) { 40.dp.toPx() },
                    ),
                )
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 3.dp, vertical = 2.dp)
                    .weight(1f)
            ) {
                BottomBarItem(
                    imageVector = Icons.Default.Menu,
                    onClick = {
                        // TODO
                    },
                    iconTint = CatppuccinUI.TextColorLight
                )
            }

            // for FAB cutout
            Spacer(modifier = Modifier.weight(0.5f))

            Row(
                modifier = Modifier
                    .padding(horizontal = 3.dp, vertical = 2.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                BottomBarItem(
                    imageVector = Icons.Default.Search,
                    onClick = {
                        viewModel.toggleSearchBar()
                    },
                    iconTint = CatppuccinUI.TextColorLight
                )
                BottomBarItem(
                    iconResourceId = R.drawable.ic_sort,
                    onClick = {
                        viewModel.toggleSelectSortOptionDialog()
                    },
                    iconTint = CatppuccinUI.TextColorLight
                )
            }
        }
    }
}

@Composable
fun BottomBarItem(
    imageVector: ImageVector,
    onClick: () -> Unit,
    size: Dp = 28.dp,
    iconTint: Color = Color.Unspecified
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 2.dp)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "Floating action button",
            tint = iconTint,
            modifier = Modifier.size(size)
        )
    }
}

@Composable
fun BottomBarItem(
    @DrawableRes iconResourceId: Int,
    onClick: () -> Unit,
    size: Dp = 28.dp,
    iconTint: Color = Color.Unspecified
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 2.dp)
    ) {
        Icon(
            painter = painterResource(id = iconResourceId),
            contentDescription = "Floating action button",
            tint = iconTint,
            modifier = Modifier.size(size)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFab(
    onClick: () -> Unit,
    lazyListState: LazyListState
) {
    val isBottomBarVisible by rememberBottomBarVisibleState(lazyListState)
    AnimatedVisibility(
        visible = isBottomBarVisible,
        enter = fadeIn() + slideInVertically { fullHeight -> fullHeight } + scaleIn(),
        exit = fadeOut() + slideOutVertically { fullHeight -> fullHeight } + scaleOut()
    ) {
        FloatingActionButton(
            onClick = onClick,
            shape = CircleShape,
            containerColor = CatppuccinUI.SelectedColor,
            modifier = Modifier.size(70.dp)
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Floating action button",
                tint = CatppuccinUI.TextColorDark,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}