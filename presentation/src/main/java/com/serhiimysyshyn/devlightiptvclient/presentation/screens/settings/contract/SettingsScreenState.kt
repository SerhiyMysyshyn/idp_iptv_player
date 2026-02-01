package com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings.contract

import androidx.compose.runtime.Immutable
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.core.AppThemeType

@Immutable
data class SettingsScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val showAppColorsDialog: Boolean = false,
    val showChangeThemeDialog: Boolean = false,
    val currentAppTheme: AppThemeType = AppThemeType.SYSTEM,
)