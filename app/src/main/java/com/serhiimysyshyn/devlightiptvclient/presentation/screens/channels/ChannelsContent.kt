package com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.serhiimysyshyn.devlightiptvclient.R
import com.serhiimysyshyn.devlightiptvclient.data.models.Channel
import com.serhiimysyshyn.devlightiptvclient.presentation.composables.molecule.CustomListItemV1
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.intent.ChannelsScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.utils.LaunchMode
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.IPTVClientTheme
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ChannelsContent(
    modifier: Modifier = Modifier,
    launchMode: LaunchMode,
    playlistId: Long,
    onChannelClicked: (Channel) -> Unit
) {
    val context = LocalContext.current
    val viewModel: ChannelsViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    when (launchMode) {
        LaunchMode.LOAD_FROM_PLAYLIST -> {
            viewModel.processIntent(ChannelsScreenIntent.LoadChannelsFromDatabase(playlistId))
        }

        LaunchMode.LOAD_FROM_FAVOURITES -> {
            viewModel.processIntent(ChannelsScreenIntent.LoadFavouritesChannelsFromDatabase)
        }

        LaunchMode.UNKNOWN -> {
            Log.e("MYSYSHYN", "UNKNOWN")
        }
    }

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
                    text = context.getString(R.string.fetching_channels_error),
                    style = IPTVClientTheme.typography.body,
                    color = IPTVClientTheme.colors.primary,
                    modifier = Modifier.padding(16.dp)
                )
            }

            state.channels.isNotEmpty() -> {
                Column(
                    modifier = modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.channels) { channel ->
                            CustomListItemV1(
                                title = channel.name,
                                icon = ResourcesCompat.getDrawable(
                                    context.resources,
                                    R.drawable.outline_media_link_24,
                                    context.theme
                                ),
                                onItemClicked = {
                                    onChannelClicked.invoke(channel)
                                },
                                functionalIcon = if (channel.isFavorite) {
                                    Icons.Default.Favorite
                                } else {
                                    Icons.Default.FavoriteBorder
                                },
                                onFunctionalIconClicked = {
                                    if (channel.isFavorite) {
                                        viewModel.processIntent(ChannelsScreenIntent.RemoveFromFavourite(channel.id))
                                    } else {
                                        viewModel.processIntent(ChannelsScreenIntent.AddToFavourite(channel.id))
                                    }
                                }
                            )
                        }
                    }
                }
            }

            else -> {
                Text(
                    text = context.getString(R.string.channels_empty),
                    style = IPTVClientTheme.typography.body,
                    color = IPTVClientTheme.colors.primary,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}