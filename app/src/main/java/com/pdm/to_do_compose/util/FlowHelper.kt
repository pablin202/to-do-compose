package com.pdm.to_do_compose.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

fun <T> Flow<T>.stateIn(
    coroutineScope: CoroutineScope,
    initialValue: T
): StateFlow<T> = stateIn(
    scope = coroutineScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = initialValue
)