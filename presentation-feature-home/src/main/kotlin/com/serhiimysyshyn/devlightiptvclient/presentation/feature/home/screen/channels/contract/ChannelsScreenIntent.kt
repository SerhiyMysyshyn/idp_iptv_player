package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.channels.contract

/** Everything the channels screen can be asked to do. */
sealed interface ChannelsScreenIntent {

    data class LoadChannelsFromDatabase(val playlistId: Long) : ChannelsScreenIntent

    data object LoadFavouritesChannelsFromDatabase : ChannelsScreenIntent

    data class AddToFavourite(val channelId: Long) : ChannelsScreenIntent

    data class RemoveFromFavourite(val channelId: Long) : ChannelsScreenIntent

    data class Search(val query: String) : ChannelsScreenIntent
}
