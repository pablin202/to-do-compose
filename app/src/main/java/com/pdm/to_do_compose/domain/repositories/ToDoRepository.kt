package com.pdm.to_do_compose.domain.repositories

import com.pdm.to_do_compose.domain.models.ToDoTaskModel
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {

    val getAllTask: Flow<List<ToDoTaskModel>>
//    val sortByLowPriority: Flow<List<ToDoTaskModel>>
//    val sortByHighPriority: Flow<List<ToDoTaskModel>>

    fun getTaskById(taskId: Int): Flow<ToDoTaskModel>

    suspend fun addTask(toDoTaskModel:ToDoTaskModel)

    suspend fun updateTask(toDoTaskModel:ToDoTaskModel)

    suspend fun deleteTask(toDoTaskModel:ToDoTaskModel)

    suspend fun deleteAllTasks()

    fun searchTasks(searchQuery: String): Flow<List<ToDoTaskModel>>
}