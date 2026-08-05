package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract

import com.serhiimysyshyn.devlightiptvclient.domain.model.Channel
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer.EqualizerBand
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer.EqualizerPreset
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.model.PlayerTrack

/**
 * Marks the events that only touch the audio-effect slice of state, so the reducer can route them
 * to one sub-function instead of carrying every branch in a single `when`.
 */
sealed interface AudioEffectEvent : PlayerScreenEvent

sealed interface PlayerScreenEvent {

    data class ChannelLoaded(val channel: Channel) : PlayerScreenEvent

    data class FavouriteChannelsLoaded(val favouriteChannels: List<Channel>) : PlayerScreenEvent

    data class PresetApplied(val preset: EqualizerPreset) : AudioEffectEvent

    data object ShowAudioSettings : AudioEffectEvent

    data object HideAudioSettings : AudioEffectEvent

    /** Carries the freshly-read band curve after the equalizer bound or a level changed. */
    data class EqualizerBandsChanged(val bands: List<EqualizerBand>) : AudioEffectEvent

    data class BassBoostChanged(val strength: Short) : AudioEffectEvent

    data class VirtualizerChanged(val strength: Short) : AudioEffectEvent

    data class FullscreenChanged(val isFullscreen: Boolean) : PlayerScreenEvent

    data class PlaybackErrorChanged(val hasError: Boolean) : PlayerScreenEvent

    data class TracksChanged(val tracks: List<PlayerTrack>) : PlayerScreenEvent

    data object ShowTrackSelector : PlayerScreenEvent

    data object HideTrackSelector : PlayerScreenEvent

    data class PictureInPictureEnabledLoaded(val isEnabled: Boolean) : PlayerScreenEvent

    data object Error : PlayerScreenEvent
}
