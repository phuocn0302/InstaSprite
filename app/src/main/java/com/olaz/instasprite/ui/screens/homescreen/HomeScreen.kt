package com.olaz.instasprite.ui.screens.homescreen

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.ui.screens.homescreen.dialog.CreateCanvasDialog
import com.olaz.instasprite.ui.theme.HomeScreenColor
import com.olaz.instasprite.utils.UiUtils
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {
    UiUtils.SetStatusBarColor(HomeScreenColor.TopbarColor)
    UiUtils.SetNavigationBarColor(HomeScreenColor.BottombarColor)

    val sprites by viewModel.sprites.collectAsState()
    val lazyListState = rememberLazyListState()
    var selectedItem by remember { mutableIntStateOf(0) }


    if (selectedItem == 1) {
        CreateCanvasDialog(
            onDismiss = { selectedItem = 0 },
        )
    }



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
                NavBar(lazyListState = lazyListState,
                    selectedItem = selectedItem,
                    onItemSelected = { selectedItem = it })
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
                        onSpriteClick = { sprite -> })
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
            FAB(lazyListState = lazyListState,  onClick = { selectedItem = 1 })
        }

    }
}

@Composable
fun NavBar(lazyListState: LazyListState,
           selectedItem: Int,
           onItemSelected: (Int) -> Unit,){
    val items = listOf("Home", "Dummy", "Search")

    val animationScale by animateFloatAsState(
        targetValue = if (lazyListState.isScrollingUp().value) 1f else 0f, label = "animationScale"
    )
    NavigationBar(
        containerColor = (HomeScreenColor.BottombarColor),
        modifier = Modifier
            .clip(
                BottomNavShape(
                    dockRadius = with(LocalDensity.current) { 45.dp.toPx() },
                ),
            )
            .height( 80.dp * animationScale),
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    when (item) {
                        "Home" -> Icon(
                            Icons.Default.Home, contentDescription = item
                        )

                        "Search" -> Icon(
                            Icons.Default.Search, contentDescription = item
                        )
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

@Composable
fun FAB(
    lazyListState: LazyListState,
    onClick: ()->Unit){
    var isScrollingUp by remember { mutableStateOf(true) }


    val animationScale by animateFloatAsState(
        targetValue = if (lazyListState.isScrollingUp().value) 1f else 0f, label = "animationScale"
    )

    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        containerColor = HomeScreenColor.SelectedColor,
        contentColor = Color.White,
        modifier = Modifier.size((75.dp) * animationScale)
    ) {
        Icon(
            Icons.Filled.Add,
            "Floating action button",
            modifier = Modifier.size(30.dp * animationScale)
        )
    }
}
@OptIn(FlowPreview::class)
@Composable
fun LazyListState.isScrollingUp(): androidx.compose.runtime.State<Boolean> {
    return produceState(initialValue = true) {
        var lastIndex = 0
        var lastScroll = Int.MAX_VALUE
        snapshotFlow {
            firstVisibleItemIndex to firstVisibleItemScrollOffset
        }
            .distinctUntilChanged()
            .collect { (currentIndex, currentScroll) ->
                if (currentIndex != lastIndex || currentScroll != lastScroll) {
                    value = currentIndex < lastIndex ||
                            (currentIndex == lastIndex && currentScroll < lastScroll)
                    lastIndex = currentIndex
                    lastScroll = currentScroll
                }
            }
    }
}