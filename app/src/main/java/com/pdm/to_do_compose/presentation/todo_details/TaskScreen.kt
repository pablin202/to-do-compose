package com.pdm.to_do_compose.presentation.todo_details

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.pdm.to_do_compose.util.Action

@Composable
fun TaskScreen(
    viewModel: TaskViewModel = hiltViewModel(),
    navigateToList: (Action) -> Unit
) {

}