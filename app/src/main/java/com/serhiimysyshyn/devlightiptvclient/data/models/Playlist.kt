package com.serhiimysyshyn.devlightiptvclient.data.models

import java.io.Serializable

data class Playlist(
    val id: Long = 0L,
    val name: String,
    val url: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis()
): Serializable

