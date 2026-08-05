package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.contract

sealed interface MainScreenIntent {

    data object OpenAddNewPlaylistDialog : MainScreenIntent

    data object HideAddNewPlaylistDialog : MainScreenIntent

    data class StartDownloadingNewPlaylist(val url: String) : MainScreenIntent

    /** Navigate inside the main screen's own NavHost. */
    data class LaunchNewScreen(val route: String) : MainScreenIntent

    /** Navigate on the root NavHost — a full-screen destination such as player or settings. */
    data class LaunchNewRootScreen(val route: String) : MainScreenIntent

    data class UpdateSelectedMenuItemIndex(val newIndex: Long) : MainScreenIntent
}
