package com.serhiimysyshyn.devlightiptvclient.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.serhiimysyshyn.devlightiptvclient.data.database.entities.ChannelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannels(channels: List<ChannelEntity>)

    @Query("SELECT * FROM channels WHERE playlistId = :playlistId")
    fun getChannelsByPlaylistId(playlistId: Long): Flow<List<ChannelEntity>>

    @Query("SELECT * FROM channels WHERE isFavorite = 1")
    fun getFavoriteChannels(): Flow<List<ChannelEntity>>

    @Query("DELETE FROM channels WHERE playlistId = :playlistId")
    suspend fun deleteChannelsByPlaylistId(playlistId: Long)

    @Query("SELECT * FROM channels")
    suspend fun getAllChannels(): List<ChannelEntity>

    @Query("DELETE FROM channels")
    suspend fun clearAllChannels()

    @Query("UPDATE channels SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateIsFavorite(id: Long, isFavorite: Boolean)

    @Query("SELECT * FROM channels WHERE id = :id")
    suspend fun getChannelInfoById(id: Long): ChannelEntity
}