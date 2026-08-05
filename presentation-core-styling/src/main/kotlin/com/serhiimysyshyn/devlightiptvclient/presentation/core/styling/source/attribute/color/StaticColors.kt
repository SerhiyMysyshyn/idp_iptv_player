package com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.attribute.color

import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeStaticColors
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.primitive.Colors

/** Shared by both schemes — a scrim over video is black regardless of the app theme. */
internal val staticColors = ThemeStaticColors(
    white = Colors.White,
    black = Colors.Black,
    transparent = Colors.Transparent,
    scrim = Colors.Scrim,
)
