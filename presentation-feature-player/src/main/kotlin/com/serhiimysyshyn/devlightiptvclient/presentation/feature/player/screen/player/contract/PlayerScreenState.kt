package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract

import androidx.compose.runtime.Immutable
import com.serhiimysyshyn.devlightiptvclient.domain.model.Channel
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer.EqualizerBand
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer.EqualizerPreset
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.model.PlayerTrack

@Immutable
data class PlayerScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val currentChannel: Channel? = null,
    val likedChannels: List<Channel> = emptyList(),
    val currentPreset: EqualizerPreset = EqualizerPreset.NORMAL,
    /** Sheet visibility lives in state, not in a `remember` inside the content composable. */
    val showAudioSettings: Boolean = false,
    /** Empty until the platform hands out an equalizer for the current audio session. */
    val equalizerBands: List<EqualizerBand> = emptyList(),
    /** Platform scale for both effects is 0..1000. */
    val bassBoostStrength: Short = 0,
    val virtualizerStrength: Short = 0,
    /**
     * Set when ExoPlayer reports a playback error. Distinct from [isError], which covers failures
     * loading channel data — this one is recoverable by re-preparing the same stream.
     */
    val hasPlaybackError: Boolean = false,
    /** Audio and subtitle tracks the current stream exposes; empty for single-track streams. */
    val tracks: List<PlayerTrack> = emptyList(),
    val showTrackSelector: Boolean = false,
    /** User preference from Settings; defaults to on, matching the persisted default. */
    val isPictureInPictureEnabled: Boolean = true,
    /**
     * When true the video fills the whole screen: app bar and the content below the player are
     * hidden, the activity switches to landscape and system bars are hidden.
     */
    val isFullscreen: Boolean = false,
)
