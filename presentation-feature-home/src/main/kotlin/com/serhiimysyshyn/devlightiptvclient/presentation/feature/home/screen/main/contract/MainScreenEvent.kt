package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.contract

sealed interface MainScreenEvent {

    data object ShowAddNewPlaylistDialog : MainScreenEvent

    data object HideAddNewPlaylistDialog : MainScreenEvent

    data class UpdateMenuItemIndex(val newIndex: Long) : MainScreenEvent
}
