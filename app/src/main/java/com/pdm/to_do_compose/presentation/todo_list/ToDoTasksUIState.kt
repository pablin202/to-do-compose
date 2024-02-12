package com.pdm.to_do_compose.presentation.todo_list

import com.pdm.to_do_compose.domain.models.ToDoTaskModel

sealed class ToDoTasksUIState {
    data object Idle : ToDoTasksUIState()
    data object Loading : ToDoTasksUIState()
    data class Success(val tasks: List<ToDoTaskModel>) : ToDoTasksUIState()
    data class Error(val error: Throwable) : ToDoTasksUIState()
}