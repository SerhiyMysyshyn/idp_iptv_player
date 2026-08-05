package com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.attribute.color

import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeColors
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticBackgroundColor
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticBorderColor
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticColors
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticForegroundColor
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticTextColor
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeStaticColors
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.primitive.Colors

internal val attributeLightColors = ThemeColors(
    semantic = ThemeSemanticColors(
        background = ThemeSemanticBackgroundColor(
            primaryMain = Colors.Grey100,
            primaryContent = Colors.Teal500,
            secondaryMain = Colors.Grey200,
            accent = Colors.Rust500,
            accentPressed = Colors.Rust700,
        ),
        foreground = ThemeSemanticForegroundColor(
            primary = Colors.Black,
            secondary = Colors.Grey600,
            accent = Colors.Rust500,
            onAccent = Colors.White,
        ),
        text = ThemeSemanticTextColor(
            primary = Colors.Black,
            secondary = Colors.Grey600,
            placeholder = Colors.Grey400,
            onAccent = Colors.White,
            error = Colors.Red500,
        ),
        border = ThemeSemanticBorderColor(
            primary = Colors.Grey400,
            secondary = Colors.Grey200,
            error = Colors.Red500,
        ),
    ),
    static = staticColors,
)
