package com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.attribute.color

import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeColors
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticBackgroundColor
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticBorderColor
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticColors
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticForegroundColor
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticTextColor
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.primitive.Colors

/**
 * Light scheme: a faintly tinted screen with **white** cards raised on top of it.
 *
 * Cards are the lightest surface, not the most saturated one — that inversion is what lets a
 * single saturated accent read as the only call to action on the screen.
 */
internal val attributeLightColors = ThemeColors(
    semantic = ThemeSemanticColors(
        background = ThemeSemanticBackgroundColor(
            primaryMain = Colors.Slate50,
            primaryContent = Colors.White,
            secondaryMain = Colors.Slate100,
            accent = Colors.Indigo600,
            accentPressed = Colors.Indigo700,
        ),
        foreground = ThemeSemanticForegroundColor(
            primary = Colors.Slate900,
            secondary = Colors.Slate600,
            accent = Colors.Indigo600,
            onAccent = Colors.White,
        ),
        text = ThemeSemanticTextColor(
            primary = Colors.Slate900,
            secondary = Colors.Slate600,
            // Same value as `secondary`: placeholders are real text, so they owe the full 4.5:1
            // even on the `secondaryMain` fill the search field uses.
            placeholder = Colors.Slate600,
            onAccent = Colors.White,
            error = Colors.Red500,
        ),
        border = ThemeSemanticBorderColor(
            primary = Colors.Slate200,
            secondary = Colors.Slate100,
            error = Colors.Red500,
        ),
    ),
    static = staticColors,
)
