package com.serhiimysyshyn.devlightiptvclient.data.models

enum class LaunchMode(val type: Int) {
    UNKNOWN(-1),
    LOAD_FROM_PLAYLIST(0),
    LOAD_FROM_FAVOURITES(1);

    companion object {
        fun fromInt(value: Int): LaunchMode {
            return entries.find { it.type == value } ?: UNKNOWN
        }
    }
}