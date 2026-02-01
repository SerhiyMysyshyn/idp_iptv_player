package com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.contract

import androidx.compose.runtime.Immutable
import com.serhiimysyshyn.devlightiptvclient.data.models.Channel

@Immutable
data class ChannelsScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val query: String = "",
    val allChannels: List<Channel> = emptyList(),
    val filteredChannels: List<Channel> = emptyList(),
)