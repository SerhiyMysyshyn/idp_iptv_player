package com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.base.viewmodel

import androidx.lifecycle.ViewModel

/**
 * Base for every screen ViewModel.
 *
 * A screen sends user actions in through [processIntent] and reads the resulting state from a
 * `StateFlow` the subclass exposes. The UI never calls anything else on the ViewModel.
 *
 * @param I the screen's intent type, declared in its `contract/` package.
 */
abstract class BaseViewModel<I> : ViewModel() {

    abstract fun processIntent(intent: I)
}
