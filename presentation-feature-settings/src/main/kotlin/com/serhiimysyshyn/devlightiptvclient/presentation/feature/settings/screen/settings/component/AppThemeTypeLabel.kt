package com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.serhiimysyshyn.devlightiptvclient.domain.model.AppThemeType
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.R as CoreUiR

/**
 * The user-facing label for a theme.
 *
 * Lives here rather than as a `displayName` property on the enum: the domain must not carry
 * localised strings, and a string resource can be translated while an enum constant cannot.
 */
@Composable
internal fun AppThemeType.label(): String = stringResource(
    when (this) {
        AppThemeType.LIGHT -> CoreUiR.string.theme_light
        AppThemeType.DARK -> CoreUiR.string.theme_dark
        AppThemeType.SYSTEM -> CoreUiR.string.theme_system
    },
)
