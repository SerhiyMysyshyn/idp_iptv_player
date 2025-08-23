package com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.contract

sealed class PlaylistsScreenIntent {

    data object LoadPlaylistsFromDatabase : PlaylistsScreenIntent()

    data class DownloadPlaylist(val playlistUrl: String) : PlaylistsScreenIntent()
}