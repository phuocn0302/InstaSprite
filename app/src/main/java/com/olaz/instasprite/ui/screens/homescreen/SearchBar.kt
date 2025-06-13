package com.olaz.instasprite.ui.screens.homescreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.olaz.instasprite.ui.theme.HomeScreenColor

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel
) {
    TextField(
        value = viewModel.searchQuery,
        onValueChange = viewModel::updateSearchQuery,
        placeholder = { Text("Search sprites") },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
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