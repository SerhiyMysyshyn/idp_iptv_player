package com.serhiimysyshyn.devlightiptvclient.domain.model

/**
 * A single playable IPTV channel.
 *
 * Immutable by construction; Compose treats it as stable via `config/compose/stability.conf`.
 */
data class Channel(
    val id: Long = 0L,
    val name: String,
    val url: String,
    val category: String,
    /** `tvg-logo` from the playlist; empty when the channel shipped none. */
    val logoUrl: String = "",
    val isFavorite: Boolean = false,
    val playlistId: Long = 0L,
)
