package com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels

import com.serhiimysyshyn.devlightiptvclient.data.repository.IMainRepository
import com.serhiimysyshyn.devlightiptvclient.presentation.base.BaseViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.intent.ChannelsScreenEvent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.intent.ChannelsScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.intent.ChannelsScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.utils.safeLaunch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch

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
        safeLaunch(
            onError = {
                _state.value = channelsScreenReducer.reduce(
                    _state.value,
                    ChannelsScreenEvent.Error
                )
            }
        ) {
            mainRepository.getChannelsByPlaylistId(playlistId)
                .collect { channels ->
                    _state.value = channelsScreenReducer.reduce(
                        _state.value,
                        ChannelsScreenEvent.Success(channels)
                    )
                }
        }
    }

    private fun addToFavourite(channelId: Long) {
        safeLaunch {
            mainRepository.addChannelToFavourite(channelId)
        }
    }

    private fun removeFromFavourite(channelId: Long) {
        safeLaunch {
            mainRepository.removeChannelFromFavourite(channelId)
        }
    }

    private fun loadFavouriteChannels() {
        safeLaunch {
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