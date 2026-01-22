package com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels

import com.serhiimysyshyn.devlightiptvclient.data.repository.MainRepository
import com.serhiimysyshyn.devlightiptvclient.presentation.base.BaseViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.contract.ChannelsScreenEvent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.contract.ChannelsScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.contract.ChannelsScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.contract.ChannelsScreenState
import com.serhiimysyshyn.devlightiptvclient.presentation.utils.safeLaunch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChannelsViewModel(
    private val mainRepository: MainRepository,
    private val channelsScreenReducer: ChannelsScreenReducer
): BaseViewModel<ChannelsScreenIntent>() {

    private val _state = MutableStateFlow(ChannelsScreenState())
    val state: StateFlow<ChannelsScreenState> = _state.asStateFlow()

    override fun processIntent(intent: ChannelsScreenIntent) {
        when (intent) {
            is ChannelsScreenIntent.LoadChannelsFromDatabase -> loadChannelsByPlaylistId(intent.playlistId)
            is ChannelsScreenIntent.LoadFavouritesChannelsFromDatabase -> loadFavouriteChannels()
            is ChannelsScreenIntent.AddToFavourite -> addToFavourite(intent.channelId)
            is ChannelsScreenIntent.RemoveFromFavourite -> removeFromFavourite(intent.channelId)
            is ChannelsScreenIntent.Search -> search(intent.query)
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

    private fun loadFavouriteChannels() {
        safeLaunch(
            onError = {
                _state.value = channelsScreenReducer.reduce(
                    _state.value,
                    ChannelsScreenEvent.Error
                )
            }
        ) {
            mainRepository.getFavouriteChannels()
                .collect { channels ->
                    _state.value = channelsScreenReducer.reduce(
                        _state.value,
                        ChannelsScreenEvent.Success(channels)
                    )
                }
        }
    }

    private fun search(query: String) {
        _state.update { current ->
            val filtered = if (query.isBlank()) {
                current.allChannels
            } else {
                current.allChannels.filter { it.name.contains(query, ignoreCase = true) }
            }
            current.copy(
                query = query,
                filteredChannels = filtered.toList()
            )
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
}