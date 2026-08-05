package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.channels

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.res.ResourcesCompat
import com.serhiimysyshyn.devlightiptvclient.domain.model.Channel
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.preview.DevicePreviews
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.theme.AppTheme
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.component.list.CustomListItemV1
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.component.textfield.SearchTextField
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.channels.contract.ChannelsScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.channels.contract.ChannelsScreenState
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.R as CoreUiR

/**
 * Stateless rendering of the channels list.
 *
 * Takes plain data and an intent sink, never the ViewModel — which is what makes the previews
 * below possible and keeps the composable testable in isolation.
 */
@Composable
internal fun ChannelsContent(
    state: ChannelsScreenState,
    onIntent: (ChannelsScreenIntent) -> Unit,
    onChannelClicked: (Channel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when {
            state.isLoading -> CircularProgressIndicator(
                color = Theme.colors.semantic.foreground.accent,
                strokeWidth = Theme.size.progressStroke,
            )

            state.isError -> Message(text = stringResource(CoreUiR.string.fetching_channels_error))

            state.allChannels.isEmpty() -> Message(text = stringResource(CoreUiR.string.channels_empty))

            else -> ChannelsList(
                state = state,
                onIntent = onIntent,
                onChannelClicked = onChannelClicked,
            )
        }
    }
}

@Composable
private fun ChannelsList(
    state: ChannelsScreenState,
    onIntent: (ChannelsScreenIntent) -> Unit,
    onChannelClicked: (Channel) -> Unit,
) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        SearchTextField(
            value = state.query,
            placeholder = stringResource(CoreUiR.string.search_hint),
            onValueChange = { query -> onIntent(ChannelsScreenIntent.Search(query)) },
        )

        // A query that matches nothing is not the same as an empty playlist — say so instead of
        // showing a blank list.
        if (state.filteredChannels.isEmpty()) {
            Message(text = stringResource(CoreUiR.string.channels_empty))
            return@Column
        }

        LazyColumn(
            contentPadding = PaddingValues(Theme.spacing.m),
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.m),
        ) {
            items(
                items = state.filteredChannels,
                key = { channel -> channel.id },
            ) { channel ->
                CustomListItemV1(
                    title = channel.name,
                    icon = ResourcesCompat.getDrawable(
                        context.resources,
                        CoreUiR.drawable.outline_media_link_24,
                        context.theme,
                    ),
                    onItemClicked = { onChannelClicked(channel) },
                    functionalIcon = if (channel.isFavorite) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    onFunctionalIconClicked = {
                        onIntent(
                            if (channel.isFavorite) {
                                ChannelsScreenIntent.RemoveFromFavourite(channel.id)
                            } else {
                                ChannelsScreenIntent.AddToFavourite(channel.id)
                            },
                        )
                    },
                )
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
private fun ChannelsContentPreview() {
    AppTheme {
        ChannelsContent(
            state = ChannelsScreenState(
                isLoading = false,
                allChannels = previewChannels,
                filteredChannels = previewChannels,
            ),
            onIntent = {},
            onChannelClicked = {},
        )
    }
}

private val previewChannels = listOf(
    Channel(id = 1, name = "Перший канал", url = "", category = "Новини", isFavorite = true),
    Channel(id = 2, name = "Кіно+", url = "", category = "Фільми"),
)
