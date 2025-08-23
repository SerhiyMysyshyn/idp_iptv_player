package com.serhiimysyshyn.devlightiptvclient.presentation.screens.main

import com.serhiimysyshyn.devlightiptvclient.data.repository.IMainRepository
import com.serhiimysyshyn.devlightiptvclient.presentation.base.BaseViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.contract.MainScreenEffect
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.contract.MainScreenEvent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.contract.MainScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.contract.MainScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.contract.MainScreenState
import com.serhiimysyshyn.devlightiptvclient.presentation.utils.safeLaunch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

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
        safeLaunch {
            _effect.emit(MainScreenEffect.LaunchNewScreen(route))
        }
    }

    private fun launchNewRootScreen(route: String) {
        safeLaunch {
            _effect.emit(MainScreenEffect.LaunchNewRootScreen(route))
        }
    }

    private fun downloadNewPlaylist(url: String) {
        safeLaunch(dispatcher = Dispatchers.IO) {
            mainRepository.downloadM3UPlaylist(url)
        }
    }

    private fun updateSelectedMenuItemIndex(newIndex: Long) {
        _state.value = mainScreenReducer.reduce(_state.value, MainScreenEvent.UpdateMenuItemIndex(newIndex))
    }
}