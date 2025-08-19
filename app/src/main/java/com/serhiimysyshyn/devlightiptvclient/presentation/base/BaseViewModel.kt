package com.serhiimysyshyn.devlightiptvclient.presentation.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<I>: ViewModel() {

    abstract fun processIntent(intent: I)

}