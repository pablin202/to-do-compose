package com.pdm.to_do_compose.presentation.todo_details

import com.pdm.to_do_compose.R
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.to_do_compose.domain.models.Priority
import com.pdm.to_do_compose.domain.models.ToDoTaskModel
import com.pdm.to_do_compose.domain.repositories.ToDoRepository
import com.pdm.to_do_compose.util.Action
import com.pdm.to_do_compose.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    private val taskId: Int = checkNotNull(savedStateHandle["taskId"])

    private val _uiState = MutableStateFlow(TaskUIState())

    val uiState: StateFlow<TaskUIState> = _uiState

    private val _uiEvent = Channel<UiTaskEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getTask()
    }

    private fun getTask() {
        if (taskId > -1) {
            _uiState.value = _uiState.value.copy(loading = true)
            try {
                viewModelScope.launch {
                    toDoRepository.getTaskById(taskId).collect {
                        _uiState.value = _uiState.value.copy(loading = false, toDoTaskModel = it)
                    }
                }
            } catch (ex: Exception) {
                _uiState.value =
                    _uiState.value.copy(loading = false, toDoTaskModel = null, error = ex)
            }
        } else {
            _uiState.value =
                _uiState.value.copy(
                    loading = false,
                    toDoTaskModel = ToDoTaskModel.EmptyModel,
                    error = null
                )
        }
    }

    fun priorityChange(priority: Priority) {
        _uiState.update {
            _uiState.value.copy(
                loading = false,
                toDoTaskModel = _uiState.value.toDoTaskModel?.copy(priority = priority.ordinal),
                error = null
            )
        }
    }

    fun titleChange(title: String) {
        if (title.length < 21) {
            _uiState.update {
                _uiState.value.copy(
                    loading = false,
                    toDoTaskModel = _uiState.value.toDoTaskModel?.copy(title = title),
                    error = null
                )
            }
        }
    }

    fun descriptionChange(description: String) {
        _uiState.update {
            _uiState.value.copy(
                loading = false,
                toDoTaskModel = _uiState.value.toDoTaskModel?.copy(description = description),
                error = null
            )
        }
    }

    fun addTask() {
        if (_uiState.value.toDoTaskModel?.title?.isEmpty() == true
            || _uiState.value.toDoTaskModel?.description?.isEmpty() == true
        ) {
            viewModelScope.launch {
                showSnackBar(R.string.empty_task_message)
            }
        } else {
            viewModelScope.launch {
                _uiState.value.toDoTaskModel?.copy(id = 0)?.let { toDoRepository.addTask(it) }
                returnToList(Action.ADD)
            }
        }
    }

    fun updateTask() {
        if (_uiState.value.toDoTaskModel?.title?.isEmpty() == true
            || _uiState.value.toDoTaskModel?.description?.isEmpty() == true
        ) {
            viewModelScope.launch {
                showSnackBar(R.string.empty_task_message)
            }
        } else {
            viewModelScope.launch {
                _uiState.value.toDoTaskModel?.let { toDoRepository.updateTask(it) }
                returnToList(Action.UPDATE)
            }
        }
    }

    fun deleteTask() {
        viewModelScope.launch {
            _uiState.value.toDoTaskModel?.let { toDoRepository.deleteTask(it) }
            returnToList(Action.DELETE)
        }
    }

    private suspend fun returnToList(action: Action) {
        _uiEvent.send(UiTaskEvent.NavigateBackEvent(action))
    }

    private suspend fun showSnackBar(text: Int) {
        _uiEvent.send(UiTaskEvent.ShowSnackbar(UiText.StringResource(text)))
    }
}