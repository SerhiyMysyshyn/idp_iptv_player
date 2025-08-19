package com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists

import androidx.lifecycle.viewModelScope
import com.serhiimysyshyn.devlightiptvclient.data.repository.IMainRepository
import com.serhiimysyshyn.devlightiptvclient.presentation.base.BaseViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.intent.PlaylistsScreenEvent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.intent.PlaylistsScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.intent.PlaylistsScreenReducer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val mainRepository: IMainRepository,
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
        viewModelScope.launch {
            mainRepository.getPlaylists()
                .catch {
                    _state.value = playlistsScreenReducer.reduce(_state.value, PlaylistsScreenEvent.Error)
                }
                .collect { playlists ->
                    _state.value = playlistsScreenReducer.reduce(
                        _state.value,
                        PlaylistsScreenEvent.Success(playlists)
                    )
                }
        }
    }

    private fun downloadPlaylist(playlistUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.downloadM3UPlaylist(playlistUrl)
        }
    }
}