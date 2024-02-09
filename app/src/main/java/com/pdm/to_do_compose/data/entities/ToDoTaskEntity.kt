package com.pdm.to_do_compose.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pdm.to_do_compose.domain.models.Priority
import com.pdm.to_do_compose.util.Constants.DATABASE_TABLE

@Entity(tableName = DATABASE_TABLE)
data class ToDoTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Priority
)
