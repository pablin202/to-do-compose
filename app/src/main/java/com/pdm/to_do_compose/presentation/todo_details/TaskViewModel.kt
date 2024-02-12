package com.pdm.to_do_compose.presentation.todo_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.to_do_compose.domain.models.ToDoTaskModel
import com.pdm.to_do_compose.domain.repositories.ToDoRepository
import com.pdm.to_do_compose.presentation.todo_list.ToDoTasksUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    private val taskId: Int = checkNotNull(savedStateHandle["taskId"])

    private val _uiState = MutableStateFlow<TaskUIState>(TaskUIState.Idle)

    val uiState: StateFlow<TaskUIState> = _uiState

    init {
        getTask()
    }

    private fun getTask() {
        if (taskId > -1) {
            _uiState.value = TaskUIState.Loading
            try {
                viewModelScope.launch {
                    toDoRepository.getTaskById(taskId).collect {
                        _uiState.value = TaskUIState.Success(it)
                    }
                }
            } catch (ex: Exception) {
                _uiState.value = TaskUIState.Error(ex)
            }
        } else {
            _uiState.value = TaskUIState.Success(ToDoTaskModel.EmptyModel)
        }
    }
}