package com.pdm.to_do_compose.domain.models

import androidx.compose.ui.graphics.Color
import com.pdm.to_do_compose.ui.theme.HighPriorityColor
import com.pdm.to_do_compose.ui.theme.LowPriorityColor
import com.pdm.to_do_compose.ui.theme.MediumPriorityColor
import com.pdm.to_do_compose.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    NONE(NonePriorityColor),
    LOW(LowPriorityColor),
    MEDIUM(MediumPriorityColor),
    HIGH(HighPriorityColor),
}

fun String.toEnumValue(): Priority {
    return when (this) {
        Priority.NONE.name -> {
            Priority.NONE
        }

        Priority.LOW.name -> {
            Priority.LOW
        }

        Priority.MEDIUM.name -> {
            Priority.MEDIUM
        }

        Priority.HIGH.name -> {
            Priority.HIGH
        }

        else -> {
            Priority.NONE
        }
    }
}