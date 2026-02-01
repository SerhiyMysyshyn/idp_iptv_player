package com.serhiimysyshyn.devlightiptvclient.data.models

import androidx.compose.runtime.Stable
import java.io.Serializable

@Stable
data class Channel(
    val id: Long = 0L,
    val name: String,
    val url: String,
    val category: String,
    val isFavorite: Boolean = false,
    val playlistId: Long = 0L
): Serializable