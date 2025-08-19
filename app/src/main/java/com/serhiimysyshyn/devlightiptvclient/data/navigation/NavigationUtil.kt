package com.serhiimysyshyn.devlightiptvclient.data.navigation

import android.util.Log
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

fun NavHostController.navigateSafe(
    route: String,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    try {
        this.navigate(route, navOptions, navigatorExtras)
    } catch (e: Exception) {
       Log.e("Navigation", "Navigation exception: ${e.message}")
    }
}