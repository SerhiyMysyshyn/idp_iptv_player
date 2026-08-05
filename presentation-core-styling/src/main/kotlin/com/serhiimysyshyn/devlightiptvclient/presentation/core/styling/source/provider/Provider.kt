package com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.provider

import androidx.compose.runtime.staticCompositionLocalOf
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeColors
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeRadius
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSize
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSpacing
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeTypography

// These are deliberately `internal`: the only supported entry point is the `Theme` object.
// Keeping the CompositionLocals private to this module means a screen cannot provide its own
// palette halfway down the tree and silently break theming for everything below it.
//
// They also `error(...)` instead of defaulting, so a composable rendered outside `AppTheme { }`
// fails loudly in a preview rather than rendering with a wrong-but-plausible palette.

internal val LocalThemeColors = staticCompositionLocalOf<ThemeColors> {
    error("No ThemeColors provided. Wrap the content in AppTheme { }.")
}

internal val LocalThemeTypography = staticCompositionLocalOf<ThemeTypography> {
    error("No ThemeTypography provided. Wrap the content in AppTheme { }.")
}

internal val LocalThemeSpacing = staticCompositionLocalOf<ThemeSpacing> {
    error("No ThemeSpacing provided. Wrap the content in AppTheme { }.")
}

internal val LocalThemeRadius = staticCompositionLocalOf<ThemeRadius> {
    error("No ThemeRadius provided. Wrap the content in AppTheme { }.")
}

internal val LocalThemeSize = staticCompositionLocalOf<ThemeSize> {
    error("No ThemeSize provided. Wrap the content in AppTheme { }.")
}
