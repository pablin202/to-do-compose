package com.pdm.to_do_compose.domain.preferences

import com.pdm.to_do_compose.domain.models.Priority

interface Preferences {

    fun saveSort(priority: Priority)

    fun loadPriority(): Priority

    companion object {
        const val KEY_SORT = "sort"
    }
}