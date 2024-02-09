package com.pdm.to_do_compose.presentation.todo_details

import androidx.lifecycle.ViewModel
import com.pdm.to_do_compose.domain.repositories.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository
) : ViewModel() {
}