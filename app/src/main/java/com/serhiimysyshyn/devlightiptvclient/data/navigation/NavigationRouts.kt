package com.serhiimysyshyn.devlightiptvclient.data.navigation

import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.utils.LaunchMode

sealed class NavigationRoute() {

    data object Main : NavigationRoute() {
        const val DESTINATION = "main"
    }

    data object Playlists : NavigationRoute() {
        const val DESTINATION = "playlists"
    }

    data class Channels(
        val launchMode: LaunchMode,
        val playlistId: Long
    ) : NavigationRoute() {
        companion object {
            const val DESTINATION = "channels/{launchMode}/{playlistId}"
        }

        val route: String get() = "channels/${launchMode.type}/$playlistId"
    }

    data class Player(
        val channelId: Long,
        val videoUrl: String
    ) : NavigationRoute() {

        companion object {
            const val DESTINATION = "player/{channelId}/{videoUrl}"
        }

        val route: String get() = "player/$channelId/$videoUrl"
    }

    data object Settings : NavigationRoute() {
        const val DESTINATION = "settings"
    }
}