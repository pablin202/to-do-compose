package com.pdm.to_do_compose.presentation.todo_list

import com.pdm.to_do_compose.domain.models.Priority
import com.pdm.to_do_compose.domain.models.ToDoTaskModel
import com.pdm.to_do_compose.util.SearchAppBarState

data class TasksUIState(
    val loading: Boolean = false,
    val tasks: List<ToDoTaskModel> = emptyList(),
    val sort: Priority = Priority.NONE,
    val searchAppBarText: String = "",
    val appBarState: SearchAppBarState = SearchAppBarState.CLOSED,
    val error: Throwable? = null
)
