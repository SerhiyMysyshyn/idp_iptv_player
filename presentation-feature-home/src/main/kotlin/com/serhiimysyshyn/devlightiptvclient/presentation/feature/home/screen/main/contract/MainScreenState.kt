package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.contract

import androidx.compose.runtime.Immutable
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.model.MainMenuItemId

@Immutable
data class MainScreenState(
    val showAddNewPlayListDialog: Boolean = false,
    val selectedMenuItemIndex: Long = MainMenuItemId.PLAYLISTS,
)
