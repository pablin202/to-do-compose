package com.pdm.to_do_compose.presentation.todo_list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.to_do_compose.domain.models.Priority
import com.pdm.to_do_compose.domain.repositories.ToDoRepository
import com.pdm.to_do_compose.util.Action
import com.pdm.to_do_compose.util.SearchAppBarState
import com.pdm.to_do_compose.util.UiText
import com.pdm.to_do_compose.util.toAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.pdm.to_do_compose.R

@HiltViewModel
class ToDoListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    private val action: Action = savedStateHandle.getStateFlow("action", "").value.toAction()

    private val _searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)

    private val _searchTextState: MutableState<String> = mutableStateOf("")

    private val _uiState = MutableStateFlow<ToDoTasksUIState>(ToDoTasksUIState.Idle)

    val searchAppBarState: State<SearchAppBarState> =
        _searchAppBarState

    val searchTextState: MutableState<String> = _searchTextState

    val uiState: StateFlow<ToDoTasksUIState> = _uiState

    private val _uiEvent = Channel<UiTaskListEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getAllTask()
        showMessageAfterAction()
    }

    private fun getAllTask() {
        _uiState.value = ToDoTasksUIState.Loading
        try {
            viewModelScope.launch {
                toDoRepository.getAllTask.collect {
                    _uiState.value = ToDoTasksUIState.Success(it)
                }
            }
        } catch (ex: Exception) {
            _uiState.value = ToDoTasksUIState.Error(ex)
        }
    }

    fun searchByText() {
        _uiState.value = ToDoTasksUIState.Loading
        try {
            viewModelScope.launch {
                toDoRepository.searchTasks(searchTextState.value).collect {
                    _uiState.value = ToDoTasksUIState.Success(it)
                }
            }
        } catch (ex: Exception) {
            _uiState.value = ToDoTasksUIState.Error(ex)
        }
    }

    fun deleteAllTask() {
        viewModelScope.launch {
            toDoRepository.deleteAllTasks()
            _uiState.value = ToDoTasksUIState.Success(emptyList())
        }
    }

    fun changePriority(priority: Priority) {
        when (priority) {
            Priority.LOW -> {
                try {
                    viewModelScope.launch {
                        toDoRepository.sortByLowPriority.collect {
                            _uiState.value = ToDoTasksUIState.Success(it)
                        }
                    }
                } catch (ex: Exception) {
                    _uiState.value = ToDoTasksUIState.Error(ex)
                }
            }

            Priority.HIGH -> {
                try {
                    viewModelScope.launch {
                        toDoRepository.sortByHighPriority.collect {
                            _uiState.value = ToDoTasksUIState.Success(it)
                        }
                    }
                } catch (ex: Exception) {
                    _uiState.value = ToDoTasksUIState.Error(ex)
                }
            }

            else -> {
                getAllTask()
            }
        }
    }

    fun searchTextChange(text: String) {
        searchTextState.value = text
    }

    fun showSearchBar() {
        _searchAppBarState.value = SearchAppBarState.OPENED
    }

    fun closeSearchBar() {
        if (searchTextState.value.isEmpty()) {
            _searchAppBarState.value = SearchAppBarState.CLOSED
        } else {
            searchTextChange("")
            getAllTask()
        }
    }

    private fun showMessageAfterAction() {
        when (action) {
            Action.ADD -> {
                showSnackBar(R.string.successfully_added_task_message)
            }

            Action.UPDATE -> {
                showSnackBar(R.string.successfully_saved_task_message)
            }

            Action.DELETE -> {
                showSnackBar(R.string.successfully_deleted_task_message)
            }

            else -> {
                //no-op
            }
        }
    }

    private fun showSnackBar(text: Int) {
        viewModelScope.launch {
            _uiEvent.send(UiTaskListEvent.ShowSnackbar(UiText.StringResource(text)))
        }
    }
}