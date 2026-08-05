package com.serhiimysyshyn.devlightiptvclient.data.repository

import android.net.Uri
import com.serhiimysyshyn.devlightiptvclient.data.core.M3UParser
import com.serhiimysyshyn.devlightiptvclient.data.database.dao.ChannelDao
import com.serhiimysyshyn.devlightiptvclient.data.database.dao.PlaylistDao
import com.serhiimysyshyn.devlightiptvclient.data.mappers.toDomain
import com.serhiimysyshyn.devlightiptvclient.data.mappers.toDomainList
import com.serhiimysyshyn.devlightiptvclient.data.mappers.toEntity
import com.serhiimysyshyn.devlightiptvclient.domain.model.Channel
import com.serhiimysyshyn.devlightiptvclient.domain.model.Playlist
import com.serhiimysyshyn.devlightiptvclient.domain.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

internal class MainRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val channelDao: ChannelDao,
    private val httpClient: OkHttpClient,
) : MainRepository {

    override suspend fun downloadM3UPlaylist(url: String) = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(url).build()
        val content = httpClient.newCall(request).execute().use { response ->
            response.body?.string()
        }

        if (content.isNullOrBlank()) return@withContext

        val channels = M3UParser.parse(content)
        if (channels.isEmpty()) return@withContext

        val playlistName = Uri.parse(url).lastPathSegment
            ?.removeSuffix(M3U_EXTENSION)
            ?.takeIf { it.isNotBlank() }
            ?: channels.first().category

        val playlistId = playlistDao.insertPlaylist(
            Playlist(name = playlistName, url = url).toEntity(),
        )

        channelDao.insertChannels(
            channels.map { channel -> channel.copy(playlistId = playlistId).toEntity() },
        )
    }

    override fun getPlaylists(): Flow<List<Playlist>> =
        playlistDao.getAllPlaylists().map { it.toDomainList() }

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistDao.insertPlaylist(playlist.toEntity())
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.deletePlaylist(playlist.toEntity())
    }

    override suspend fun clearAll() {
        playlistDao.clearAll()
    }

    override fun getChannelsByPlaylistId(playlistId: Long): Flow<List<Channel>> =
        channelDao.getChannelsByPlaylistId(playlistId).map { it.toDomainList() }

    override fun getFavouriteChannels(): Flow<List<Channel>> =
        channelDao.getFavoriteChannels().map { it.toDomainList() }

    override suspend fun addChannelToFavourite(channelId: Long) {
        channelDao.updateIsFavorite(channelId, isFavorite = true)
    }

    override suspend fun removeChannelFromFavourite(channelId: Long) {
        channelDao.updateIsFavorite(channelId, isFavorite = false)
    }

    override suspend fun getChannelInfoById(channelId: Long): Channel =
        channelDao.getChannelInfoById(channelId).toDomain()

    private companion object {
        const val M3U_EXTENSION = ".m3u"
    }
}
