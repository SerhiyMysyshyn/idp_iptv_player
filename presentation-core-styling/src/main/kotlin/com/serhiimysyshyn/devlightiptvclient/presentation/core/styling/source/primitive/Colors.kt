package com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.primitive

import androidx.compose.ui.graphics.Color

/**
 * Raw palette — the only place in the project where a hex colour literal is allowed.
 *
 * Primitives are never referenced from UI code: screens use the semantic tokens on
 * [com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme], which map to these
 * per light/dark scheme. That indirection is what makes retheming a one-file change.
 *
 * The ramp is deliberately *neutral surfaces + one saturated accent*. The previous palette used a
 * saturated teal for every card and a near-complementary rust for buttons on top of them, which
 * made accents vibrate and left secondary text at 1.77:1 — well under the 4.5:1 WCAG AA floor.
 * Every foreground/background pair used by [attributeLightColors] and [attributeDarkColors] is
 * verified against that floor (3:1 for non-text marks).
 */
internal object Colors {

    // Base
    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF000000)
    val Transparent = Color(0x00000000)
    val Scrim = Color(0xB3000000)

    /**
     * Neutral ramp, very slightly blue-tinted so it reads as a deliberate companion to the indigo
     * accent rather than as dead grey.
     */
    val Slate50 = Color(0xFFF7F8FB)
    val Slate100 = Color(0xFFEEF0F6)
    val Slate200 = Color(0xFFE0E3ED)
    val Slate300 = Color(0xFFC7CBD9)
    val Slate500 = Color(0xFF6E7488)
    val Slate600 = Color(0xFF565C6E)
    val Slate700 = Color(0xFF3A3F4F)
    val Slate800 = Color(0xFF222632)
    val Slate900 = Color(0xFF171A23)
    val Slate950 = Color(0xFF0F1219)

    /** Indigo — the brand accent. Indigo600 carries white text in both schemes at 6.29:1. */
    val Indigo400 = Color(0xFF818CF8)
    val Indigo600 = Color(0xFF4F46E5)
    val Indigo700 = Color(0xFF4338CA)

    /** Amber — sparingly, for "live"/highlight marks that must not read as the primary action. */
    val Amber400 = Color(0xFFFBBF24)
    val Amber600 = Color(0xFFD97706)

    // System
    val Red400 = Color(0xFFF87171)
    val Red500 = Color(0xFFDC2626)
}
