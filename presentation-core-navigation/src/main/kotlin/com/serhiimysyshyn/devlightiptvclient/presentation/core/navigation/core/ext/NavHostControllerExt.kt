package com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.core.ext

import android.util.Log
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

/**
 * Navigates without crashing when the destination is unknown or the graph is not attached yet.
 *
 * Double taps on a list row and navigation issued while the host is being recreated both surface
 * as [IllegalArgumentException]/[IllegalStateException] from Navigation; neither is worth killing
 * the app over.
 */
fun NavHostController.navigateSafe(
    route: String,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    try {
        navigate(route, navOptions, navigatorExtras)
    } catch (error: IllegalArgumentException) {
        Log.e(TAG, "Unknown destination: $route", error)
    } catch (error: IllegalStateException) {
        Log.e(TAG, "Navigation graph not ready for: $route", error)
    }
}

private const val TAG = "Navigation"
