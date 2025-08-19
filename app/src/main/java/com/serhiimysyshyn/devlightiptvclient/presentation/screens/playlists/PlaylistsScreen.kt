package com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.serhiimysyshyn.devlightiptvclient.data.models.Playlist
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaylistsScreen(
    modifier: Modifier = Modifier,
    onPlaylistClicked: (Playlist) -> Unit
) {

    val viewModel: PlaylistsViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    PlaylistContent(
        modifier = modifier,
        state = state,
        onPlaylistClicked = onPlaylistClicked
    )
}