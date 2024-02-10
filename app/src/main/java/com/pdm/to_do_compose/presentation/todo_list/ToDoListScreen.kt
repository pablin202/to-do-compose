package com.pdm.to_do_compose.presentation.todo_list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pdm.to_do_compose.R
import androidx.hilt.navigation.compose.hiltViewModel
import com.pdm.to_do_compose.domain.models.ToDoTaskModel
import com.pdm.to_do_compose.util.TestTags.ListScreen.FAB_BUTTON
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.unit.dp
import com.pdm.to_do_compose.domain.models.Priority
import com.pdm.to_do_compose.presentation.todo_list.components.DefaultListAppBar
import com.pdm.to_do_compose.ui.theme.ToDoComposeTheme
import com.pdm.to_do_compose.util.TestTags.ListScreen.TASKS_LIST

@Composable
fun ToDoListScreen(
    viewModel: ToDoListViewModel = hiltViewModel(),
    navigateToTask: (Int) -> Unit
) {
    val tasks by viewModel.allTasks.collectAsState()
    ToDoListContent(tasks, {}, {}, {}) { taskId ->
        navigateToTask(taskId)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ToDoListContent(
    task: List<ToDoTaskModel> = emptyList(),
    onSearchClicked: () -> Unit,
    onPriorityChanged: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit,
    onFabClicked: (Int) -> Unit
) {

    val listState = rememberLazyListState()
    // The FAB is initially expanded. Once the first visible item is past the first item we
    // collapse the FAB. We use a remembered derived state to minimize unnecessary compositions.
    val expandedFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    Scaffold(
        topBar = {
            DefaultListAppBar({
                onSearchClicked()
            }, {
                onPriorityChanged(it)
            }) {
                onDeleteAllClicked()
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier.testTag(FAB_BUTTON),
                expanded = expandedFab,
                onClick = { onFabClicked(-1) },
                icon = {
                    Icon(
                        Icons.Outlined.Add, stringResource(id = R.string.add_new_task_button),
                    )
                },
                text = { Text(text = stringResource(id = R.string.add_new_task)) },
            )
        }) {
        LazyColumn(
            state = listState, modifier = Modifier
                .fillMaxSize()
                .testTag(TASKS_LIST)
        ) {
            for (index in 0 until 100) {
                item {
                    Text(
                        text = "List item - $index",
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ToDoContentPreview() {
    ToDoListContent(emptyList(), {}, {}, {}) {}
}

@Preview(showBackground = true)
@Composable
private fun ToDoContentDarkModePreview() {
    ToDoComposeTheme(darkTheme = true) {
        ToDoListContent(emptyList(), {}, {}, {}) {}
    }
}