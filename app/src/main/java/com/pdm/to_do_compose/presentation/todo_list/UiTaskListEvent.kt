package com.pdm.to_do_compose.presentation.todo_list

import com.pdm.to_do_compose.util.UiText

sealed class UiTaskListEvent {
    data class ShowSnackbar(val value: UiText) : UiTaskListEvent()
}