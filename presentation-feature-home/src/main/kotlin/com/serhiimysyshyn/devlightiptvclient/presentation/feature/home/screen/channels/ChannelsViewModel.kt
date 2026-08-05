package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.channels

import com.serhiimysyshyn.devlightiptvclient.domain.model.Channel
import com.serhiimysyshyn.devlightiptvclient.domain.repository.MainRepository
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.base.viewmodel.BaseViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.ext.safeLaunch
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.channels.contract.ChannelsScreenEvent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.channels.contract.ChannelsScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.channels.contract.ChannelsScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.channels.contract.ChannelsScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChannelsViewModel(
    private val mainRepository: MainRepository,
    private val reducer: ChannelsScreenReducer,
) : BaseViewModel<ChannelsScreenIntent>() {

    private val _state = MutableStateFlow(ChannelsScreenState())
    val state: StateFlow<ChannelsScreenState> = _state.asStateFlow()

    /**
     * The active channel subscription.
     *
     * Kept so that re-entering the screen with a different playlist cancels the previous stream
     * instead of leaving two collectors racing to write the same state.
     */
    private var observeChannelsJob: Job? = null

    override fun processIntent(intent: ChannelsScreenIntent) {
        when (intent) {
            is ChannelsScreenIntent.LoadChannelsFromDatabase ->
                observeChannels(mainRepository.getChannelsByPlaylistId(intent.playlistId))

            is ChannelsScreenIntent.LoadFavouritesChannelsFromDatabase ->
                observeChannels(mainRepository.getFavouriteChannels())

            is ChannelsScreenIntent.AddToFavourite -> setFavourite(intent.channelId, isFavourite = true)
            is ChannelsScreenIntent.RemoveFromFavourite -> setFavourite(intent.channelId, isFavourite = false)
            is ChannelsScreenIntent.Search -> emit(ChannelsScreenEvent.QueryChanged(intent.query))
        }
    }

    private fun observeChannels(channels: Flow<List<Channel>>) {
        observeChannelsJob?.cancel()
        observeChannelsJob = safeLaunch(
            onError = { emit(ChannelsScreenEvent.Error) },
        ) {
            channels.collect { emit(ChannelsScreenEvent.Success(it)) }
        }
    }

    private fun setFavourite(channelId: Long, isFavourite: Boolean) {
        safeLaunch(onError = { emit(ChannelsScreenEvent.Error) }) {
            if (isFavourite) {
                mainRepository.addChannelToFavourite(channelId)
            } else {
                mainRepository.removeChannelFromFavourite(channelId)
            }
        }
    }

    private fun emit(event: ChannelsScreenEvent) {
        _state.value = reducer.reduce(_state.value, event)
    }
}
