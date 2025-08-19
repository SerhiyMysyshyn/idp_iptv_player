package com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.intent

sealed class MainScreenEvent {
    data object ShowAddNewPlaylistDialog : MainScreenEvent()
    data object HideAddNewPlaylistDialog : MainScreenEvent()
    data class UpdateMenuItemIndex(val newIndex: Long) : MainScreenEvent()
}