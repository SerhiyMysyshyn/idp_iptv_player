package com.serhiimysyshyn.devlightiptvclient.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "channels")
data class ChannelEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val url: String,
    val category: String,
    /** Defaulted so the v1 -> v2 migration can add the column without touching existing rows. */
    val logoUrl: String = "",
    val isFavorite: Boolean,
    val playlistId: Long
)
