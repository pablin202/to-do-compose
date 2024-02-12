package com.pdm.to_do_compose.presentation.todo_list

import androidx.compose.runtime.MutableState
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

    val searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)

    val searchTextState: MutableState<String> =
        mutableStateOf("")

    private val _allTask = MutableStateFlow<List<ToDoTaskModel>>(emptyList())

    val allTasks: StateFlow<List<ToDoTaskModel>> = _allTask

    fun getAllTask() {
        viewModelScope.launch {
            toDoRepository.getAllTask.collect {
                _allTask.value = it
            }
        }
    }

    fun searchByText(text: String) {

    }

    fun deleteAllTask() {

    }

    fun changePriority(priority: Priority) {

    }

    fun searchTextChange(text: String) {
        searchTextState.value = text
    }

    fun showSearchBar() {
        searchAppBarState.value = SearchAppBarState.OPENED
    }

    fun closeSearchBar() {
        if (searchTextState.value.isEmpty()) {
            searchAppBarState.value = SearchAppBarState.CLOSED
        } else {
            searchTextChange("")
        }
    }
}