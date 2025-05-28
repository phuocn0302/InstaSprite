package com.olaz.instasprite.domain.canvashistory

import java.util.ArrayDeque

class CanvasHistoryManager<T> {
    private val undoStack: ArrayDeque<T> = ArrayDeque()
    private val redoStack: ArrayDeque<T> = ArrayDeque()

    fun saveState(state: T) {
        undoStack.addLast(state)
        redoStack.clear()
    }

    fun undo(): T? {
        if (undoStack.size > 1) {
            val current = undoStack.removeLast()
            redoStack.addLast(current)
            return undoStack.last()
        }
        return null
    }

    fun redo(): T? {
        if (redoStack.isNotEmpty()) {
            val redoState = redoStack.removeLast()
            undoStack.addLast(redoState)
            return redoState
        }
        return null
    }

    fun getUndoStack(): ArrayDeque<T> {
        return undoStack
    }

    fun getRedoStack(): ArrayDeque<T> {
        return redoStack
    }
}
