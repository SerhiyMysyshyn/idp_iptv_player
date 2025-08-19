package com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.intent

import com.serhiimysyshyn.devlightiptvclient.data.models.Channel

sealed class ChannelsScreenEvent {

    data class Success(val channels: List<Channel>) : ChannelsScreenEvent()
    data object Error : ChannelsScreenEvent()
}