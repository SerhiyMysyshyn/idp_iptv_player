package com.serhiimysyshyn.devlightiptvclient.data.models

import androidx.compose.runtime.Immutable
import java.io.Serializable

@Immutable
data class Playlist(
    val id: Long = 0L,
    val name: String,
    val url: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis()
): Serializable

