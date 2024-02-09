package com.pdm.to_do_compose.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pdm.to_do_compose.data.entities.ToDoTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllTask(): Flow<List<ToDoTaskEntity>>

    @Query("SELECT * FROM todo_table WHERE id=:taskId")
    fun getTaskById(taskId: Int): Flow<ToDoTaskEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(toDoTaskEntity: ToDoTaskEntity)

    @Update
    suspend fun updateTask(toDoTaskEntity: ToDoTaskEntity)

    @Delete
    suspend fun deleteTask(toDoTaskEntity: ToDoTaskEntity)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM todo_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchTasks(searchQuery: String): Flow<List<ToDoTaskEntity>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun selectByLowPriority(): Flow<List<ToDoTaskEntity>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 3 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 1 END")
    fun selectByHighPriority(): Flow<List<ToDoTaskEntity>>

}