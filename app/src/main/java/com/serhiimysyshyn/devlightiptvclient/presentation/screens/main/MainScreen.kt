package com.serhiimysyshyn.devlightiptvclient.presentation.screens.main

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.serhiimysyshyn.devlightiptvclient.R
import com.serhiimysyshyn.devlightiptvclient.data.navigation.LocalAppNavController
import com.serhiimysyshyn.devlightiptvclient.data.navigation.NavigationRoute
import com.serhiimysyshyn.devlightiptvclient.data.navigation.navigateSafe
import com.serhiimysyshyn.devlightiptvclient.presentation.composables.organism.MainAppBar
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.ChannelsScreen
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.utils.LaunchMode
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.composables.CustomModalDrawerSheet
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.intent.MainScreenEffect
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.intent.MainScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.composables.AddNewPlaylistDialog
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.PlaylistsScreen
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.IPTVClientTheme
import com.serhiimysyshyn.devlightiptvclient.presentation.utils.generateMenuItems
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen() {
    val viewModel: MainViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val rootNavController = LocalAppNavController.current
    val mainNavController = rememberNavController()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MainScreenEffect.LaunchNewScreen -> {
                    mainNavController.navigateSafe(effect.route)
                }
                is MainScreenEffect.LaunchNewRootScreen -> {
                    rootNavController.navigateSafe(effect.route)
                }
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CustomModalDrawerSheet(
                menuItems = generateMenuItems(context),
                selectedMenuItemIndex = state.selectedMenuItemIndex,
                onMenuItemClicked = { menuItem ->
                    scope.launch {
                        drawerState.close()
                    }
                    when(menuItem.index) {
                        0L -> {
                            viewModel.processIntent(MainScreenIntent.LaunchNewScreen(NavigationRoute.Playlists.DESTINATION))
                            viewModel.processIntent(MainScreenIntent.UpdateSelectedMenuItemIndex(menuItem.index))
                        }
                        1L -> {
                            viewModel.processIntent(MainScreenIntent.LaunchNewScreen(
                                route = NavigationRoute.Channels(
                                    launchMode = LaunchMode.LOAD_FROM_FAVOURITES,
                                    playlistId = -1
                                ).route
                            ))
                            viewModel.processIntent(MainScreenIntent.UpdateSelectedMenuItemIndex(menuItem.index))
                        }
                        2L -> {
                            viewModel.processIntent(MainScreenIntent.LaunchNewRootScreen(NavigationRoute.Settings.DESTINATION))
                        }
                    }
                }
            )
        }
    ) {
        Scaffold(
            containerColor = IPTVClientTheme.colors.background,
            topBar = {
                MainAppBar(
                    title = context.getString(R.string.app_name),
                    openMenuIcon = Icons.Default.Menu,
                    onClick = {
                        scope.launch { drawerState.open() }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    containerColor = IPTVClientTheme.colors.surface,
                    onClick = {
                        viewModel.processIntent(MainScreenIntent.OpenAddNewPlaylistDialog)
                    }
                ) { Icon(Icons.Default.Add,"") }
            },
            floatingActionButtonPosition = FabPosition.End
        ) { padding ->
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        color = IPTVClientTheme.colors.surface,
                        strokeWidth = 4.dp
                    )
                }
            }

            if (state.isError) {
                // Error
            }

            NavHost(
                mainNavController,
                startDestination = NavigationRoute.Playlists.DESTINATION,
                modifier = Modifier.padding(padding)
            ) {
                composable(NavigationRoute.Playlists.DESTINATION) {
                    PlaylistsScreen { playlist ->
                        viewModel.processIntent(
                            MainScreenIntent.LaunchNewScreen(
                                route = NavigationRoute.Channels(
                                    launchMode = LaunchMode.LOAD_FROM_PLAYLIST,
                                    playlistId = playlist.id
                                ).route
                            )
                        )
                    }
                }

                composable(
                    route = NavigationRoute.Channels.DESTINATION,
                    arguments = listOf(
                        navArgument("launchMode") { type = NavType.IntType },
                        navArgument("playlistId") { type = NavType.LongType }
                    )
                ) { backStackEntry ->
                    val launchMode = backStackEntry.arguments?.getInt("launchMode") ?: -1
                    val playlistId = backStackEntry.arguments?.getLong("playlistId") ?: -1

                    ChannelsScreen(
                        launchMode = LaunchMode.fromInt(launchMode),
                        playlistId = playlistId
                    ) {
                        viewModel.processIntent(
                            MainScreenIntent.LaunchNewRootScreen(
                                route = NavigationRoute.Player(
                                    channelId = it.id,
                                    videoUrl = Uri.encode(it.url)
                                ).route
                            )
                        )
                    }
                }
            }
        }

        if (state.showAddNewPlayListDialog) {
            AddNewPlaylistDialog(
                onConfirmClicked = {
                    viewModel.processIntent(MainScreenIntent.StartDownloadingNewPlaylist(it))
                    viewModel.processIntent(MainScreenIntent.HideAddNewPlaylistDialog)
                },
                onDismiss = {
                    viewModel.processIntent(MainScreenIntent.HideAddNewPlaylistDialog)
                }
            )
        }
    }
}