package com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels

import com.serhiimysyshyn.devlightiptvclient.data.models.Channel


data class ChannelsScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val channels: List<Channel> = emptyList()
)