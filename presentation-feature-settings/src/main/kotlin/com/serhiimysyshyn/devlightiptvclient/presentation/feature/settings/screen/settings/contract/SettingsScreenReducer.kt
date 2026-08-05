package com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.contract

import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.base.reducer.BaseScreenReducer

class SettingsScreenReducer : BaseScreenReducer<SettingsScreenState, SettingsScreenEvent>() {

    override fun reduce(
        currentState: SettingsScreenState,
        event: SettingsScreenEvent,
    ): SettingsScreenState = when (event) {
        is SettingsScreenEvent.ShowAppColorsDialog -> currentState.copy(showAppColorsDialog = true)
        is SettingsScreenEvent.HideAppColorsDialog -> currentState.copy(showAppColorsDialog = false)
        is SettingsScreenEvent.ShowAppThemeDialog -> currentState.copy(showChangeThemeDialog = true)
        is SettingsScreenEvent.HideAppThemeDialog -> currentState.copy(showChangeThemeDialog = false)

        // One event for both the initial read and later updates: the theme is observed from
        // DataStore, so a write comes back through the same flow.
        is SettingsScreenEvent.AppThemeLoaded -> currentState.copy(
            isLoading = false,
            isError = false,
            currentAppTheme = event.appTheme,
        )

        is SettingsScreenEvent.Error -> currentState.copy(
            isLoading = false,
            isError = true,
        )
    }
}
