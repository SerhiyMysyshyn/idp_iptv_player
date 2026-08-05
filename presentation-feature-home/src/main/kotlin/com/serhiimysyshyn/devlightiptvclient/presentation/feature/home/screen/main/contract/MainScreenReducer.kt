package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.contract

import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.base.reducer.BaseScreenReducer

class MainScreenReducer : BaseScreenReducer<MainScreenState, MainScreenEvent>() {

    override fun reduce(
        currentState: MainScreenState,
        event: MainScreenEvent,
    ): MainScreenState = when (event) {
        is MainScreenEvent.ShowAddNewPlaylistDialog -> currentState.copy(
            showAddNewPlayListDialog = true,
        )

        is MainScreenEvent.HideAddNewPlaylistDialog -> currentState.copy(
            showAddNewPlayListDialog = false,
        )

        is MainScreenEvent.UpdateMenuItemIndex -> currentState.copy(
            selectedMenuItemIndex = event.newIndex,
        )
    }
}
