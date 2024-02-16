package com.pdm.to_do_compose.presentation.todo_list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pdm.to_do_compose.R
import androidx.hilt.navigation.compose.hiltViewModel
import com.pdm.to_do_compose.util.TestTags.ListScreen.FAB_BUTTON
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.pdm.to_do_compose.domain.models.Priority
import com.pdm.to_do_compose.domain.models.ToDoTaskModel
import com.pdm.to_do_compose.presentation.todo_list.components.DefaultListAppBar
import com.pdm.to_do_compose.presentation.todo_list.components.EmptyContent
import com.pdm.to_do_compose.presentation.todo_list.components.ListTasks
import com.pdm.to_do_compose.presentation.todo_list.components.SearchAppBar
import com.pdm.to_do_compose.ui.theme.ToDoComposeTheme
import com.pdm.to_do_compose.util.SearchAppBarState
import com.pdm.to_do_compose.util.UiText
import kotlinx.coroutines.launch

@Composable
fun ToDoListScreen(
    viewModel: ToDoListViewModel = hiltViewModel(), navigateToTask: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = keyboardController) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiTaskListEvent.ShowSnackbar -> {
                    scope.launch {
                        if (event.showUndoAction) {
                            val result = snackbarHostState.showSnackbar(
                                event.value.asString(context),
                                actionLabel = UiText.StringResource(R.string.undo)
                                    .asString(context),
                            )
                            when (result) {
                                SnackbarResult.ActionPerformed -> {
                                    viewModel.undoDeletedTask()
                                }

                                SnackbarResult.Dismissed -> {
                                    /* Handle snackbar dismissed */
                                }

                                else -> {}
                            }
                        } else {
                            snackbarHostState.showSnackbar(
                                event.value.asString(context)
                            )
                        }
                        keyboardController?.hide()
                    }
                }
            }
        }
    }

    ToDoListContent(state,
        snackbarHostState,
        { viewModel.showSearchBar() },
        { viewModel.changePriority(state.tasks, it) },
        { viewModel.deleteAllTask() },
        { viewModel.searchTextChange(it) },
        { viewModel.closeSearchBar() },
        { viewModel.selectTask(it) }) { taskId ->
        navigateToTask(taskId)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ToDoListContent(
    state: TasksUIState,
    snackbarHostState: SnackbarHostState,
    onOpenSearchBarClicked: () -> Unit,
    onPriorityChanged: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onTaskSelected: (ToDoTaskModel) -> Unit,
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

    val showAlertMessage = remember {
        mutableStateOf(false)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (state.appBarState == SearchAppBarState.CLOSED) {
                DefaultListAppBar({
                    onOpenSearchBarClicked()
                }, {
                    onPriorityChanged(it)
                }) {
                    showAlertMessage.value = true
                }
            } else {
                SearchAppBar(text = state.searchAppBarText,
                    onTextChange = { onTextChange(it) },
                    onCloseClicked = { onCloseClicked() })
                //onSearchClicked = { onSearchClicked() })
            }

        }, floatingActionButton = {
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
        }) { innerPadding ->

        if (showAlertMessage.value) {
            AlertDialog(onDismissRequest = { showAlertMessage.value = false },
                icon = {
                    Icon(
                        Icons.Filled.DeleteOutline,
                        contentDescription = stringResource(id = R.string.delete_icon)
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showAlertMessage.value = false
                            onDeleteAllClicked()
                        }
                    ) {
                        Text(stringResource(id = R.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showAlertMessage.value = false
                        }
                    ) {
                        Text(stringResource(id = R.string.dismiss))
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.alert_dialog_title))
                },
                text = {
                    Text(text = stringResource(id = R.string.alert_dialog_text))
                }
            )
        }

        if (state.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (state.tasks.isEmpty()) {
                EmptyContent()
            } else {
                ListTasks(
                    tasks = state.tasks,
                    innerPadding = innerPadding,
                    listState = listState,
                    onTaskSelected = {
                        onTaskSelected(it)
                        onFabClicked(it.id)
                    }
                )
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
private fun ToDoContentPreview() {
    ToDoListContent(
        TasksUIState(),
        SnackbarHostState(),
        {},
        {},
        {},
        {},
        {},
        {}) {}
}

@Preview(showBackground = true)
@Composable
private fun ToDoContentDarkModePreview() {
    ToDoComposeTheme(darkTheme = true) {
        ToDoListContent(
            TasksUIState(),
            SnackbarHostState(),
            {},
            {},
            {},
            {},
            {},
            {}) {

        }
    }
}