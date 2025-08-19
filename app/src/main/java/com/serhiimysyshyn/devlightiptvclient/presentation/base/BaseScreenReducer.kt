package com.serhiimysyshyn.devlightiptvclient.presentation.base

abstract class BaseScreenReducer<S, E> {

    abstract fun reduce(currentState: S, event: E): S
}