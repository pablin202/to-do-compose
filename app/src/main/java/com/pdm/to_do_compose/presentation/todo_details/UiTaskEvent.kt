package com.pdm.to_do_compose.presentation.todo_details

import com.pdm.to_do_compose.util.Action
import com.pdm.to_do_compose.util.UiText

sealed class UiTaskEvent {
    data class NavigateBackEvent(val action: Action) : UiTaskEvent()
    data class ShowSnackbar(val value: UiText) : UiTaskEvent()
}