package com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.attribute

import androidx.compose.ui.unit.dp
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeRadius
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSize
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeSpacing

internal val attributeSpacing = ThemeSpacing(
    xxs = 2.dp,
    xs = 4.dp,
    s = 8.dp,
    m = 16.dp,
    l = 24.dp,
    xl = 32.dp,
    xxl = 48.dp,
)

/**
 * Softer than before: cards move from 8dp to 16dp and dialogs/sheets to 28dp.
 *
 * The ramp is now an even progression (6/10/16/28) instead of the previous 4/8/16/36, where the
 * jump to `xl` was large enough that a dialog and a card next to each other looked unrelated.
 */
internal val attributeRadius = ThemeRadius(
    s = 6.dp,
    m = 10.dp,
    l = 16.dp,
    xl = 28.dp,
    full = 1000.dp,
)

internal val attributeSize = ThemeSize(
    iconS = 16.dp,
    iconM = 24.dp,
    iconL = 36.dp,
    avatar = 44.dp,
    rowHeight = 42.dp,
    progressStroke = 4.dp,
)
