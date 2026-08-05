package com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.core.provider

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

/**
 * The root NavController, provided once by the host activity.
 *
 * The nested NavHost inside the main screen keeps its own controller; this local is for escaping
 * to a full-screen destination such as the player or settings.
 */
val LocalAppNavController = staticCompositionLocalOf<NavHostController> {
    error("LocalAppNavController not provided. Provide it from the host activity.")
}
