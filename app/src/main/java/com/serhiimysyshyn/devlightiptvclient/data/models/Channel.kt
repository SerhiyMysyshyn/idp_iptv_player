package com.serhiimysyshyn.devlightiptvclient.data.models

import java.io.Serializable

data class Channel(
    val id: Long = 0L,
    val name: String,
    val url: String,
    val category: String,
    val isFavorite: Boolean = false,
    val playlistId: Long = 0L
): Serializable