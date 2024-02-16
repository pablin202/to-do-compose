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
import com.pdm.to_do_compose.domain.models.ToDoTaskModel
import com.pdm.to_do_compose.domain.preferences.Preferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ToDoListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val toDoRepository: ToDoRepository,
    private val preferences: Preferences
) : ViewModel() {

    private val action: Action =
        savedStateHandle.getStateFlow("action", "").value.toAction()

    private val _searchAppBarState =
        MutableStateFlow(SearchAppBarState.CLOSED)

    private val _searchTextState = MutableStateFlow("")

    private val _sort: MutableStateFlow<Priority> =
        MutableStateFlow(preferences.loadPriority())

    private val _loadingState = MutableStateFlow(false)

    private val _error: MutableStateFlow<Throwable?> =
        MutableStateFlow(null)

    private val _uiEvent = Channel<UiTaskListEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _taskSelected: MutableState<ToDoTaskModel> =
        mutableStateOf(ToDoTaskModel.EmptyModel)

    private val _sortTasks = combine(toDoRepository.getAllTask, _sort) { tasks, sort ->
        changePriority(tasks, sort)
    }.catch {
        _error.value = it
    }

    val uiState: StateFlow<TasksUIState> = combine(
        _loadingState, _sort, _sortTasks, _searchTextState, _searchAppBarState,
    ) { isLoading, sort, tasks, text, barState ->

        if (text.isEmpty()) {
            TasksUIState(
                loading = isLoading,
                tasks = tasks,
                sort = sort,
                searchAppBarText = text,
                appBarState = barState,
            )
        } else {
            TasksUIState(
                loading = isLoading,
                tasks = tasks.filter {
                    it.description.contains(_searchTextState.value)
                            || it.title.contains(_searchTextState.value)
                },
                sort = sort,
                searchAppBarText = text,
                appBarState = barState,
            )
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TasksUIState(loading = true)
    )

    init {
        showMessageAfterAction()
    }

    fun deleteAllTask() {
        viewModelScope.launch {
            toDoRepository.deleteAllTasks()
        }
    }

    fun selectTask(toDoTaskModel: ToDoTaskModel) {
        viewModelScope.launch {
            _taskSelected.value = toDoTaskModel
        }
    }

    fun undoDeletedTask() {
        if (_taskSelected.value.id != ToDoTaskModel.EmptyModel.id) {
            viewModelScope.launch {
                toDoRepository.addTask(_taskSelected.value)
            }
        }
    }

    fun changePriority(tasks: List<ToDoTaskModel>, priority: Priority): List<ToDoTaskModel> {

        _sort.value = priority
        preferences.saveSort(_sort.value)

        return when (priority) {
            Priority.LOW -> {
                tasks.sortedBy { it.priority }
            }

            Priority.HIGH -> {
                tasks.sortedByDescending { it.priority }
            }

            else -> {
                tasks.sortedBy { it.id }
            }
        }
    }

    fun searchTextChange(text: String) {
        _searchTextState.value = text
    }

    fun showSearchBar() {
        _searchAppBarState.value = SearchAppBarState.OPENED
    }

    fun closeSearchBar() {
        if (_searchTextState.value.isEmpty()) {
            _searchAppBarState.value = SearchAppBarState.CLOSED
        } else {
            searchTextChange("")
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
                showSnackBar(R.string.successfully_deleted_task_message, true)
            }

            else -> {
                //no-op
            }
        }
    }

    private fun showSnackBar(text: Int, showUndoAction: Boolean = false) {
        viewModelScope.launch {
            _uiEvent.send(UiTaskListEvent.ShowSnackbar(UiText.StringResource(text), showUndoAction))
        }
    }
}