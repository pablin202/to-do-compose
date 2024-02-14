package com.pdm.to_do_compose.data.repositories

import com.pdm.to_do_compose.data.ToDoDao
import com.pdm.to_do_compose.data.mappers.toEntity
import com.pdm.to_do_compose.data.mappers.toModel
import com.pdm.to_do_compose.domain.models.ToDoTaskModel
import com.pdm.to_do_compose.domain.repositories.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ToDoRepositoryImpl @Inject constructor(
    private val toDoDao: ToDoDao
) : ToDoRepository {
    override val getAllTask: Flow<List<ToDoTaskModel>>
        get() = toDoDao.getAllTask().map { entity -> entity.map { it.toModel() } }
    override val sortByLowPriority: Flow<List<ToDoTaskModel>>
        get() = toDoDao.selectByLowPriority().map { entity -> entity.map { it.toModel() } }
    override val sortByHighPriority: Flow<List<ToDoTaskModel>>
        get() = toDoDao.selectByHighPriority().map { entity -> entity.map { it.toModel() } }

    override fun getTaskById(taskId: Int): Flow<ToDoTaskModel> = flow {

        val model = withContext(Dispatchers.IO) {
            toDoDao.getTaskById(taskId).toModel()
        }

        emit(model)
    }

    override suspend fun addTask(toDoTaskModel: ToDoTaskModel) {
        toDoDao.addTask(toDoTaskModel.toEntity())
    }

    override suspend fun updateTask(toDoTaskModel: ToDoTaskModel) {
        toDoDao.updateTask(toDoTaskModel.toEntity())
    }

    override suspend fun deleteTask(toDoTaskModel: ToDoTaskModel) {
        toDoDao.deleteTask(toDoTaskModel.id)
    }

    override suspend fun deleteAllTasks() {
        toDoDao.deleteAllTasks()
    }

    override fun searchTasks(searchQuery: String): Flow<List<ToDoTaskModel>> {
        return toDoDao.searchTasks(searchQuery).map { entity -> entity.map { it.toModel() } }
    }

}