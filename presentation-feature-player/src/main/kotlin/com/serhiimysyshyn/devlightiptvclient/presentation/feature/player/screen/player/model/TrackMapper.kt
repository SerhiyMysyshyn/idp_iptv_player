package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.model

import androidx.media3.common.C
import androidx.media3.common.Format
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import java.util.Locale

/**
 * Flattens the player's audio and subtitle tracks into [PlayerTrack]s.
 *
 * Unsupported tracks are dropped — offering a track the decoder can't play would just produce
 * silence when picked.
 */
fun Tracks.toPlayerTracks(): List<PlayerTrack> = groups
    .withIndex()
    .filter { (_, group) -> group.type.toTrackType() != null }
    .flatMap { (groupIndex, group) ->
        val type = group.type.toTrackType() ?: return@flatMap emptyList()

        (0 until group.length)
            .filter { trackIndex -> group.isTrackSupported(trackIndex) }
            .map { trackIndex ->
                PlayerTrack(
                    type = type,
                    groupIndex = groupIndex,
                    trackIndex = trackIndex,
                    label = group.getTrackFormat(trackIndex).toTrackLabel(trackIndex),
                    isSelected = group.isTrackSelected(trackIndex),
                )
            }
    }

/** Applies [track] as an override, replacing any previous selection for the same track type. */
fun ExoPlayer.selectTrack(track: PlayerTrack) {
    val group = currentTracks.groups.getOrNull(track.groupIndex) ?: return

    trackSelectionParameters = trackSelectionParameters
        .buildUpon()
        .setOverrideForType(
            TrackSelectionOverride(group.mediaTrackGroup, track.trackIndex),
        )
        .build()
}

private fun Int.toTrackType(): PlayerTrackType? = when (this) {
    C.TRACK_TYPE_AUDIO -> PlayerTrackType.AUDIO
    C.TRACK_TYPE_TEXT -> PlayerTrackType.SUBTITLE
    else -> null
}

/**
 * IPTV streams are inconsistent about track metadata, so fall back through language → label →
 * codec → a bare index rather than showing an empty row.
 */
private fun Format.toTrackLabel(trackIndex: Int): String {
    val displayLanguage = language
        ?.takeIf { it.isNotBlank() && it != C.LANGUAGE_UNDETERMINED }
        ?.let { tag -> Locale.forLanguageTag(tag).displayLanguage.takeIf(String::isNotBlank) }

    return displayLanguage
        ?: label?.takeIf(String::isNotBlank)
        ?: codecs?.takeIf(String::isNotBlank)
        ?: "#${trackIndex + 1}"
}
