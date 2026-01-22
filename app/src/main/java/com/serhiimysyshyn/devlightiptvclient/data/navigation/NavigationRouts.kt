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
        val playlistId: Long,
    ) : NavigationRoute() {
        companion object {
            const val DESTINATION = "channels/{launchMode}/{playlistId}"
        }

        val route: String get() = "channels/${launchMode.type}/$playlistId"
    }

    data class Player(
        val channelId: Long,
    ) : NavigationRoute() {

        companion object {
            const val DESTINATION = "player/{channelId}"
        }

        val route: String get() = "player/$channelId"
    }

    data object Settings : NavigationRoute() {
        const val DESTINATION = "settings"
    }
}