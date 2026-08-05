package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.playlists.contract

import com.serhiimysyshyn.devlightiptvclient.domain.model.Playlist

sealed interface PlaylistsScreenEvent {

    data class Success(val playlists: List<Playlist>) : PlaylistsScreenEvent

    data object Loading : PlaylistsScreenEvent

    data object Error : PlaylistsScreenEvent
}
