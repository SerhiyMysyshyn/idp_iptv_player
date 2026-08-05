package com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.attribute.color

import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeColors
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticBackgroundColor
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticBorderColor
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticColors
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticForegroundColor
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticTextColor
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.primitive.Colors

internal val attributeDarkColors = ThemeColors(
    semantic = ThemeSemanticColors(
        background = ThemeSemanticBackgroundColor(
            primaryMain = Colors.Blue800,
            primaryContent = Colors.Blue900,
            secondaryMain = Colors.Blue700,
            accent = Colors.Rust500,
            accentPressed = Colors.Rust700,
        ),
        foreground = ThemeSemanticForegroundColor(
            primary = Colors.White,
            secondary = Colors.Grey400,
            accent = Colors.Rust500,
            onAccent = Colors.White,
        ),
        text = ThemeSemanticTextColor(
            primary = Colors.White,
            secondary = Colors.Grey400,
            placeholder = Colors.Grey600,
            onAccent = Colors.White,
            error = Colors.Red300,
        ),
        border = ThemeSemanticBorderColor(
            primary = Colors.Grey600,
            secondary = Colors.Blue700,
            error = Colors.Red300,
        ),
    ),
    static = staticColors,
)
