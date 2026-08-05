package com.serhiimysyshyn.devlightiptvclient.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.source.NavigationRoute
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.MainScreen
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.PlayerScreen
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.SettingsScreen

/**
 * The root navigation graph.
 *
 * It lives in `:app` because it is the only place allowed to reference every feature module —
 * putting it inside a feature would force that feature to depend on its siblings.
 */
@Composable
internal fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Main.DESTINATION,
        modifier = modifier,
    ) {
        composable(NavigationRoute.Main.DESTINATION) {
            MainScreen()
        }

        composable(NavigationRoute.Settings.DESTINATION) {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(
            route = NavigationRoute.Player.DESTINATION,
            arguments = listOf(
                navArgument(NavigationRoute.Player.ARG_CHANNEL_ID) { type = NavType.LongType },
            ),
        ) {
            PlayerScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
