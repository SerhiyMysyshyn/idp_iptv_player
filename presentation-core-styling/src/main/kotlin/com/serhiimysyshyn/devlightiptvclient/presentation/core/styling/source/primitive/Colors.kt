package com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.primitive

import androidx.compose.ui.graphics.Color

/**
 * Raw palette — the only place in the project where a hex colour literal is allowed.
 *
 * Primitives are never referenced from UI code: screens use the semantic tokens on
 * [com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme], which map to these
 * per light/dark scheme. That indirection is what makes retheming a one-file change.
 */
internal object Colors {

    // Base
    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF000000)
    val Transparent = Color(0x00000000)
    val Scrim = Color(0x99000000)

    // Neutral
    val Grey100 = Color(0xFFEBEDF1)
    val Grey200 = Color(0xFFD7D8DC)
    val Grey400 = Color(0xFFB0B4BB)
    val Grey600 = Color(0xFF6D737C)

    // Teal — content surfaces in the light scheme
    val Teal500 = Color(0xFF00B0A7)
    val Teal700 = Color(0xFF00908A)

    // Deep blue — backgrounds in the dark scheme
    val Blue700 = Color(0xFF06556C)
    val Blue800 = Color(0xFF054B5D)
    val Blue900 = Color(0xFF232C47)

    // Rust — the accent colour, identical in both schemes
    val Rust500 = Color(0xFFBA3518)
    val Rust700 = Color(0xFF9F2E15)

    // System
    val Red500 = Color(0xFFE53935)
    val Red300 = Color(0xFFEF9A9A)
}
