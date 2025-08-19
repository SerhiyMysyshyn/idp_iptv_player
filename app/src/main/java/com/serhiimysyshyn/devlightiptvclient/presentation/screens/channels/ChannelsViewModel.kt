package com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels

import androidx.lifecycle.viewModelScope
import com.serhiimysyshyn.devlightiptvclient.data.repository.IMainRepository
import com.serhiimysyshyn.devlightiptvclient.presentation.base.BaseViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.intent.ChannelsScreenEvent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.intent.ChannelsScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.intent.ChannelsScreenReducer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ChannelsViewModel(
    private val mainRepository: IMainRepository,
    private val channelsScreenReducer: ChannelsScreenReducer
): BaseViewModel<ChannelsScreenIntent>() {

    private val _state = MutableStateFlow(ChannelsScreenState())
    val state: StateFlow<ChannelsScreenState> = _state.asStateFlow()

    override fun processIntent(intent: ChannelsScreenIntent) {
        when(intent) {
            is ChannelsScreenIntent.LoadChannelsFromDatabase -> loadChannelsByPlaylistId(intent.playlistId)
            is ChannelsScreenIntent.AddToFavourite -> addToFavourite(intent.channelId)
            is ChannelsScreenIntent.RemoveFromFavourite -> removeFromFavourite(intent.channelId)
            is ChannelsScreenIntent.LoadFavouritesChannelsFromDatabase -> loadFavouriteChannels()
        }
    }

    private fun loadChannelsByPlaylistId(playlistId: Long) {
        viewModelScope.launch {
            mainRepository.getChannelsByPlaylistId(playlistId)
                .catch {
                    _state.value = channelsScreenReducer.reduce(_state.value, ChannelsScreenEvent.Error)
                }
                .collect { channels ->
                    _state.value = channelsScreenReducer.reduce(
                        _state.value,
                        ChannelsScreenEvent.Success(channels)
                    )
                }
        }
    }

    private fun addToFavourite(channelId: Long) {
        viewModelScope.launch {
            mainRepository.addChannelToFavourite(channelId)
        }
    }

    private fun removeFromFavourite(channelId: Long) {
        viewModelScope.launch {
            mainRepository.removeChannelFromFavourite(channelId)
        }
    }

    private fun loadFavouriteChannels() {
        viewModelScope.launch {
            mainRepository.getFavouriteChannels()
                .catch {
                    _state.value = channelsScreenReducer.reduce(_state.value, ChannelsScreenEvent.Error)
                }
                .collect { channels ->
                    _state.value = channelsScreenReducer.reduce(
                        _state.value,
                        ChannelsScreenEvent.Success(channels)
                    )
                }
        }
    }
}