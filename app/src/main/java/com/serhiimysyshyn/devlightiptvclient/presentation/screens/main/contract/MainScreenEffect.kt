package com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.contract

sealed class MainScreenEffect {
    data class LaunchNewScreen(val route: String) : MainScreenEffect()
    data class LaunchNewRootScreen(val route: String) : MainScreenEffect()
}