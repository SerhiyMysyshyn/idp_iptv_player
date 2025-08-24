package com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.contract

import com.serhiimysyshyn.devlightiptvclient.data.models.Channel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.equalizer.EqualizerPreset

data class PlayerScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val videoUrl: String = "",
    val currentChannel: Channel? = null,
    val likedChannels: List<Channel> = emptyList(),
    val currentPreset: EqualizerPreset = EqualizerPreset.NORMAL
)