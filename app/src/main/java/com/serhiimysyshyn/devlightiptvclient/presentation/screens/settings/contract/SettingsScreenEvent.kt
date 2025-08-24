package com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings.contract

import com.serhiimysyshyn.devlightiptvclient.presentation.theme.AppThemeType

sealed class SettingsScreenEvent {

    data object ShowAppColorsDialogEvent : SettingsScreenEvent()

    data object HideAppColorsDialogEvent : SettingsScreenEvent()

    data object ShowAppThemeDialogEvent : SettingsScreenEvent()

    data object HideAppThemeDialogEvent : SettingsScreenEvent()

    data class LoadAppThemeSuccessEvent(
        val appTheme: AppThemeType,
    ) : SettingsScreenEvent()

    data class UpdateAppThemeEvent(
        val appTheme: AppThemeType,
    ) : SettingsScreenEvent()
}