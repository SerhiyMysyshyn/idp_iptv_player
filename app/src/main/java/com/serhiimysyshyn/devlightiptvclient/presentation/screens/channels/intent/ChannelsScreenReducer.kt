package com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.intent

import com.serhiimysyshyn.devlightiptvclient.presentation.base.BaseScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.ChannelsScreenState

class ChannelsScreenReducer : BaseScreenReducer<ChannelsScreenState, ChannelsScreenEvent>() {

    override fun reduce(currentState: ChannelsScreenState, event: ChannelsScreenEvent): ChannelsScreenState {
        return when (event) {
            is ChannelsScreenEvent.Success -> currentState.copy(
                isLoading = false,
                isError = false,
                channels = event.channels
            )
            is ChannelsScreenEvent.Error -> currentState.copy(
                isLoading = false,
                isError = true,
                channels = emptyList()
            )
        }
    }
}