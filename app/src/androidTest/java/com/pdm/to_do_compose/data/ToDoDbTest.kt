package com.pdm.to_do_compose.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.pdm.to_do_compose.data.entities.ToDoTaskEntity
import com.pdm.to_do_compose.domain.models.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.cancelAndJoin
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
@SmallTest
class ToDoDbTest {

    private lateinit var database: ToDoDatabase
    private lateinit var toDoDao: ToDoDao

    @Before
    fun databaseCreated() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ToDoDatabase::class.java
        ).allowMainThreadQueries().build()

        toDoDao = database.toDoDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertTaskReturnsTrue(): Unit = runBlocking {

        val taskOne = ToDoTaskEntity(
            id = 1,
            title = "Title",
            description = "Description",
            priority = Priority.HIGH
        )

        toDoDao.addTask(taskOne)

        val latch = CountDownLatch(1)

        val job = async(Dispatchers.IO) {
            toDoDao.getAllTask().collect {
                assertThat(it).contains(taskOne)
                latch.countDown()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun insertTaskReturnsTrueById(): Unit = runBlocking {

        val taskOne = ToDoTaskEntity(
            id = 1,
            title = "Title",
            description = "Description",
            priority = Priority.HIGH
        )

        toDoDao.addTask(taskOne)

        val latch = CountDownLatch(1)

        val job = async(Dispatchers.IO) {
            toDoDao.getTaskById(1).collect {
                assertThat(it.id).isEqualTo(1)
                assertThat(it.id).isNotEqualTo(2)
                latch.countDown()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun updateTaskReturnsTrueById(): Unit = runBlocking {

        var taskOne = ToDoTaskEntity(
            id = 1,
            title = "Title",
            description = "Description",
            priority = Priority.HIGH
        )

        toDoDao.addTask(taskOne)

        taskOne = taskOne.copy(title = "Title 2")

        toDoDao.updateTask(taskOne)

        val latch = CountDownLatch(1)

        val job = async(Dispatchers.IO) {
            toDoDao.getTaskById(1).collect {
                assertThat(it.title).isEqualTo(taskOne.title)
                latch.countDown()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun deleteTaskReturnsTrueById(): Unit = runBlocking {

        val taskOne = ToDoTaskEntity(
            id = 1,
            title = "Title",
            description = "Description",
            priority = Priority.HIGH
        )

        toDoDao.addTask(taskOne)

        toDoDao.deleteTask(taskOne)

        val latch = CountDownLatch(1)

        val job = async(Dispatchers.IO) {
            toDoDao.getTaskById(1).collect {
                assertThat(it).isNull()
                latch.countDown()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }

        job.cancelAndJoin()
    }

    @Test
    fun deleteAllTasksReturnsTrueById(): Unit = runBlocking {

        val taskOne = ToDoTaskEntity(
            id = 1,
            title = "Title",
            description = "Description",
            priority = Priority.HIGH
        )

        val taskTwo = ToDoTaskEntity(
            id = 2,
            title = "Title",
            description = "Description",
            priority = Priority.HIGH
        )

        toDoDao.addTask(taskOne)
        toDoDao.addTask(taskTwo)

        toDoDao.deleteAllTasks()

        val latch = CountDownLatch(1)

        val job = async(Dispatchers.IO) {
            toDoDao.getAllTask().collect {
                assertThat(it).isEmpty()
                latch.countDown()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }

        job.cancelAndJoin()
    }
}