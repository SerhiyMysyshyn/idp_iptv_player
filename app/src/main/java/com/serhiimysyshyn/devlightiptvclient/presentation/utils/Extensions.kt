package com.serhiimysyshyn.devlightiptvclient.presentation.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun ViewModel.safeLaunch(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    onError: (Throwable) -> Unit = {},
    block: suspend CoroutineScope.() -> Unit
): Job {
    val handler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }

    return viewModelScope.launch(dispatcher + handler) {
        try {
            block()
        } catch (e: Exception) {
            onError(e)
        }
    }
}

fun CoroutineScope.safeLaunch(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    onError: (Throwable) -> Unit = {},
    block: suspend CoroutineScope.() -> Unit
): Job {
    val handler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }

    return this.launch(dispatcher + handler) {
        try {
            block()
        } catch (e: Exception) {
            onError(e)
        }
    }
}