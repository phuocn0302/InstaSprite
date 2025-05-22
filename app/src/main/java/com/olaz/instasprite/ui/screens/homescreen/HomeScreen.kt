package com.olaz.instasprite.ui.screens.homescreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.ui.screens.drawingscreen.DrawingScreen
import com.olaz.instasprite.ui.theme.HomeScreenColor
import com.olaz.instasprite.utils.UiUtils

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun HomeScreen() {
    UiUtils.SetStatusBarColor(HomeScreenColor.TopbarColor)
    UiUtils.SetNavigationBarColor(HomeScreenColor.BottombarColor)

    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Create", "Search")

    var showDrawingScreen by remember { mutableStateOf(false) }

    var canvasWidth by remember { mutableIntStateOf(16) }
    var canvasHeight by remember { mutableIntStateOf(16) }


    if (selectedItem == 1) {
        CreateCanvasDialog(
            onDismiss = { selectedItem = 0 },
            onCreateCanvas = { width, height ->
                canvasWidth = width
                canvasHeight = height
                showDrawingScreen = true
                selectedItem = 0
            }
        )
    }

    if (showDrawingScreen) {
        DrawingScreen(canvasWidth, canvasHeight)
    } else {
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
                NavigationBar(
                    containerColor = (HomeScreenColor.BottombarColor),
                    modifier = Modifier
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {
                                when (item) {
                                    "Home" -> Icon(Icons.Default.Home, contentDescription = item)
                                    "Create" -> Icon(Icons.Default.Add, contentDescription = item)
                                    "Search" -> Icon(
                                        Icons.Default.Search,
                                        contentDescription = item
                                    )
                                }
                            },
                            label = { Text(item) },
                            selected = selectedItem == index,
                            onClick = { selectedItem = index },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = HomeScreenColor.SelectedColor,
                                unselectedIconColor = Color.White,
                                unselectedTextColor = Color.White,
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(1.dp))
                        )
                    }

                }
            }
        ) {
            // WIP
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(HomeScreenColor.BackgroundColor)
            )

        }
    }
}