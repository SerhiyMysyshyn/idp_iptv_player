package com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.intent

import com.serhiimysyshyn.devlightiptvclient.presentation.base.BaseScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.ChannelsScreenState

class ChannelsScreenReducer : BaseScreenReducer<ChannelsScreenState, ChannelsScreenEvent>() {

    override fun reduce(
        currentState: ChannelsScreenState,
        event: ChannelsScreenEvent
    ): ChannelsScreenState {
        return when (event) {
            is ChannelsScreenEvent.Success -> {
                val all = event.channels
                val filtered = if (currentState.query.isBlank()) {
                    all
                } else {
                    all.filter { it.name.contains(currentState.query, ignoreCase = true) }
                }
                currentState.copy(
                    isLoading = false,
                    isError = false,
                    allChannels = all,
                    filteredChannels = filtered
                )
            }
            is ChannelsScreenEvent.Error -> currentState.copy(
                isLoading = false,
                isError = true,
                allChannels = emptyList(),
                filteredChannels = emptyList()
            )
        }
    }
}