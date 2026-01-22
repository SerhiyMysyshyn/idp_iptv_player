package com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.contract

import com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.equalizer.EqualizerPreset

sealed class  PlayerScreenIntent {

    data class LoadChannel(
        val channelId: Long
    ) : PlayerScreenIntent()

    data class AddChannelToFavourite(
        val channelId: Long
    ) : PlayerScreenIntent()

    data object LoadFavouriteChannels : PlayerScreenIntent()

    data class ApplyPreset(
        val preset: EqualizerPreset
    ) : PlayerScreenIntent()
}