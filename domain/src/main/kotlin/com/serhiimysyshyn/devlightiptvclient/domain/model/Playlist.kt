package com.serhiimysyshyn.devlightiptvclient.domain.model

/** A saved M3U playlist and the channels parsed out of it. */
data class Playlist(
    val id: Long = 0L,
    val name: String,
    val url: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis(),
)
