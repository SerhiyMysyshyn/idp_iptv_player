package com.serhiimysyshyn.devlightiptvclient.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val url: String,
    val description: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)