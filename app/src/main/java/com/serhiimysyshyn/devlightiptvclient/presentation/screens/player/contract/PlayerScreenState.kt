package com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.contract

import com.serhiimysyshyn.devlightiptvclient.data.models.Channel

data class PlayerScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val currentChannel: Channel? = null,
    val likedChannels: List<Channel> = emptyList()
    // Equalizer settings
)