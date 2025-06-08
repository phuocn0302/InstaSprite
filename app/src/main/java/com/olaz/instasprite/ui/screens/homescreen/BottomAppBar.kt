package com.olaz.instasprite.ui.screens.homescreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.ui.theme.HomeScreenColor
import com.olaz.instasprite.utils.rememberBottomBarVisibleState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBottomBar(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    lazyListState: LazyListState
) {
    val items = listOf("Home", "Dummy", "Search", "Sort")

    val isBottomBarVisible by rememberBottomBarVisibleState(lazyListState)
    AnimatedVisibility(
        visible = isBottomBarVisible,
        enter = slideInVertically { fullHeight -> fullHeight },
        exit = slideOutVertically { fullHeight -> fullHeight }
    ) {

        BottomAppBar(
            containerColor = HomeScreenColor.BottombarColor,
            modifier = Modifier
                .height(56.dp)
                .clip(
                    BottomNavShape(
                        dockRadius = with(LocalDensity.current) { 40.dp.toPx() },
                    ),
                )
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        when (item) {
                            "Home" -> Icon(Icons.Default.Home, contentDescription = item)
                            "Search" -> Icon(Icons.Default.Search, contentDescription = item)
                            "Sort" -> Icon(Icons.Default.Menu, contentDescription = item)
                            else -> {}
                        }
                    },
                    selected = selectedItem == index,
                    onClick = { onItemSelected(index) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        indicatorColor = HomeScreenColor.BottombarColor,
                        unselectedIconColor = Color.White,
                        unselectedTextColor = Color.White,
                    ),
                    modifier = Modifier.clip(RoundedCornerShape(1.dp))
                )
            }
        }
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
            containerColor = HomeScreenColor.SelectedColor,
            contentColor = Color.White,
            modifier = Modifier.size(70.dp)
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Floating action button",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}