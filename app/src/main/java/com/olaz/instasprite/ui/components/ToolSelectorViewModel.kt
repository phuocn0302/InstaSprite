package com.olaz.instasprite.ui.components

import androidx.lifecycle.ViewModel
import com.olaz.instasprite.domain.tool.PencilTool
import com.olaz.instasprite.domain.tool.Tool
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ToolSelectorState(
    val selectedTool: Tool = PencilTool,
)

class ToolSelectorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        ToolSelectorState(
            selectedTool = PencilTool
        )
    )
    val uiState: StateFlow<ToolSelectorState> = _uiState.asStateFlow()

    fun selectTool(tool: Tool) {
        _uiState.value = _uiState.value.copy(selectedTool = tool)
    }
}