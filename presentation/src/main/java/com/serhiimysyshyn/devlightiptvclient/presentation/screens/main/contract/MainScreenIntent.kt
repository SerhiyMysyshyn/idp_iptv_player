package com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.contract

sealed class MainScreenIntent {
    data object OpenAddNewPlaylistDialog : MainScreenIntent()
    data object HideAddNewPlaylistDialog : MainScreenIntent()

    data class StartDownloadingNewPlaylist(val url: String) : MainScreenIntent()

    data class LaunchNewScreen(val route: String) : MainScreenIntent()
    data class LaunchNewRootScreen(val route: String) : MainScreenIntent()

    data class UpdateSelectedMenuItemIndex(val newIndex: Long) : MainScreenIntent()
}