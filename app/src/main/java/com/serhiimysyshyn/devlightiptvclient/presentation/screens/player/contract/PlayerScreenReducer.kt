package com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.contract

import com.serhiimysyshyn.devlightiptvclient.presentation.base.BaseScreenReducer

class PlayerScreenReducer : BaseScreenReducer<PlayerScreenState, PlayerScreenEvent>() {

    override fun reduce(
        currentState: PlayerScreenState,
        event: PlayerScreenEvent
    ): PlayerScreenState {
        return when (event) {
            is PlayerScreenEvent.LoadChannelDataSuccessEvent -> currentState.copy(
                isLoading = false,
                isError = false,
                currentChannel = event.channel,
            )
            is PlayerScreenEvent.LoadFavouriteChannelsSuccessEvent -> currentState.copy(
                likedChannels = event.favouriteChannels
            )
            is PlayerScreenEvent.ApplyPresetEvent -> currentState.copy(
                currentPreset = event.preset
            )
        }
    }
}