package com.serhiimysyshyn.devlightiptvclient.presentation.screens.main

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.serhiimysyshyn.devlightiptvclient.R
import com.serhiimysyshyn.devlightiptvclient.data.models.Channel
import com.serhiimysyshyn.devlightiptvclient.data.models.Playlist
import com.serhiimysyshyn.devlightiptvclient.data.navigation.NavigationRoute
import com.serhiimysyshyn.devlightiptvclient.presentation.composables.organism.MainAppBar
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.ChannelsScreen
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.utils.LaunchMode
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.composables.CustomModalDrawerSheet
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.contract.MainScreenState
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.PlaylistsScreen
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.IPTVClientTheme
import com.serhiimysyshyn.devlightiptvclient.presentation.utils.generateMenuItems
import com.serhiimysyshyn.devlightiptvclient.presentation.utils.safeLaunch
import kotlinx.coroutines.launch

@Composable
internal fun MainContent(
    mainNavController: NavHostController,
    state: MainScreenState,
    onMenuItemClicked: (Long) -> Unit,
    onAddNewPlaylistButtonClicked: () -> Unit,
    onPlaylistScreenLoad: (Playlist) -> Unit,
    onChannelsScreenLoad: (Channel) -> Unit,
) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CustomModalDrawerSheet(
                menuItems = generateMenuItems(context),
                selectedMenuItemIndex = state.selectedMenuItemIndex,
                onMenuItemClicked = { menuItem ->
                    scope.safeLaunch {
                        drawerState.close()
                    }
                    onMenuItemClicked.invoke(menuItem.index)
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
                        scope.safeLaunch { drawerState.open() }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    containerColor = IPTVClientTheme.colors.surface,
                    onClick = {
                        onAddNewPlaylistButtonClicked.invoke()
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
                    PlaylistsScreen { playlist -> onPlaylistScreenLoad.invoke(playlist) }
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
                        onChannelsScreenLoad.invoke(it)
                    }
                }
            }
        }
    }
}