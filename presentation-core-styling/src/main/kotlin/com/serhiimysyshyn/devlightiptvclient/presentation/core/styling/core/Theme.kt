package com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.provider.LocalThemeColors
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.provider.LocalThemeRadius
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.provider.LocalThemeSize
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.provider.LocalThemeSpacing
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.provider.LocalThemeTypography

/**
 * The single entry point to the design system.
 *
 * ```kotlin
 * Scaffold(containerColor = Theme.colors.semantic.background.primaryMain) { ... }
 * Text(text = title, style = Theme.typography.body, color = Theme.colors.semantic.text.primary)
 * Spacer(Modifier.height(Theme.spacing.m))
 * ```
 *
 * @property colors semantic and static colour tokens
 * @property typography text styles, colourless by design
 * @property spacing gaps and paddings
 * @property radius corner radii
 * @property size fixed component dimensions
 */
object Theme {

    val colors: ThemeColors
        @Composable
        @ReadOnlyComposable
        get() = LocalThemeColors.current

    val typography: ThemeTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalThemeTypography.current

    val spacing: ThemeSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalThemeSpacing.current

    val radius: ThemeRadius
        @Composable
        @ReadOnlyComposable
        get() = LocalThemeRadius.current

    val size: ThemeSize
        @Composable
        @ReadOnlyComposable
        get() = LocalThemeSize.current
}
