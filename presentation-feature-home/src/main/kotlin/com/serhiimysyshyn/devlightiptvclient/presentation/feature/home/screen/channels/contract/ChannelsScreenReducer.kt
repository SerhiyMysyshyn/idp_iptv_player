package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.channels.contract

import com.serhiimysyshyn.devlightiptvclient.domain.model.Channel
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.base.reducer.BaseScreenReducer

class ChannelsScreenReducer : BaseScreenReducer<ChannelsScreenState, ChannelsScreenEvent>() {

    override fun reduce(
        currentState: ChannelsScreenState,
        event: ChannelsScreenEvent,
    ): ChannelsScreenState = when (event) {
        is ChannelsScreenEvent.Success -> currentState.copy(
            isLoading = false,
            isError = false,
            allChannels = event.channels,
            filteredChannels = event.channels.filterBy(currentState.query),
        )

        // Filtering lives here rather than in the ViewModel so that a new page of channels and a
        // new query both go through exactly one place — otherwise the two can disagree.
        is ChannelsScreenEvent.QueryChanged -> currentState.copy(
            query = event.query,
            filteredChannels = currentState.allChannels.filterBy(event.query),
        )

        is ChannelsScreenEvent.Error -> currentState.copy(
            isLoading = false,
            isError = true,
            allChannels = emptyList(),
            filteredChannels = emptyList(),
        )
    }
}

private fun List<Channel>.filterBy(query: String): List<Channel> =
    if (query.isBlank()) this else filter { it.name.contains(query, ignoreCase = true) }
