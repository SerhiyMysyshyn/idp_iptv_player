package com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeColors
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.attribute.attributeRadius
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.attribute.attributeSize
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.attribute.attributeSpacing
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.attribute.attributeTypography
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.attribute.color.attributeDarkColors
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.attribute.color.attributeLightColors
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.provider.LocalThemeColors
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.provider.LocalThemeRadius
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.provider.LocalThemeSize
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.provider.LocalThemeSpacing
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.provider.LocalThemeTypography

/**
 * Provides the design system to everything below it.
 *
 * Two things differ from the previous `IPTVClientTheme`:
 *
 * 1. It does **not** wrap the content in a `Box` painted with the background colour. That wrapper
 *    forced every screen to render on top of an extra full-screen layer and made `Scaffold`'s own
 *    `containerColor` a no-op. Screens paint their own background via `Scaffold`.
 * 2. It installs a [MaterialTheme] derived from the tokens. Material 3 components (`Card`,
 *    `TextField`, `Scaffold`, `AlertDialog`) read `MaterialTheme` for their defaults; without it
 *    they fall back to the stock baseline purple, which is why so many call sites previously had
 *    to override every colour by hand.
 *
 * @param useDarkTheme whether to use the dark scheme; follows the system setting by default.
 */
@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (useDarkTheme) attributeDarkColors else attributeLightColors

    CompositionLocalProvider(
        LocalThemeColors provides colors,
        LocalThemeTypography provides attributeTypography,
        LocalThemeSpacing provides attributeSpacing,
        LocalThemeRadius provides attributeRadius,
        LocalThemeSize provides attributeSize,
    ) {
        MaterialTheme(
            colorScheme = colors.asMaterialColorScheme(useDarkTheme),
            content = content,
        )
    }
}

/** Bridges the semantic tokens onto the Material 3 slots so stock components inherit the theme. */
private fun ThemeColors.asMaterialColorScheme(useDarkTheme: Boolean): ColorScheme {
    val base = if (useDarkTheme) darkColorScheme() else lightColorScheme()

    return base.copy(
        primary = semantic.background.accent,
        onPrimary = semantic.text.onAccent,
        secondary = semantic.background.primaryContent,
        onSecondary = semantic.text.primary,
        background = semantic.background.primaryMain,
        onBackground = semantic.text.primary,
        surface = semantic.background.primaryContent,
        onSurface = semantic.text.primary,
        surfaceVariant = semantic.background.secondaryMain,
        onSurfaceVariant = semantic.text.secondary,
        outline = semantic.border.primary,
        outlineVariant = semantic.border.secondary,
        error = semantic.text.error,
        onError = static.white,
        scrim = static.scrim,
    )
}
