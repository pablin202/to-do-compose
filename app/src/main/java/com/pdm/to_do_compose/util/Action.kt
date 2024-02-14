package com.pdm.to_do_compose.util

enum class Action {
    ADD,
    UPDATE,
    DELETE,
    DELETE_ALL,
    UNDO,
    NO_ACTION
}

fun String?.toAction(): Action {
    return when {
        this == "ADD" -> {
            Action.ADD
        }

        this == "UPDATE" -> {
            Action.UPDATE
        }

        this == "DELETE" -> {
            Action.DELETE
        }
        else -> {
            Action.NO_ACTION
        }
    }
}