package com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/** Gaps and paddings. Screens use these instead of literal `16.dp`. */
@Immutable
data class ThemeSpacing(
    val xxs: Dp,
    val xs: Dp,
    val s: Dp,
    val m: Dp,
    val l: Dp,
    val xl: Dp,
    val xxl: Dp,
)

/** Corner radii. */
@Immutable
data class ThemeRadius(
    val s: Dp,
    val m: Dp,
    val l: Dp,
    val xl: Dp,
    val full: Dp,
)

/** Fixed component dimensions — icon boxes, row heights, stroke widths. */
@Immutable
data class ThemeSize(
    val iconS: Dp,
    val iconM: Dp,
    val iconL: Dp,
    /** Leading square in a list row — channel logo or its generated stand-in. */
    val avatar: Dp,
    val rowHeight: Dp,
    val progressStroke: Dp,
)
