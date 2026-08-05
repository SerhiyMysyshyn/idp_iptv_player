package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.playlists

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.serhiimysyshyn.devlightiptvclient.domain.model.Playlist
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun PlaylistsScreen(
    onPlaylistClicked: (Playlist) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: PlaylistsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    PlaylistsContent(
        state = state,
        onPlaylistClicked = onPlaylistClicked,
        modifier = modifier,
    )
}
