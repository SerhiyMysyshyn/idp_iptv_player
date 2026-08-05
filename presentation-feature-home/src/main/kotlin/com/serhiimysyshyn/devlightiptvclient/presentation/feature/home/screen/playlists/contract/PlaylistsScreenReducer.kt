package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.playlists.contract

import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.base.reducer.BaseScreenReducer

class PlaylistsScreenReducer : BaseScreenReducer<PlaylistsScreenState, PlaylistsScreenEvent>() {

    override fun reduce(
        currentState: PlaylistsScreenState,
        event: PlaylistsScreenEvent,
    ): PlaylistsScreenState = when (event) {
        is PlaylistsScreenEvent.Loading -> currentState.copy(
            isLoading = true,
            isError = false,
        )

        is PlaylistsScreenEvent.Success -> currentState.copy(
            isLoading = false,
            isError = false,
            playlists = event.playlists,
        )

        is PlaylistsScreenEvent.Error -> currentState.copy(
            isLoading = false,
            isError = true,
            playlists = emptyList(),
        )
    }
}
