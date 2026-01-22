package com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.contract

import com.serhiimysyshyn.devlightiptvclient.data.models.Channel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.equalizer.EqualizerPreset

sealed class PlayerScreenEvent {

    data class LoadChannelDataSuccessEvent(val channel: Channel) : PlayerScreenEvent()

    data class LoadFavouriteChannelsSuccessEvent(
        val favouriteChannels: List<Channel>
    ) : PlayerScreenEvent()

    data class ApplyPresetEvent(
        val preset: EqualizerPreset
    ) : PlayerScreenEvent()
}