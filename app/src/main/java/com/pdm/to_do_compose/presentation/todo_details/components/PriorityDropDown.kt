package com.pdm.to_do_compose.presentation.todo_details.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pdm.to_do_compose.domain.models.Priority
import com.pdm.to_do_compose.ui.theme.PRIORITY_INDICATOR_SIZE
import com.pdm.to_do_compose.R
import com.pdm.to_do_compose.presentation.todo_list.components.PriorityItem
import com.pdm.to_do_compose.ui.theme.LARGE_PADDING

@Composable
fun PriorityDropDown(
    priority: Priority,
    modifier: Modifier,
    onPrioritySelected: (Priority) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val angle: Float by animateFloatAsState(
        targetValue =
        if (expanded) 180f else 0f, label = ""
    )

    Row(
        modifier = modifier
            .height(60.dp)
            .clickable { expanded = true }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier
            .size(PRIORITY_INDICATOR_SIZE)
            .weight(1f), onDraw = {
            drawCircle(color = priority.color)
        })
        Text(
            text = priority.name, style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .weight(8f)
                .padding(start = LARGE_PADDING)
        )
        IconButton(
            modifier = Modifier
                .alpha(0.5f)
                .rotate(angle)
                .weight(1.5f),
            onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(id = R.string.drop_down_arrow_icon)
            )
        }

        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(text = { PriorityItem(priority = Priority.LOW) }, onClick = {
                expanded = false
                onPrioritySelected(Priority.LOW)
            })
            DropdownMenuItem(text = { PriorityItem(priority = Priority.MEDIUM) }, onClick = {
                expanded = false
                onPrioritySelected(Priority.MEDIUM)
            })
            DropdownMenuItem(text = { PriorityItem(priority = Priority.HIGH) }, onClick = {
                expanded = false
                onPrioritySelected(Priority.HIGH)
            })
        }
    }
}

@Preview
@Composable
fun PriorityDropDownPreview() {
    PriorityDropDown(
        priority = Priority.LOW,
        modifier = Modifier.fillMaxWidth(),
        onPrioritySelected = {})
}

@Preview
@Composable
fun PriorityHighDropDownPreview() {
    PriorityDropDown(
        priority = Priority.HIGH,
        modifier = Modifier.fillMaxWidth(),
        onPrioritySelected = {})
}