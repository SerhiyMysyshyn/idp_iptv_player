package com.serhiimysyshyn.devlightiptvclient.data.repository

import com.serhiimysyshyn.devlightiptvclient.data.models.Channel
import com.serhiimysyshyn.devlightiptvclient.data.models.Playlist
import kotlinx.coroutines.flow.Flow

interface IMainRepository {
    suspend fun downloadM3UPlaylist(url: String)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun clearAll()

    suspend fun getChannelsByPlaylistId(playlistId: Long): Flow<List<Channel>>

    suspend fun getFavouriteChannels(): Flow<List<Channel>>

    suspend fun addChannelToFavourite(channelId: Long)

    suspend fun removeChannelFromFavourite(channelId: Long)
}