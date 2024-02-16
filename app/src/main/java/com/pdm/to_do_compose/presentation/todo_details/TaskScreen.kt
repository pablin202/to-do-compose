package com.pdm.to_do_compose.presentation.todo_details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.pdm.to_do_compose.presentation.todo_details.components.AppBarEditionTask
import com.pdm.to_do_compose.presentation.todo_details.components.AppBarEmpty
import com.pdm.to_do_compose.presentation.todo_details.components.AppBarNewTask
import com.pdm.to_do_compose.presentation.todo_details.components.PriorityDropDown
import com.pdm.to_do_compose.ui.theme.LARGE_PADDING
import com.pdm.to_do_compose.util.Action
import com.pdm.to_do_compose.R
import com.pdm.to_do_compose.domain.models.Priority
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    viewModel: TaskViewModel = hiltViewModel(),
    navigateToList: (Action) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = keyboardController) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiTaskEvent.NavigateBackEvent -> {
                    navigateToList(event.action)
                    keyboardController?.hide()
                }

                is UiTaskEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            event.value.asString(context)
                        )
                        keyboardController?.hide()
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (uiState.loading) {
                AppBarEmpty(onBackClicked = { navigateToList(Action.NO_ACTION) })
            } else {
                if (uiState.toDoTaskModel != null) {
                    if (uiState.toDoTaskModel!!.id == -1) {
                        AppBarNewTask(onBackClicked = { navigateToList(Action.NO_ACTION) }) {
                            viewModel.addTask()
                        }
                    } else {
                        AppBarEditionTask(
                            onBackClicked = { navigateToList(Action.NO_ACTION) },
                            onDeleteClicked = { viewModel.deleteTask() }) {
                            viewModel.updateTask()
                        }
                    }
                }
            }
        }
    ) { padding ->

        if (uiState.loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (uiState.toDoTaskModel != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = LARGE_PADDING, top = LARGE_PADDING),
                        value = uiState.toDoTaskModel!!.title,
                        label = { Text(text = stringResource(id = R.string.title)) },
                        singleLine = true,
                        onValueChange = {
                            viewModel.titleChange(it)
                        })

                    PriorityDropDown(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = LARGE_PADDING),
                        priority = Priority.entries[uiState.toDoTaskModel!!.priority],
                        onPrioritySelected = {
                            viewModel.priorityChange(it)
                        })

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = LARGE_PADDING),
                        label = { Text(text = stringResource(id = R.string.description)) },
                        value = uiState.toDoTaskModel!!.description,
                        onValueChange = {
                            viewModel.descriptionChange(it)
                        })
                }
            }
        }
    }
}