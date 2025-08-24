package com.serhiimysyshyn.devlightiptvclient.presentation.screens.player

import androidx.lifecycle.SavedStateHandle
import com.serhiimysyshyn.devlightiptvclient.data.repository.IMainRepository
import com.serhiimysyshyn.devlightiptvclient.presentation.base.BaseViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.contract.PlayerScreenEvent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.contract.PlayerScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.contract.PlayerScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.contract.PlayerScreenState
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.equalizer.EqualizerPreset
import com.serhiimysyshyn.devlightiptvclient.presentation.utils.safeLaunch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerViewModel(
    savedStateHandle: SavedStateHandle,
    private val mainRepository: IMainRepository,
    private val playerScreenReducer: PlayerScreenReducer
) : BaseViewModel<PlayerScreenIntent>() {

    private val _state = MutableStateFlow(PlayerScreenState())
    val state: StateFlow<PlayerScreenState> = _state.asStateFlow()

    val channelId: Long = savedStateHandle["channelId"] ?: -1

    override fun processIntent(intent: PlayerScreenIntent) {
        when (intent) {
            is PlayerScreenIntent.AddChannelToFavourite -> addChannelToFavourite(intent.channelId)
            is PlayerScreenIntent.LoadChannel -> loadChannelData(intent.channelId)
            is PlayerScreenIntent.LoadFavouriteChannels -> loadFavouriteChannels()
            is PlayerScreenIntent.ApplyPreset -> applyPreset(intent.preset)
        }
    }

    init {
        loadChannelData(channelId)
        loadFavouriteChannels()
    }

    fun loadChannelData(channelId: Long) {
        safeLaunch {
            val currentChannel = mainRepository.getChannelInfoById(channelId)
            _state.value = playerScreenReducer.reduce(
                _state.value,
                PlayerScreenEvent.LoadChannelDataSuccessEvent(currentChannel)
            )
        }
    }

    fun addChannelToFavourite(channelId: Long) {
        safeLaunch {
            mainRepository.addChannelToFavourite(channelId)
        }
    }

    fun loadFavouriteChannels() {
        safeLaunch {
            mainRepository.getFavouriteChannels()
                .collect {
                    _state.value = playerScreenReducer.reduce(
                        _state.value,
                        PlayerScreenEvent.LoadFavouriteChannelsSuccessEvent(it)
                    )
                }
        }
    }

    fun applyPreset(preset: EqualizerPreset) {
        safeLaunch {
            _state.value = playerScreenReducer.reduce(
                _state.value,
                PlayerScreenEvent.ApplyPresetEvent(preset)
            )
        }
    }
}