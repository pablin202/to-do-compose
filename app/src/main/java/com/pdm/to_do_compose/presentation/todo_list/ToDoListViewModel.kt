package com.pdm.to_do_compose.presentation.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.to_do_compose.domain.models.ToDoTaskModel
import com.pdm.to_do_compose.domain.repositories.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    private val _allTask = MutableStateFlow<List<ToDoTaskModel>>(emptyList())

    val allTasks : StateFlow<List<ToDoTaskModel>> = _allTask

    fun getAllTask() {
        viewModelScope.launch {
            toDoRepository.getAllTask.collect {
                _allTask.value = it
            }
        }
    }

}