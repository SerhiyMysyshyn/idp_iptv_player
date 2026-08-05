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
    val isFavorite: Boolean = false,
    val playlistId: Long = 0L,
)
