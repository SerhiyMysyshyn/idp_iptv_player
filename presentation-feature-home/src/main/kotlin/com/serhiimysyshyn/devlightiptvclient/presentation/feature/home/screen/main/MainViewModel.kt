package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main

import com.serhiimysyshyn.devlightiptvclient.domain.repository.MainRepository
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.base.viewmodel.BaseViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.ext.safeLaunch
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.contract.MainScreenEffect
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.contract.MainScreenEvent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.contract.MainScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.contract.MainScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.contract.MainScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class MainViewModel(
    private val mainRepository: MainRepository,
    private val reducer: MainScreenReducer,
) : BaseViewModel<MainScreenIntent>() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    /**
     * Buffered channel rather than a `MutableSharedFlow`.
     *
     * A hot SharedFlow with no replay drops emissions that happen before the screen starts
     * collecting, which loses the very first navigation after process death.
     */
    private val _effect = Channel<MainScreenEffect>(Channel.BUFFERED)
    val effect: Flow<MainScreenEffect> = _effect.receiveAsFlow()

    override fun processIntent(intent: MainScreenIntent) {
        when (intent) {
            is MainScreenIntent.OpenAddNewPlaylistDialog -> emit(MainScreenEvent.ShowAddNewPlaylistDialog)
            is MainScreenIntent.HideAddNewPlaylistDialog -> emit(MainScreenEvent.HideAddNewPlaylistDialog)
            is MainScreenIntent.StartDownloadingNewPlaylist -> downloadNewPlaylist(intent.url)
            is MainScreenIntent.LaunchNewScreen -> send(MainScreenEffect.LaunchNewScreen(intent.route))
            is MainScreenIntent.LaunchNewRootScreen -> send(MainScreenEffect.LaunchNewRootScreen(intent.route))
            is MainScreenIntent.UpdateSelectedMenuItemIndex ->
                emit(MainScreenEvent.UpdateMenuItemIndex(intent.newIndex))
        }
    }

    private fun downloadNewPlaylist(url: String) {
        safeLaunch(dispatcher = Dispatchers.IO) {
            mainRepository.downloadM3UPlaylist(url)
        }
    }

    private fun send(effect: MainScreenEffect) {
        safeLaunch { _effect.send(effect) }
    }

    private fun emit(event: MainScreenEvent) {
        _state.value = reducer.reduce(_state.value, event)
    }
}
