package com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.contract

import com.serhiimysyshyn.devlightiptvclient.presentation.base.BaseScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.contract.MainScreenState

class MainScreenReducer : BaseScreenReducer<MainScreenState, MainScreenEvent>() {

    override fun reduce(currentState: MainScreenState, event: MainScreenEvent): MainScreenState {
        return when (event) {
            is MainScreenEvent.ShowAddNewPlaylistDialog -> currentState.copy(
                showAddNewPlayListDialog = true
            )
            is MainScreenEvent.HideAddNewPlaylistDialog -> currentState.copy(
                showAddNewPlayListDialog = false
            )
            is MainScreenEvent.UpdateMenuItemIndex -> currentState.copy(
                selectedMenuItemIndex = event.newIndex
            )
        }
    }
}