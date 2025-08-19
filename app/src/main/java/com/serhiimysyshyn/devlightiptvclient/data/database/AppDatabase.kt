package com.serhiimysyshyn.devlightiptvclient.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.serhiimysyshyn.devlightiptvclient.data.database.dao.ChannelDao
import com.serhiimysyshyn.devlightiptvclient.data.database.dao.PlaylistDao
import com.serhiimysyshyn.devlightiptvclient.data.database.entities.ChannelEntity
import com.serhiimysyshyn.devlightiptvclient.data.database.entities.PlaylistEntity

@Database(
    entities = [PlaylistEntity::class, ChannelEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playlistDao(): PlaylistDao

    abstract fun channelDao(): ChannelDao
}