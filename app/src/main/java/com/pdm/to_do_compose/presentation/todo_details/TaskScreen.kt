package com.pdm.to_do_compose.presentation.todo_details

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.pdm.to_do_compose.presentation.todo_details.components.AppBarEditionTask
import com.pdm.to_do_compose.presentation.todo_details.components.AppBarEmpty
import com.pdm.to_do_compose.presentation.todo_details.components.AppBarNewTask
import com.pdm.to_do_compose.util.Action

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    viewModel: TaskViewModel = hiltViewModel(),
    navigateToList: (Action) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            when (uiState) {
                is TaskUIState.Success -> {
                    if ((uiState as TaskUIState.Success).toDoTaskModel.id == -1) {
                        AppBarNewTask(onBackClicked = { navigateToList(Action.NO_ACTION) }) {

                        }
                    } else {
                        AppBarEditionTask(
                            onBackClicked = { navigateToList(Action.NO_ACTION) },
                            onDeleteClicked = { /*TODO*/ }) {

                        }
                    }
                }

                else -> {
                    AppBarEmpty(onBackClicked = { navigateToList(Action.NO_ACTION) })
                }
            }

        }
    ) {
        if (uiState is TaskUIState.Success) {
            Log.d("Description Task", (uiState as TaskUIState.Success).toDoTaskModel.description)
        }
    }
}