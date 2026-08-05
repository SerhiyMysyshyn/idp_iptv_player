package com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.contract

import com.serhiimysyshyn.devlightiptvclient.domain.model.AppThemeType

sealed interface SettingsScreenIntent {

    data object ShowAppColorsDialog : SettingsScreenIntent

    data object HideAppColorsDialog : SettingsScreenIntent

    data object ShowChangeThemeDialog : SettingsScreenIntent

    data object HideChangeThemeDialog : SettingsScreenIntent

    data class UpdateAppTheme(val appThemeType: AppThemeType) : SettingsScreenIntent

    data class UpdatePictureInPictureEnabled(val isEnabled: Boolean) : SettingsScreenIntent
}
