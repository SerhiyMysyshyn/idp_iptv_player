package com.serhiimysyshyn.devlightiptvclient.data.repository

import android.net.Uri
import com.serhiimysyshyn.devlightiptvclient.data.database.dao.ChannelDao
import com.serhiimysyshyn.devlightiptvclient.data.database.dao.PlaylistDao
import com.serhiimysyshyn.devlightiptvclient.data.mappers.toEntity
import com.serhiimysyshyn.devlightiptvclient.data.mappers.toUIModel
import com.serhiimysyshyn.devlightiptvclient.data.mappers.toUIModelList
import com.serhiimysyshyn.devlightiptvclient.data.models.Channel
import com.serhiimysyshyn.devlightiptvclient.data.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.collections.firstOrNull
import kotlin.collections.isNotEmpty
import kotlin.collections.map

interface MainRepository {
    suspend fun downloadM3UPlaylist(url: String)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun clearAll()

    suspend fun getChannelsByPlaylistId(playlistId: Long): Flow<List<Channel>>

    suspend fun getFavouriteChannels(): Flow<List<Channel>>

    suspend fun addChannelToFavourite(channelId: Long)

    suspend fun removeChannelFromFavourite(channelId: Long)

    suspend fun getChannelInfoById(channelId: Long): Channel
}

class MainRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val channelDao: ChannelDao
): MainRepository {

    override suspend fun downloadM3UPlaylist(url: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val content = response.body?.string()

        if (!content.isNullOrBlank()) {
            val channels = parseM3U(content)

            if (channels.isNotEmpty()) {
                // 1. Витягнути назву плейлиста (наприклад, з першого каналу або з URL)
                val playlistName = Uri.parse(url).lastPathSegment?.removeSuffix(".m3u")?.ifBlank {
                    channels.firstOrNull()?.category
                } ?: "Новий плейлист"

                // 2. Створити плейлист і зберегти його у БД
                val playlist = Playlist(
                    name = playlistName,
                    url = url
                )
                val playlistId = playlistDao.insertPlaylist(playlist.toEntity())

                // 3. Прив'язати канали до плейлиста
                val channelEntities = channels.map {
                    it.copy(playlistId = playlistId).toEntity()
                }

                // 4. Зберегти канали у БД
                channelDao.insertChannels(channelEntities)
            }
        }
    }

    private fun parseM3U(content: String): List<Channel> {
        val lines = content.lines()
        val channels = mutableListOf<Channel>()

        var currentName = ""
        var currentCategory = ""

        for (i in lines.indices) {
            val line = lines[i]
            if (line.startsWith("#EXTINF")) {
                val nameMatch = Regex("tvg-name=\"(.*?)\"").find(line)
                val categoryMatch = Regex("group-title=\"(.*?)\"").find(line)
                val nameFallback = line.substringAfter(",").trim()

                currentName = nameMatch?.groupValues?.get(1) ?: nameFallback
                currentCategory = categoryMatch?.groupValues?.get(1) ?: "Без категорії"
            } else if (line.startsWith("http")) {
                channels.add(
                    Channel(
                        name = currentName,
                        url = line.trim(),
                        category = currentCategory
                    )
                )
            }
        }
        return channels
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylists().map { it.toUIModelList() }
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistDao.insertPlaylist(playlist.toEntity())
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.deletePlaylist(playlist.toEntity())
    }

    override suspend fun clearAll() {
        playlistDao.clearAll()
    }

    override suspend fun getChannelsByPlaylistId(playlistId: Long): Flow<List<Channel>> {
        return channelDao.getChannelsByPlaylistId(playlistId).map { it.toUIModelList() }
    }

    override suspend fun getFavouriteChannels(): Flow<List<Channel>> {
        return channelDao.getFavoriteChannels().map { it.toUIModelList() }
    }

    override suspend fun addChannelToFavourite(channelId: Long) {
        return channelDao.updateIsFavorite(channelId, true)
    }

    override suspend fun removeChannelFromFavourite(channelId: Long) {
        return channelDao.updateIsFavorite(channelId, false)
    }

    override suspend fun getChannelInfoById(channelId: Long): Channel {
        return channelDao.getChannelInfoById(channelId).toUIModel()
    }
}