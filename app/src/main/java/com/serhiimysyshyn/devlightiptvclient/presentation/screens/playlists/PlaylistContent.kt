package com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.serhiimysyshyn.devlightiptvclient.R
import com.serhiimysyshyn.devlightiptvclient.data.models.Playlist
import com.serhiimysyshyn.devlightiptvclient.presentation.composables.molecule.CustomListItemV1
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.IPTVClientTheme

@Composable
internal fun PlaylistContent(
    modifier: Modifier = Modifier,
    state: PlaylistsScreenState,
    onPlaylistClicked: (Playlist) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    color = IPTVClientTheme.colors.surface,
                    strokeWidth = 4.dp,
                )
            }

            state.isError -> {
                Text(
                    text = context.getString(R.string.fetching_playlists_error),
                    style = IPTVClientTheme.typography.body,
                    color = IPTVClientTheme.colors.primary,
                    modifier = Modifier.padding(16.dp)
                )
            }

            state.playlists.isNotEmpty() -> {
                Column(
                    modifier = modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.playlists) { playlist ->
                            CustomListItemV1(
                                title = playlist.name,
                                icon = ResourcesCompat.getDrawable(context.resources, R.drawable.outline_folder_open_24, context.theme),
                                onItemClicked = { onPlaylistClicked.invoke(playlist) },
                                functionalIcon = Icons.Default.FavoriteBorder
                            )
                        }
                    }
                }
            }

            else -> {
                Text(
                    text = context.getString(R.string.playlists_empty),
                    style = IPTVClientTheme.typography.body,
                    color = IPTVClientTheme.colors.primary,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}