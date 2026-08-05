package com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.contract

import androidx.compose.runtime.Immutable
import com.serhiimysyshyn.devlightiptvclient.domain.model.AppThemeType

@Immutable
data class SettingsScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val showAppColorsDialog: Boolean = false,
    val showChangeThemeDialog: Boolean = false,
    val currentAppTheme: AppThemeType = AppThemeType.SYSTEM,
    /** Defaults to on, matching the stored preference's default. */
    val isPictureInPictureEnabled: Boolean = true,
)
