package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract

import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer.EqualizerPreset

sealed interface PlayerScreenIntent {

    data class LoadChannel(val channelId: Long) : PlayerScreenIntent

    data class ToggleChannelFavourite(val channelId: Long) : PlayerScreenIntent

    data object LoadFavouriteChannels : PlayerScreenIntent

    data class ApplyPreset(val preset: EqualizerPreset) : PlayerScreenIntent

    data object ShowPresetDialog : PlayerScreenIntent

    data object HidePresetDialog : PlayerScreenIntent

    data object ToggleFullscreen : PlayerScreenIntent
}
