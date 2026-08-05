package com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.source

/**
 * Every destination in the app.
 *
 * Living in `presentation-core-navigation` rather than in a feature module is what lets two
 * features navigate to each other without depending on each other — the route is shared, the
 * screens are not. (It used to sit in `:data`, which meant the data layer knew about screens.)
 *
 * `DESTINATION` is the pattern registered with the NavHost; `route` is the concrete filled-in
 * path for an instance.
 */
sealed interface NavigationRoute {

    data object Main : NavigationRoute {
        const val DESTINATION = "main"
    }

    data object Playlists : NavigationRoute {
        const val DESTINATION = "playlists"
    }

    data object Settings : NavigationRoute {
        const val DESTINATION = "settings"
    }

    data class Channels(
        val launchMode: LaunchMode,
        val playlistId: Long,
    ) : NavigationRoute {

        val route: String get() = "channels/${launchMode.type}/$playlistId"

        companion object {
            const val ARG_LAUNCH_MODE = "launchMode"
            const val ARG_PLAYLIST_ID = "playlistId"
            const val DESTINATION = "channels/{$ARG_LAUNCH_MODE}/{$ARG_PLAYLIST_ID}"

            const val NO_PLAYLIST_ID = -1L
        }
    }

    data class Player(
        val channelId: Long,
    ) : NavigationRoute {

        val route: String get() = "player/$channelId"

        companion object {
            const val ARG_CHANNEL_ID = "channelId"
            const val DESTINATION = "player/{$ARG_CHANNEL_ID}"
        }
    }
}
