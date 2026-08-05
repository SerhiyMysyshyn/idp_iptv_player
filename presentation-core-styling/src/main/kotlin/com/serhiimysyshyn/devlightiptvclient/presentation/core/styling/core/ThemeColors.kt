package com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Root colour structure, reached from UI code as `Theme.colors.semantic.*` / `Theme.colors.static.*`.
 *
 * Tokens are named after the **role** they play, never after a Material slot. `background.accent`
 * says where the colour goes; `surface` did not, which is how a FAB, a progress spinner and an
 * error text all ended up sharing one token in the previous version.
 */
@Immutable
data class ThemeColors(
    /** Tokens that flip between the light and dark schemes. */
    val semantic: ThemeSemanticColors,
    /** Tokens that are identical in both schemes. */
    val static: ThemeStaticColors,
)

@Immutable
data class ThemeSemanticColors(
    val background: ThemeSemanticBackgroundColor,
    val foreground: ThemeSemanticForegroundColor,
    val text: ThemeSemanticTextColor,
    val border: ThemeSemanticBorderColor,
)

/** Fills behind content. */
@Immutable
data class ThemeSemanticBackgroundColor(
    /** The screen itself — `Scaffold.containerColor`. */
    val primaryMain: Color,
    /** Raised content sitting on [primaryMain]: cards, app bar, navigation drawer. */
    val primaryContent: Color,
    /** Secondary fills nested inside [primaryContent]. */
    val secondaryMain: Color,
    /** Call-to-action fill: FAB, selected drawer row. */
    val accent: Color,
    /** Pressed/ripple state of [accent]. */
    val accentPressed: Color,
)

/** Icons and other non-text foreground marks. */
@Immutable
data class ThemeSemanticForegroundColor(
    val primary: Color,
    val secondary: Color,
    /** Accent-coloured marks on a neutral background — e.g. the loading spinner. */
    val accent: Color,
    /** Marks drawn on top of [ThemeSemanticBackgroundColor.accent]. */
    val onAccent: Color,
)

@Immutable
data class ThemeSemanticTextColor(
    val primary: Color,
    val secondary: Color,
    val placeholder: Color,
    /** Text on top of [ThemeSemanticBackgroundColor.accent]. */
    val onAccent: Color,
    val error: Color,
)

@Immutable
data class ThemeSemanticBorderColor(
    val primary: Color,
    val secondary: Color,
    val error: Color,
)

/** Colours that do not depend on the active scheme. */
@Immutable
data class ThemeStaticColors(
    val white: Color,
    val black: Color,
    val transparent: Color,
    /** Dim layer behind player controls and modal surfaces. */
    val scrim: Color,
)
