package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.playlists.contract

import androidx.compose.runtime.Immutable
import com.serhiimysyshyn.devlightiptvclient.domain.model.Playlist

@Immutable
data class PlaylistsScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val playlists: List<Playlist> = emptyList(),
)
