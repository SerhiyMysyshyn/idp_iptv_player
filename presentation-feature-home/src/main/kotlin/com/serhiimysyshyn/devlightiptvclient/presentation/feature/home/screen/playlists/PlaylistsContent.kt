package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.playlists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.res.ResourcesCompat
import com.serhiimysyshyn.devlightiptvclient.domain.model.Playlist
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.preview.DevicePreviews
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.theme.AppTheme
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.component.list.CustomListItemV1
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.playlists.contract.PlaylistsScreenState
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.R as CoreUiR

@Composable
internal fun PlaylistsContent(
    state: PlaylistsScreenState,
    onPlaylistClicked: (Playlist) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when {
            state.isLoading -> CircularProgressIndicator(
                color = Theme.colors.semantic.foreground.accent,
                strokeWidth = Theme.size.progressStroke,
            )

            state.isError -> Message(text = stringResource(CoreUiR.string.fetching_playlists_error))

            state.playlists.isEmpty() -> Message(text = stringResource(CoreUiR.string.playlists_empty))

            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(Theme.spacing.m),
                verticalArrangement = Arrangement.spacedBy(Theme.spacing.m),
            ) {
                items(
                    items = state.playlists,
                    key = { playlist -> playlist.id },
                ) { playlist ->
                    CustomListItemV1(
                        title = playlist.name,
                        description = playlist.description,
                        icon = ResourcesCompat.getDrawable(
                            context.resources,
                            CoreUiR.drawable.outline_folder_open_24,
                            context.theme,
                        ),
                        onItemClicked = { onPlaylistClicked(playlist) },
                    )
                }
            }
        }
    }
}

@Composable
private fun Message(text: String) {
    Text(
        text = text,
        style = Theme.typography.body,
        color = Theme.colors.semantic.text.primary,
        modifier = Modifier.padding(Theme.spacing.m),
    )
}

@DevicePreviews
@Composable
private fun PlaylistsContentPreview() {
    AppTheme {
        PlaylistsContent(
            state = PlaylistsScreenState(
                isLoading = false,
                playlists = listOf(
                    Playlist(id = 1, name = "Кіно+", url = ""),
                    Playlist(id = 2, name = "Новини", url = ""),
                ),
            ),
            onPlaylistClicked = {},
        )
    }
}
