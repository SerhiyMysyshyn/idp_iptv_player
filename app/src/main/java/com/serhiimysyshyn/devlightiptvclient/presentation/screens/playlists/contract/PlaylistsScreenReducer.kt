package com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.contract

import com.serhiimysyshyn.devlightiptvclient.presentation.base.BaseScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.contract.PlaylistsScreenState

class PlaylistsScreenReducer : BaseScreenReducer<PlaylistsScreenState, PlaylistsScreenEvent>() {

    override fun reduce(currentState: PlaylistsScreenState, event: PlaylistsScreenEvent): PlaylistsScreenState {
        return when (event) {
            is PlaylistsScreenEvent.Success -> currentState.copy(
                isLoading = false,
                isError = false,
                playlists = event.playlists
            )
            is PlaylistsScreenEvent.Error -> currentState.copy(
                isLoading = false,
                isError = true,
                playlists = emptyList()
            )
        }
    }
}