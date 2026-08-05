package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.contract

/**
 * One-shot side effects — consumed once by the screen, never stored in state.
 *
 * Navigation belongs here rather than in [MainScreenState]: a route left in state would replay on
 * every configuration change and navigate again.
 */
sealed interface MainScreenEffect {

    data class LaunchNewScreen(val route: String) : MainScreenEffect

    data class LaunchNewRootScreen(val route: String) : MainScreenEffect
}
