package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.playlists

import com.serhiimysyshyn.devlightiptvclient.domain.repository.MainRepository
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.base.viewmodel.BaseViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.ext.safeLaunch
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.playlists.contract.PlaylistsScreenEvent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.playlists.contract.PlaylistsScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.playlists.contract.PlaylistsScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.playlists.contract.PlaylistsScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlaylistsViewModel(
    private val mainRepository: MainRepository,
    private val reducer: PlaylistsScreenReducer,
) : BaseViewModel<PlaylistsScreenIntent>() {

    private val _state = MutableStateFlow(PlaylistsScreenState())
    val state: StateFlow<PlaylistsScreenState> = _state.asStateFlow()

    init {
        processIntent(PlaylistsScreenIntent.LoadPlaylistsFromDatabase)
    }

    override fun processIntent(intent: PlaylistsScreenIntent) {
        when (intent) {
            is PlaylistsScreenIntent.LoadPlaylistsFromDatabase -> observePlaylists()
            is PlaylistsScreenIntent.DownloadPlaylist -> downloadPlaylist(intent.playlistUrl)
        }
    }

    private fun observePlaylists() {
        safeLaunch(onError = { emit(PlaylistsScreenEvent.Error) }) {
            mainRepository.getPlaylists().collect { playlists ->
                emit(PlaylistsScreenEvent.Success(playlists))
            }
        }
    }

    private fun downloadPlaylist(playlistUrl: String) {
        emit(PlaylistsScreenEvent.Loading)

        // No `finally { stopLoading() }` here: the download writes to the database, and the
        // `observePlaylists` subscription reports success once the new rows land. Clearing the
        // flag eagerly would hide the spinner before the list actually updates.
        safeLaunch(
            dispatcher = Dispatchers.IO,
            onError = { emit(PlaylistsScreenEvent.Error) },
        ) {
            mainRepository.downloadM3UPlaylist(playlistUrl)
        }
    }

    private fun emit(event: PlaylistsScreenEvent) {
        _state.value = reducer.reduce(_state.value, event)
    }
}
