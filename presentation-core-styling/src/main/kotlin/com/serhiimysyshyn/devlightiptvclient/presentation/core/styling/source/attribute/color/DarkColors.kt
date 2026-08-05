package com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.attribute.color

import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeColors
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticBackgroundColor
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticBorderColor
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticColors
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticForegroundColor
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSemanticTextColor
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.primitive.Colors

/**
 * Dark scheme: near-black screen with cards lifted one step, mirroring the light scheme's
 * elevation order.
 *
 * The surfaces stay dark enough for a video player — a bright chrome around a dark stream is what
 * makes late-night viewing unpleasant. `foreground.accent` steps up to Indigo400 because the
 * button fill's Indigo600 is too dark to read as a *mark* against these surfaces.
 */
internal val attributeDarkColors = ThemeColors(
    semantic = ThemeSemanticColors(
        background = ThemeSemanticBackgroundColor(
            primaryMain = Colors.Slate950,
            primaryContent = Colors.Slate900,
            secondaryMain = Colors.Slate800,
            accent = Colors.Indigo600,
            accentPressed = Colors.Indigo700,
        ),
        foreground = ThemeSemanticForegroundColor(
            primary = Colors.Slate50,
            secondary = Colors.Slate300,
            accent = Colors.Indigo400,
            onAccent = Colors.White,
        ),
        text = ThemeSemanticTextColor(
            primary = Colors.Slate50,
            secondary = Colors.Slate300,
            placeholder = Colors.Slate500,
            onAccent = Colors.White,
            error = Colors.Red400,
        ),
        border = ThemeSemanticBorderColor(
            primary = Colors.Slate700,
            secondary = Colors.Slate800,
            error = Colors.Red400,
        ),
    ),
    static = staticColors,
)
