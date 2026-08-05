package com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.base.reducer

/**
 * Pure state transition: `(currentState, event) -> newState`.
 *
 * Implementations must stay side-effect free — no I/O, no coroutines, no logging. That is what
 * makes them trivially unit-testable and keeps state changes reproducible.
 *
 * @param S the screen's state type.
 * @param E the internal event type produced by the ViewModel.
 */
abstract class BaseScreenReducer<S, E> {

    abstract fun reduce(currentState: S, event: E): S
}
