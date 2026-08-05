package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.channels.contract

import com.serhiimysyshyn.devlightiptvclient.domain.model.Channel

/** Internal results the ViewModel feeds to [ChannelsScreenReducer]. */
sealed interface ChannelsScreenEvent {

    data class Success(val channels: List<Channel>) : ChannelsScreenEvent

    data class QueryChanged(val query: String) : ChannelsScreenEvent

    data object Error : ChannelsScreenEvent
}
