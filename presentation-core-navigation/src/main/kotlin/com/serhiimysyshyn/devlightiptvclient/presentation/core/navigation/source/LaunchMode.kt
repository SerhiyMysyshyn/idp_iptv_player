package com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.source

/**
 * Why the channels screen was opened — it shows either one playlist's channels or the favourites.
 *
 * This is a navigation argument, not a domain concept, so it lives next to the routes rather than
 * in `:domain`. [type] is the wire value carried in the route path.
 */
enum class LaunchMode(val type: Int) {
    UNKNOWN(-1),
    LOAD_FROM_PLAYLIST(0),
    LOAD_FROM_FAVOURITES(1),
    ;

    companion object {
        fun fromInt(value: Int): LaunchMode = entries.find { it.type == value } ?: UNKNOWN
    }
}
