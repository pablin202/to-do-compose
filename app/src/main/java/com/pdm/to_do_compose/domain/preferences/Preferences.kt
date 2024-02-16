package com.pdm.to_do_compose.domain.preferences

import com.pdm.to_do_compose.domain.models.Priority
import com.pdm.to_do_compose.domain.models.ToDoTaskModel

interface Preferences {

    fun saveSort(priority: Priority)

    fun loadPriority(): Priority

    fun saveTask(toDoTaskModel: ToDoTaskModel)

    fun getLastTask(): ToDoTaskModel

    companion object {
        const val KEY_SORT = "sort"
        const val KEY_TASK_ID = "task_id"
        const val KEY_TASK_TITLE = "task_title"
        const val KEY_TASK_DESC = "task_desc"
        const val KEY_TASK_PRIORITY = "task_priority"
    }
}