package com.pdm.to_do_compose.data.mappers

import com.pdm.to_do_compose.data.entities.ToDoTaskEntity
import com.pdm.to_do_compose.domain.models.ToDoTaskModel

fun ToDoTaskEntity.toModel(): ToDoTaskModel {
    return ToDoTaskModel(
        id, title, description, priority
    )
}

fun ToDoTaskModel.toEntity(): ToDoTaskEntity {
    return ToDoTaskEntity(
        id, title, description, priority
    )
}