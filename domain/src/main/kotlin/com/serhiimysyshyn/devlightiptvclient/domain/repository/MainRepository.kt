package com.serhiimysyshyn.devlightiptvclient.domain.repository

import com.serhiimysyshyn.devlightiptvclient.domain.model.Channel
import com.serhiimysyshyn.devlightiptvclient.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

/**
 * Playlist and channel storage.
 *
 * Flow-returning functions are intentionally **not** `suspend`: building a cold Flow does no work,
 * so marking them `suspend` would only force callers into a coroutine to get the stream handle.
 */
interface MainRepository {

    suspend fun downloadM3UPlaylist(url: String)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun clearAll()

    fun getChannelsByPlaylistId(playlistId: Long): Flow<List<Channel>>

    fun getFavouriteChannels(): Flow<List<Channel>>

    suspend fun addChannelToFavourite(channelId: Long)

    suspend fun removeChannelFromFavourite(channelId: Long)

    suspend fun getChannelInfoById(channelId: Long): Channel
}
