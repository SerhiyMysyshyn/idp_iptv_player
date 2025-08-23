package com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.contract

import com.serhiimysyshyn.devlightiptvclient.data.models.Playlist

sealed class PlaylistsScreenEvent {

    data class Success(val playlists: List<Playlist>) : PlaylistsScreenEvent()
    data object Error : PlaylistsScreenEvent()
}