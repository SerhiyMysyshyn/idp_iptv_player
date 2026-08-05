package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract

import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer.EqualizerBand
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer.EqualizerPreset
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.model.PlayerTrack

/**
 * Marks the intents that only drive the audio-effect panel, so the ViewModel can route them to
 * one sub-function instead of carrying every branch in a single `when`.
 */
sealed interface AudioEffectIntent : PlayerScreenIntent

sealed interface PlayerScreenIntent {

    data class LoadChannel(val channelId: Long) : PlayerScreenIntent

    data class ToggleChannelFavourite(val channelId: Long) : PlayerScreenIntent

    data object LoadFavouriteChannels : PlayerScreenIntent

    data class ApplyPreset(val preset: EqualizerPreset) : AudioEffectIntent

    data object ShowAudioSettings : AudioEffectIntent

    data object HideAudioSettings : AudioEffectIntent

    /** Emitted once the platform equalizer is bound and its bands can be read. */
    data class AudioEffectsReady(val bands: List<EqualizerBand>) : AudioEffectIntent

    data class ChangeBandLevel(val index: Short, val millibel: Short) : AudioEffectIntent

    data class ChangeBassBoost(val strength: Short) : AudioEffectIntent

    data class ChangeVirtualizer(val strength: Short) : AudioEffectIntent

    data object ResetAudioEffects : AudioEffectIntent

    data object ToggleFullscreen : PlayerScreenIntent

    data class PlaybackErrorOccurred(val isError: Boolean) : PlayerScreenIntent

    data object RetryPlayback : PlayerScreenIntent

    data class TracksAvailable(val tracks: List<PlayerTrack>) : PlayerScreenIntent

    data object ShowTrackSelector : PlayerScreenIntent

    data object HideTrackSelector : PlayerScreenIntent

    data class SelectTrack(val track: PlayerTrack) : PlayerScreenIntent
}
