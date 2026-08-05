package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player

import androidx.lifecycle.SavedStateHandle
import com.serhiimysyshyn.devlightiptvclient.domain.repository.MainRepository
import com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.source.NavigationRoute
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.base.viewmodel.BaseViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.ext.safeLaunch
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract.PlayerScreenEvent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract.PlayerScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract.PlayerScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract.PlayerScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerViewModel(
    savedStateHandle: SavedStateHandle,
    private val mainRepository: MainRepository,
    private val reducer: PlayerScreenReducer,
) : BaseViewModel<PlayerScreenIntent>() {

    private val _state = MutableStateFlow(PlayerScreenState())
    val state: StateFlow<PlayerScreenState> = _state.asStateFlow()

    private val channelId: Long =
        savedStateHandle[NavigationRoute.Player.ARG_CHANNEL_ID] ?: NO_CHANNEL_ID

    init {
        if (channelId != NO_CHANNEL_ID) {
            loadChannel(channelId)
        }
        observeFavouriteChannels()
    }

    override fun processIntent(intent: PlayerScreenIntent) {
        when (intent) {
            is PlayerScreenIntent.LoadChannel -> loadChannel(intent.channelId)
            is PlayerScreenIntent.ToggleChannelFavourite -> toggleFavourite(intent.channelId)
            is PlayerScreenIntent.LoadFavouriteChannels -> observeFavouriteChannels()
            is PlayerScreenIntent.ApplyPreset -> emit(PlayerScreenEvent.PresetApplied(intent.preset))
            is PlayerScreenIntent.ShowPresetDialog -> emit(PlayerScreenEvent.ShowPresetDialog)
            is PlayerScreenIntent.HidePresetDialog -> emit(PlayerScreenEvent.HidePresetDialog)
            is PlayerScreenIntent.ToggleFullscreen -> toggleFullscreen()
        }
    }

    private fun toggleFullscreen() {
        emit(PlayerScreenEvent.FullscreenChanged(!_state.value.isFullscreen))
    }

    private fun loadChannel(channelId: Long) {
        safeLaunch(onError = { emit(PlayerScreenEvent.Error) }) {
            emit(PlayerScreenEvent.ChannelLoaded(mainRepository.getChannelInfoById(channelId)))
        }
    }

    private fun observeFavouriteChannels() {
        safeLaunch(onError = { emit(PlayerScreenEvent.Error) }) {
            mainRepository.getFavouriteChannels().collect { channels ->
                emit(PlayerScreenEvent.FavouriteChannelsLoaded(channels))
            }
        }
    }

    /**
     * Toggles rather than only adding — the button showed a filled heart for an already-favourite
     * channel but tapping it re-added the channel instead of removing it.
     */
    private fun toggleFavourite(channelId: Long) {
        val isFavourite = _state.value.currentChannel?.isFavorite == true

        safeLaunch(onError = { emit(PlayerScreenEvent.Error) }) {
            if (isFavourite) {
                mainRepository.removeChannelFromFavourite(channelId)
            } else {
                mainRepository.addChannelToFavourite(channelId)
            }
        }
    }

    private fun emit(event: PlayerScreenEvent) {
        _state.value = reducer.reduce(_state.value, event)
    }

    private companion object {
        const val NO_CHANNEL_ID = -1L
    }
}
