package com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings.contract

import com.serhiimysyshyn.devlightiptvclient.presentation.theme.AppThemeType

sealed class SettingsScreenIntent {

    data object ShowAppColorsDialog: SettingsScreenIntent()

    data object HideAppColorsDialog: SettingsScreenIntent()

    data object ShowChangeThemeDialog: SettingsScreenIntent()

    data object HideChangeThemeDialog: SettingsScreenIntent()

    data class UpdateAppTheme(
        val appThemeType: AppThemeType
    ): SettingsScreenIntent()
}