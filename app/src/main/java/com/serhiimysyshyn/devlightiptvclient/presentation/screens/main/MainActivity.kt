package com.serhiimysyshyn.devlightiptvclient.presentation.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.serhiimysyshyn.devlightiptvclient.data.navigation.LocalAppNavController
import com.serhiimysyshyn.devlightiptvclient.data.navigation.NavigationRoute
import com.serhiimysyshyn.devlightiptvclient.data.repository.ThemeRepository
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.PlayerScreen
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings.SettingsScreen
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.AppThemeType
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.IPTVClientTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val themeRepository: ThemeRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentTheme by themeRepository.getTheme().collectAsState(initial = AppThemeType.SYSTEM)

            IPTVClientTheme(
                appTheme = currentTheme
            ) {
                val rootNavController = rememberNavController()

                CompositionLocalProvider(LocalAppNavController provides rootNavController) {
                    NavHost(
                        navController = rootNavController,
                        startDestination = NavigationRoute.Main.DESTINATION
                    ) {
                        composable(NavigationRoute.Main.DESTINATION) {
                            MainScreen()
                        }

                        composable(NavigationRoute.Settings.DESTINATION) {
                            SettingsScreen(
                                onNavigateBack = {
                                    rootNavController.popBackStack()
                                }
                            )
                        }

                        composable(
                            route = NavigationRoute.Player.DESTINATION,
                            arguments = listOf(
                                navArgument("channelId") { type = NavType.LongType },
                            )
                        ) { backStackEntry ->
                            PlayerScreen(
                                onNavigateBack = {
                                    rootNavController.popBackStack()
                                }
                            )
                        }

                        // Інші глобальні екрани
                    }
                }
            }
        }
    }
}