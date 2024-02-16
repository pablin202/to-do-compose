package com.pdm.to_do_compose.data.preferences

import android.content.SharedPreferences
import com.pdm.to_do_compose.domain.models.Priority
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

}
