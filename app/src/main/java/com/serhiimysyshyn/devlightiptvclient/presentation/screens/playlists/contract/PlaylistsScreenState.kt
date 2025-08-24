package com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.contract

import com.serhiimysyshyn.devlightiptvclient.data.models.Playlist

data class PlaylistsScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val playlists: List<Playlist> = emptyList()
)