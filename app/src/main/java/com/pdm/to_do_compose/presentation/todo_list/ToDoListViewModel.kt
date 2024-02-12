package com.pdm.to_do_compose.presentation.todo_list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.to_do_compose.domain.models.Priority
import com.pdm.to_do_compose.domain.repositories.ToDoRepository
import com.pdm.to_do_compose.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    private val _searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)

    private val _searchTextState: MutableState<String> = mutableStateOf("")

    private val _uiState = MutableStateFlow<ToDoTasksUIState>(ToDoTasksUIState.Idle)

    val searchAppBarState: State<SearchAppBarState> =
        _searchAppBarState

    val searchTextState: MutableState<String> = _searchTextState

    val uiState: StateFlow<ToDoTasksUIState> = _uiState

    init {
        getAllTask()
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

    fun searchByText(text: String) {
        _uiState.value = ToDoTasksUIState.Loading
        try {
            viewModelScope.launch {
                toDoRepository.searchTasks(text).collect {
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
}