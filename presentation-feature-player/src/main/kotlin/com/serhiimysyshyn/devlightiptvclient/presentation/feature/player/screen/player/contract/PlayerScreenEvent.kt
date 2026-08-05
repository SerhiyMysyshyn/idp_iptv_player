package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract

import com.serhiimysyshyn.devlightiptvclient.domain.model.Channel
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer.EqualizerPreset

sealed interface PlayerScreenEvent {

    data class ChannelLoaded(val channel: Channel) : PlayerScreenEvent

    data class FavouriteChannelsLoaded(val favouriteChannels: List<Channel>) : PlayerScreenEvent

    data class PresetApplied(val preset: EqualizerPreset) : PlayerScreenEvent

    data object ShowPresetDialog : PlayerScreenEvent

    data object HidePresetDialog : PlayerScreenEvent

    data object Error : PlayerScreenEvent
}
