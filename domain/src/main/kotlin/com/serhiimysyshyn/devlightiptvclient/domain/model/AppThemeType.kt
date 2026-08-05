package com.serhiimysyshyn.devlightiptvclient.domain.model

/**
 * The theme the user picked in settings.
 *
 * Carries no display name on purpose — user-facing labels are string resources resolved in the
 * presentation layer, so the domain stays free of localised text.
 */
enum class AppThemeType {
    LIGHT,
    DARK,
    SYSTEM,
}
