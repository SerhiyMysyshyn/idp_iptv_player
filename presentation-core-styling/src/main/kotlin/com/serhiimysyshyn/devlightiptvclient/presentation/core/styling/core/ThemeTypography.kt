package com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

/**
 * Text styles.
 *
 * None of these carry a colour: colour is a semantic decision that belongs to the call site
 * (`color = Theme.colors.semantic.text.secondary`). Baking one in — as the old `caption` did with
 * `Color.Gray` — makes the style unusable on a dark background.
 */
@Immutable
data class ThemeTypography(
    val h1: TextStyle,
    val h2: TextStyle,
    val title: TextStyle,
    val body: TextStyle,
    val bodySmall: TextStyle,
    val caption: TextStyle,
)
