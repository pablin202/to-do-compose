package com.pdm.to_do_compose.util

import kotlinx.coroutines.CoroutineDispatcher

class AppDispatchers(
    val default: CoroutineDispatcher,
    val main: CoroutineDispatcher,
    val io: CoroutineDispatcher
) {
    companion object {
        fun createTestDispatchers(coroutineDispatcher: CoroutineDispatcher): AppDispatchers {
            return AppDispatchers(
                default = coroutineDispatcher,
                main = coroutineDispatcher,
                io = coroutineDispatcher
            )
        }
    }
}