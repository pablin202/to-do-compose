package com.pdm.to_do_compose.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pdm.to_do_compose.data.entities.ToDoTaskEntity

@Database(entities = [ToDoTaskEntity::class], version = 1, exportSchema = false)
abstract class ToDoDatabase: RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
}