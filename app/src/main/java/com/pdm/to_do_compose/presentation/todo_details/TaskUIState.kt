package com.pdm.to_do_compose.presentation.todo_details

import com.pdm.to_do_compose.domain.models.ToDoTaskModel

sealed class TaskUIState {
    data object Idle : TaskUIState()
    data object Loading : TaskUIState()
    data class Success(val toDoTaskModel: ToDoTaskModel) : TaskUIState()
    data class Error(val error: Throwable) : TaskUIState()
}