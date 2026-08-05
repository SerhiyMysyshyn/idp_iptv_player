package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.playlists.contract

sealed interface PlaylistsScreenIntent {

    data object LoadPlaylistsFromDatabase : PlaylistsScreenIntent

    data class DownloadPlaylist(val playlistUrl: String) : PlaylistsScreenIntent
}
