package com.pdm.to_do_compose.presentation.todo_list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.to_do_compose.domain.models.Priority
import com.pdm.to_do_compose.domain.models.ToDoTaskModel
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

    private val _allTask = MutableStateFlow<List<ToDoTaskModel>>(emptyList())

    val searchAppBarState: State<SearchAppBarState> =
        _searchAppBarState

    val searchTextState: MutableState<String> = _searchTextState

    val allTasks: StateFlow<List<ToDoTaskModel>> = _allTask

    init {
        getAllTask()
    }

    private fun getAllTask() {
        viewModelScope.launch {
            toDoRepository.getAllTask.collect {
                _allTask.value = it
            }
        }
    }

    fun searchByText(text: String) {
        viewModelScope.launch {
            toDoRepository.searchTasks(text).collect {
                _allTask.value = it
            }
        }
    }

    fun deleteAllTask() {
        viewModelScope.launch {
            toDoRepository.deleteAllTasks()
            _allTask.value = emptyList()
        }
    }

    fun changePriority(priority: Priority) {
        when (priority) {
            Priority.LOW -> {
                viewModelScope.launch {
                    toDoRepository.sortByLowPriority.collect {
                        _allTask.value = it
                    }
                }
            }

            Priority.HIGH -> {
                viewModelScope.launch {
                    toDoRepository.sortByHighPriority.collect {
                        _allTask.value = it
                    }
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
        }
    }
}