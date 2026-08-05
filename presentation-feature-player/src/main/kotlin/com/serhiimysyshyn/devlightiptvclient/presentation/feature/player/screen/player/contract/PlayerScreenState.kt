package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract

import androidx.compose.runtime.Immutable
import com.serhiimysyshyn.devlightiptvclient.domain.model.Channel
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer.EqualizerPreset

@Immutable
data class PlayerScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val currentChannel: Channel? = null,
    val likedChannels: List<Channel> = emptyList(),
    val currentPreset: EqualizerPreset = EqualizerPreset.NORMAL,
    /** Dialog visibility lives in state, not in a `remember` inside the content composable. */
    val showPresetDialog: Boolean = false,
)
