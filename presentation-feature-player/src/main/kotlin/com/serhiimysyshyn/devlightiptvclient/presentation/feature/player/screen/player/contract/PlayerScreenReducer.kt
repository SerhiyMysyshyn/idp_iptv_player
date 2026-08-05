package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract

import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.base.reducer.BaseScreenReducer

class PlayerScreenReducer : BaseScreenReducer<PlayerScreenState, PlayerScreenEvent>() {

    override fun reduce(
        currentState: PlayerScreenState,
        event: PlayerScreenEvent,
    ): PlayerScreenState = when (event) {
        is PlayerScreenEvent.ChannelLoaded -> currentState.copy(
            isLoading = false,
            isError = false,
            currentChannel = event.channel,
        )

        is PlayerScreenEvent.FavouriteChannelsLoaded -> currentState.copy(
            likedChannels = event.favouriteChannels,
            // The favourites stream is the source of truth for the favourite flag, so refresh the
            // currently playing channel from it — otherwise the heart icon goes stale after a tap.
            currentChannel = currentState.currentChannel?.let { channel ->
                channel.copy(isFavorite = event.favouriteChannels.any { it.id == channel.id })
            },
        )

        is PlayerScreenEvent.PresetApplied -> currentState.copy(
            currentPreset = event.preset,
            showPresetDialog = false,
        )

        is PlayerScreenEvent.ShowPresetDialog -> currentState.copy(showPresetDialog = true)

        is PlayerScreenEvent.HidePresetDialog -> currentState.copy(showPresetDialog = false)

        is PlayerScreenEvent.Error -> currentState.copy(
            isLoading = false,
            isError = true,
        )
    }
}
