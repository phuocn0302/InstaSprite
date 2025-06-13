package com.olaz.instasprite.ui.screens.homescreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.olaz.instasprite.ui.theme.HomeScreenColor

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var isFocused by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler(enabled = isFocused) {
        if (viewModel.searchQuery.isEmpty()) {
            viewModel.toggleSearchBar()
        }
        focusManager.clearFocus()
    }

    TextField(
        value = viewModel.searchQuery,
        onValueChange = viewModel::updateSearchQuery,
        placeholder = { Text("Search sprites") },
        singleLine = true,
        trailingIcon = {
            IconButton(
                onClick = {
                    if (viewModel.searchQuery.isEmpty()) {
                        viewModel.toggleSearchBar()
                    }
                    viewModel.updateSearchQuery("")
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Dismiss",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged {
                isFocused = it.isFocused
            },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = HomeScreenColor.TopbarColor,
            disabledContainerColor = HomeScreenColor.TopbarColor,
            unfocusedContainerColor = HomeScreenColor.TopbarColor,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            unfocusedPlaceholderColor = Color.White,
            focusedPlaceholderColor = Color.White
        ),
    )

}