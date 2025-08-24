package com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings.contract

import com.serhiimysyshyn.devlightiptvclient.presentation.base.BaseScreenReducer

class SettingsScreenReducer : BaseScreenReducer<SettingsScreenState, SettingsScreenEvent>()  {
    override fun reduce(
        currentState: SettingsScreenState,
        event: SettingsScreenEvent
    ): SettingsScreenState {
        return when (event) {
            is SettingsScreenEvent.ShowAppColorsDialogEvent -> currentState.copy(
                showAppColorsDialog = true
            )

            is SettingsScreenEvent.HideAppColorsDialogEvent -> currentState.copy(
                showAppColorsDialog = false
            )

            is SettingsScreenEvent.ShowAppThemeDialogEvent -> currentState.copy(
                showChangeThemeDialog = true
            )

            is SettingsScreenEvent.HideAppThemeDialogEvent -> currentState.copy(
                showChangeThemeDialog = false
            )

            is SettingsScreenEvent.LoadAppThemeSuccessEvent -> currentState.copy(
                isLoading = false,
                isError = false,
                currentAppTheme = event.appTheme,
            )

            is SettingsScreenEvent.UpdateAppThemeEvent -> currentState.copy(
                isLoading = false,
                isError = false,
                currentAppTheme = event.appTheme,
            )
        }
    }
}