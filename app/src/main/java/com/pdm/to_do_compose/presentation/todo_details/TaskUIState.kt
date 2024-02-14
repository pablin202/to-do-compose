package com.pdm.to_do_compose.presentation.todo_details

import com.pdm.to_do_compose.domain.models.ToDoTaskModel

data class TaskUIState(
    val loading: Boolean = false,
    val toDoTaskModel: ToDoTaskModel? = null,
    val error: Throwable? = null
)