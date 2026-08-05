package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.model

import androidx.compose.runtime.Immutable

/** Audio or subtitle, the two track types worth offering a picker for. */
enum class PlayerTrackType {
    AUDIO,
    SUBTITLE,
}

/**
 * One selectable track, flattened out of media3's `Tracks.Group` / track-index pair so the UI
 * never touches player types.
 *
 * [groupIndex] and [trackIndex] are what the screen needs to rebuild a `TrackSelectionOverride`
 * when the user picks this entry.
 */
@Immutable
data class PlayerTrack(
    val type: PlayerTrackType,
    val groupIndex: Int,
    val trackIndex: Int,
    /** Language tag or codec name — whatever the stream provided. Never blank. */
    val label: String,
    val isSelected: Boolean,
)
