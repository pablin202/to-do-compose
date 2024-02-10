package com.pdm.to_do_compose.presentation.todo_list.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.pdm.to_do_compose.R
import com.pdm.to_do_compose.domain.models.Priority
import com.pdm.to_do_compose.ui.theme.LARGE_PADDING
import com.pdm.to_do_compose.ui.theme.PRIORITY_INDICATOR_SIZE
import com.pdm.to_do_compose.ui.theme.ToDoComposeTheme
import com.pdm.to_do_compose.ui.theme.Typography
import com.pdm.to_do_compose.util.TestTags
import com.pdm.to_do_compose.util.TestTags.ListScreen.DELETE_ALL_BUTTON_ACTION
import com.pdm.to_do_compose.util.TestTags.ListScreen.SEARCH_BUTTON_ACTION
import com.pdm.to_do_compose.util.TestTags.ListScreen.SORT_BUTTON_ACTION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onPriorityChanged: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.testTag(TestTags.ListScreen.DEFAULT_APP_BAR),
        title = { Text(text = stringResource(id = R.string.tasks)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        actions = {
            ListAppBarActions({ onSearchClicked() }, { onPriorityChanged(it) }) {
                onDeleteAllClicked()
            }
        }
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onPriorityChanged: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit
) {
    SearchAction(onSearchClicked)
    SortAction(onPriorityChanged)
    DeleteAllAction(onDeleteAllClicked)
}

@Composable
fun SearchAction(
    onSearchClicked: () -> Unit
) {
    IconButton(
        onClick = { onSearchClicked() },
        modifier = Modifier.testTag(SEARCH_BUTTON_ACTION)
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(id = R.string.search_tasks),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun DeleteAllAction(
    onDeleteAllClicked: () -> Unit
) {
    IconButton(
        onClick = { onDeleteAllClicked() },
        modifier = Modifier.testTag(DELETE_ALL_BUTTON_ACTION)
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete_all),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun SortAction(
    onPriorityChanged: (Priority) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    IconButton(
        onClick = { expanded = true },
        modifier = Modifier.testTag(SORT_BUTTON_ACTION)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter_list),
            contentDescription = stringResource(id = R.string.sort_tasks),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {

            DropdownMenuItem(text = { PriorityItem(priority = Priority.LOW) }, onClick = {
                expanded = false
                onPriorityChanged(Priority.LOW)
            })

            DropdownMenuItem(text = { PriorityItem(priority = Priority.HIGH) }, onClick = {
                expanded = false
                onPriorityChanged(Priority.HIGH)
            })

            DropdownMenuItem(text = { PriorityItem(priority = Priority.NONE) }, onClick = {
                expanded = false
                onPriorityChanged(Priority.NONE)
            })
        }
    }
}

@Composable
fun PriorityItem(
    priority: Priority
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(PRIORITY_INDICATOR_SIZE)) {
            drawCircle(color = priority.color)
        }
        Text(
            modifier = Modifier.padding(start = LARGE_PADDING),
            text = priority.name,
            style = Typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun DefaultListAppBarPreview() {
    DefaultListAppBar({},{}) {

    }
}

@Preview
@Composable
private fun DefaultListAppBarDarkModePreview() {
    ToDoComposeTheme(darkTheme = true) {
        DefaultListAppBar({},{}) {

        }
    }
}

@Preview
@Composable
fun HighPriorityItemPreview() {
    PriorityItem(priority = Priority.HIGH)
}

@Preview
@Composable
fun MediumPriorityItemPreview() {
    PriorityItem(priority = Priority.MEDIUM)
}

@Preview
@Composable
fun LowPriorityItemPreview() {
    PriorityItem(priority = Priority.LOW)
}

@Preview
@Composable
fun NonePriorityItemPreview() {
    PriorityItem(priority = Priority.NONE)
}