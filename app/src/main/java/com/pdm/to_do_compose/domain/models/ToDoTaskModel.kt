package com.pdm.to_do_compose.domain.models

data class ToDoTaskModel(
    val id: Int,
    val title: String,
    val description: String,
    val priority: Priority
) {
    companion object {
        val EmptyModel = ToDoTaskModel(
            -1,
            "",
            "",
            Priority.NONE
        )
    }
}
