package com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Launches [block] in `viewModelScope`, routing failures to [onError] instead of crashing.
 *
 * [CancellationException] is rethrown: cancellation is how coroutines shut down normally, and
 * swallowing it here would report a screen leaving the composition as an error state.
 */
fun ViewModel.safeLaunch(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    onError: (Throwable) -> Unit = {},
    block: suspend CoroutineScope.() -> Unit,
): Job = viewModelScope.safeLaunch(dispatcher, onError, block)

/**
 * Scope-level counterpart of [safeLaunch], for use with `rememberCoroutineScope`.
 *
 * Catching `Throwable` is the point of this function: it is the single error boundary around
 * ViewModel work, so anything a repository throws must reach [onError] rather than the default
 * handler. Cancellation is excluded above.
 */
@Suppress("TooGenericExceptionCaught")
fun CoroutineScope.safeLaunch(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    onError: (Throwable) -> Unit = {},
    block: suspend CoroutineScope.() -> Unit,
): Job = launch(dispatcher) {
    try {
        block()
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (throwable: Throwable) {
        onError(throwable)
    }
}
