package com.serhiimysyshyn.devlightiptvclient.data.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalAppNavController = staticCompositionLocalOf<NavHostController> {
    error("NavController not provided")
}