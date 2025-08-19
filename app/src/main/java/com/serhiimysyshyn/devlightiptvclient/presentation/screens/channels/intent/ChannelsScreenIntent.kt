package com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.intent

sealed class ChannelsScreenIntent {

    data class LoadChannelsFromDatabase(val playlistId: Long) : ChannelsScreenIntent()
    data object LoadFavouritesChannelsFromDatabase : ChannelsScreenIntent()
    data class AddToFavourite(val channelId: Long) : ChannelsScreenIntent()
    data class RemoveFromFavourite(val channelId: Long) : ChannelsScreenIntent()
}