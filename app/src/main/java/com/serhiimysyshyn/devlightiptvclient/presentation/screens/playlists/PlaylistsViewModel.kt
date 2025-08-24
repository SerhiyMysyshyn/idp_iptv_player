package com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists

import com.serhiimysyshyn.devlightiptvclient.data.repository.MainRepository
import com.serhiimysyshyn.devlightiptvclient.presentation.base.BaseViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.contract.PlaylistsScreenEvent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.contract.PlaylistsScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.contract.PlaylistsScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.contract.PlaylistsScreenState
import com.serhiimysyshyn.devlightiptvclient.presentation.utils.safeLaunch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlaylistsViewModel(
    private val mainRepository: MainRepository,
    private val playlistsScreenReducer: PlaylistsScreenReducer
) : BaseViewModel<PlaylistsScreenIntent>() {

    init {
        processIntent(PlaylistsScreenIntent.LoadPlaylistsFromDatabase)
    }

    private val _state = MutableStateFlow(PlaylistsScreenState())
    val state: StateFlow<PlaylistsScreenState> = _state.asStateFlow()

    override fun processIntent(intent: PlaylistsScreenIntent) {
        when (intent) {
            is PlaylistsScreenIntent.LoadPlaylistsFromDatabase -> loadPlaylistFromDatabase()
            is PlaylistsScreenIntent.DownloadPlaylist -> {
                _state.update { it.copy(isLoading = true) }

                try {
                    downloadPlaylist(intent.playlistUrl)
                } finally {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun loadPlaylistFromDatabase() {
        safeLaunch(
            onError = {
                _state.value = playlistsScreenReducer.reduce(
                    _state.value,
                    PlaylistsScreenEvent.Error
                )
            }
        ) {
            mainRepository.getPlaylists()
                .collect { playlists ->
                    _state.value = playlistsScreenReducer.reduce(
                        _state.value,
                        PlaylistsScreenEvent.Success(playlists)
                    )
                }
        }
    }

    private fun downloadPlaylist(playlistUrl: String) {
        safeLaunch(
            dispatcher = Dispatchers.IO,
            onError = {
                _state.value = playlistsScreenReducer.reduce(
                    _state.value,
                    PlaylistsScreenEvent.Error
                )
            }
        ) {
            mainRepository.downloadM3UPlaylist(playlistUrl)
        }
    }
}