package com.pdm.to_do_compose.data.preferences

import android.content.SharedPreferences
import com.pdm.to_do_compose.domain.models.Priority
import com.pdm.to_do_compose.domain.models.ToDoTaskModel
import com.pdm.to_do_compose.domain.models.toEnumValue
import com.pdm.to_do_compose.domain.preferences.Preferences

class DefaultPreferences(private val sharedPref: SharedPreferences) : Preferences {
    override fun saveSort(priority: Priority) {
        sharedPref.edit()
            .putString(Preferences.KEY_SORT, priority.name)
            .apply()
    }

    override fun loadPriority(): Priority {
        val priority = sharedPref.getString(Preferences.KEY_SORT, Priority.NONE.name)
        return priority?.toEnumValue() ?: Priority.NONE
    }

    override fun saveTask(toDoTaskModel: ToDoTaskModel) {
        sharedPref.edit()
            .putInt(Preferences.KEY_TASK_ID, toDoTaskModel.id)
            .apply()

        sharedPref.edit()
            .putString(Preferences.KEY_TASK_DESC, toDoTaskModel.description)
            .apply()

        sharedPref.edit()
            .putString(Preferences.KEY_TASK_TITLE, toDoTaskModel.title)
            .apply()

        sharedPref.edit()
            .putInt(Preferences.KEY_TASK_PRIORITY, toDoTaskModel.priority)
            .apply()
    }

    override fun getLastTask(): ToDoTaskModel {
        return try {
            val id = sharedPref.getInt(Preferences.KEY_TASK_ID, -1)
            val priority = sharedPref.getInt(Preferences.KEY_TASK_PRIORITY, -1)
            val desc = sharedPref.getString(Preferences.KEY_TASK_DESC, "")
            val title = sharedPref.getString(Preferences.KEY_TASK_TITLE, "")
            ToDoTaskModel(id, title!!, desc!!, priority)
        } catch (ex: Exception) {
            ToDoTaskModel.EmptyModel
        }
    }
}
