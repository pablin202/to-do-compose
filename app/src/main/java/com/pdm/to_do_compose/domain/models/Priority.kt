package com.pdm.to_do_compose.domain.models

import androidx.compose.ui.graphics.Color
import com.pdm.to_do_compose.ui.theme.HighPriorityColor
import com.pdm.to_do_compose.ui.theme.LowPriorityColor
import com.pdm.to_do_compose.ui.theme.MediumPriorityColor
import com.pdm.to_do_compose.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}