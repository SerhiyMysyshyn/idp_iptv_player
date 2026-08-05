package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.channels.contract

import androidx.compose.runtime.Immutable
import com.serhiimysyshyn.devlightiptvclient.domain.model.Channel

/**
 * @property allChannels everything loaded for the current playlist / favourites.
 * @property filteredChannels what the list actually renders — [allChannels] narrowed by [query].
 */
@Immutable
data class ChannelsScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val query: String = "",
    val allChannels: List<Channel> = emptyList(),
    val filteredChannels: List<Channel> = emptyList(),
)
