package com.serhiimysyshyn.devlightiptvclient.presentation.screens.main

import androidx.lifecycle.viewModelScope
import com.serhiimysyshyn.devlightiptvclient.data.repository.IMainRepository
import com.serhiimysyshyn.devlightiptvclient.presentation.base.BaseViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.intent.MainScreenEffect
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.intent.MainScreenEvent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.intent.MainScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.intent.MainScreenReducer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val mainRepository: IMainRepository,
    private val mainScreenReducer: MainScreenReducer
) : BaseViewModel<MainScreenIntent>() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MainScreenEffect>()
    val effect = _effect.asSharedFlow()

    override fun processIntent(intent: MainScreenIntent) {
        when (intent) {
            is MainScreenIntent.OpenAddNewPlaylistDialog -> openAddNewPlaylistDialog()
            is MainScreenIntent.HideAddNewPlaylistDialog -> hideAddNewPlaylistDialog()

            is MainScreenIntent.StartDownloadingNewPlaylist -> downloadNewPlaylist(intent.url)

            is MainScreenIntent.LaunchNewScreen -> launchNewScreen(intent.route)
            is MainScreenIntent.LaunchNewRootScreen -> launchNewRootScreen(intent.route)

            is MainScreenIntent.UpdateSelectedMenuItemIndex -> updateSelectedMenuItemIndex(intent.newIndex)
        }
    }

    private fun openAddNewPlaylistDialog() {
        _state.value = mainScreenReducer.reduce(_state.value, MainScreenEvent.ShowAddNewPlaylistDialog)
    }

    private fun hideAddNewPlaylistDialog() {
        _state.value = mainScreenReducer.reduce(_state.value, MainScreenEvent.HideAddNewPlaylistDialog)
    }

    private fun launchNewScreen(route: String) {
        viewModelScope.launch {
            _effect.emit(MainScreenEffect.LaunchNewScreen(route))
        }
    }

    private fun launchNewRootScreen(route: String) {
        viewModelScope.launch {
            _effect.emit(MainScreenEffect.LaunchNewRootScreen(route))
        }
    }

    private fun downloadNewPlaylist(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.downloadM3UPlaylist(url)
        }
    }

    private fun updateSelectedMenuItemIndex(newIndex: Long) {
        _state.value = mainScreenReducer.reduce(_state.value, MainScreenEvent.UpdateMenuItemIndex(newIndex))
    }
}