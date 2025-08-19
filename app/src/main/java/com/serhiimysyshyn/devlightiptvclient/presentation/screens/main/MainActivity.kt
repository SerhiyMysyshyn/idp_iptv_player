package com.serhiimysyshyn.devlightiptvclient.presentation.screens.main

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.serhiimysyshyn.devlightiptvclient.data.navigation.LocalAppNavController
import com.serhiimysyshyn.devlightiptvclient.data.navigation.NavigationRoute
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.PlayerScreen
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings.SettingsScreen
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.IPTVClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IPTVClientTheme {
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
                            SettingsScreen()
                        }

                        composable(
                            route = NavigationRoute.Player.DESTINATION,
                            arguments = listOf(
                                navArgument("channelId") { type = NavType.LongType },
                                navArgument("videoUrl") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getLong("channelId") ?: -1
                            val url = backStackEntry.arguments?.getString("videoUrl")?.let {
                                Uri.decode(it)
                            } ?: ""
                            PlayerScreen(
                                channelId = id,
                                videoUrl = url
                            )
                        }

                        // Інші глобальні екрани
                    }
                }
            }
        }
    }
}