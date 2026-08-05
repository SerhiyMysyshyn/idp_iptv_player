package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.source.LaunchMode
import com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.source.NavigationRoute
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.ext.safeLaunch
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.component.appbar.MainAppBar
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.channels.ChannelsScreen
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.component.CustomModalDrawerSheet
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.contract.MainScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.contract.MainScreenState
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.model.rememberMainMenuItems
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.playlists.PlaylistsScreen
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.R as CoreUiR

@Composable
internal fun MainContent(
    state: MainScreenState,
    mainNavController: NavHostController,
    onIntent: (MainScreenIntent) -> Unit,
    onMenuItemClicked: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val menuItems = rememberMainMenuItems()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CustomModalDrawerSheet(
                menuItems = menuItems,
                selectedMenuItemIndex = state.selectedMenuItemIndex,
                onMenuItemClicked = { menuItem ->
                    scope.safeLaunch { drawerState.close() }
                    onMenuItemClicked(menuItem.index)
                },
            )
        },
    ) {
        Scaffold(
            modifier = modifier,
            containerColor = Theme.colors.semantic.background.primaryMain,
            topBar = {
                MainAppBar(
                    title = stringResource(CoreUiR.string.app_name),
                    navigationIcon = Icons.Default.Menu,
                    navigationContentDescription = stringResource(CoreUiR.string.open_menu),
                    onNavigationClick = { scope.safeLaunch { drawerState.open() } },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    containerColor = Theme.colors.semantic.background.accent,
                    contentColor = Theme.colors.semantic.foreground.onAccent,
                    onClick = { onIntent(MainScreenIntent.OpenAddNewPlaylistDialog) },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(CoreUiR.string.add_playlist_title),
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End,
        ) { padding ->
            NavHost(
                navController = mainNavController,
                startDestination = NavigationRoute.Playlists.DESTINATION,
                modifier = Modifier.padding(padding),
            ) {
                composable(NavigationRoute.Playlists.DESTINATION) {
                    PlaylistsScreen(
                        onPlaylistClicked = { playlist ->
                            val route = NavigationRoute.Channels(
                                launchMode = LaunchMode.LOAD_FROM_PLAYLIST,
                                playlistId = playlist.id,
                            ).route
                            onIntent(MainScreenIntent.LaunchNewScreen(route))
                        },
                    )
                }

                composable(
                    route = NavigationRoute.Channels.DESTINATION,
                    arguments = listOf(
                        navArgument(NavigationRoute.Channels.ARG_LAUNCH_MODE) { type = NavType.IntType },
                        navArgument(NavigationRoute.Channels.ARG_PLAYLIST_ID) { type = NavType.LongType },
                    ),
                ) { backStackEntry ->
                    val arguments = backStackEntry.arguments
                    val launchMode = LaunchMode.fromInt(
                        arguments?.getInt(NavigationRoute.Channels.ARG_LAUNCH_MODE) ?: LaunchMode.UNKNOWN.type,
                    )
                    val playlistId = arguments?.getLong(NavigationRoute.Channels.ARG_PLAYLIST_ID)
                        ?: NavigationRoute.Channels.NO_PLAYLIST_ID

                    ChannelsScreen(
                        launchMode = launchMode,
                        playlistId = playlistId,
                        onChannelClicked = { channel ->
                            val route = NavigationRoute.Player(channelId = channel.id).route
                            onIntent(MainScreenIntent.LaunchNewRootScreen(route))
                        },
                    )
                }
            }
        }
    }
}
