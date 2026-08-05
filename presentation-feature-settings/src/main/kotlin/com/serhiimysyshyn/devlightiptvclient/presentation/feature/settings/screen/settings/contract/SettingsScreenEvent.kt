package com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.contract

import com.serhiimysyshyn.devlightiptvclient.domain.model.AppThemeType

sealed interface SettingsScreenEvent {

    data object ShowAppColorsDialog : SettingsScreenEvent

    data object HideAppColorsDialog : SettingsScreenEvent

    data object ShowAppThemeDialog : SettingsScreenEvent

    data object HideAppThemeDialog : SettingsScreenEvent

    data class AppThemeLoaded(val appTheme: AppThemeType) : SettingsScreenEvent

    data object Error : SettingsScreenEvent
}
