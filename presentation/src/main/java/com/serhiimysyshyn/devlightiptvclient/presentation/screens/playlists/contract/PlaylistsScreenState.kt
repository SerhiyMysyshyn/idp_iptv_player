package com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.contract

import androidx.compose.runtime.Immutable
import com.serhiimysyshyn.devlightiptvclient.data.models.Playlist

@Immutable
data class PlaylistsScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val playlists: List<Playlist> = emptyList()
)