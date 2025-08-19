package com.serhiimysyshyn.devlightiptvclient.presentation.screens.main

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.serhiimysyshyn.devlightiptvclient.data.navigation.LocalAppNavController
import com.serhiimysyshyn.devlightiptvclient.data.navigation.NavigationRoute
import com.serhiimysyshyn.devlightiptvclient.data.navigation.navigateSafe
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.utils.LaunchMode
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.intent.MainScreenEffect
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.intent.MainScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.composables.AddNewPlaylistDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen() {
    val viewModel: MainViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
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

    MainContent(
        state = state,
        onMenuItemClicked = { index ->
            when(index) {
                0L -> {
                    viewModel.processIntent(
                        MainScreenIntent.LaunchNewScreen(
                            NavigationRoute.Playlists.DESTINATION
                        )
                    )
                    viewModel.processIntent(
                        MainScreenIntent.UpdateSelectedMenuItemIndex(
                            index
                        )
                    )
                }
                1L -> {
                    viewModel.processIntent(MainScreenIntent.LaunchNewScreen(
                        route = NavigationRoute.Channels(
                            launchMode = LaunchMode.LOAD_FROM_FAVOURITES,
                            playlistId = -1
                        ).route
                    ))
                    viewModel.processIntent(
                        MainScreenIntent.UpdateSelectedMenuItemIndex(
                            index
                        )
                    )
                }
                2L -> {
                    viewModel.processIntent(
                        MainScreenIntent.LaunchNewRootScreen(
                            NavigationRoute.Settings.DESTINATION
                        )
                    )
                }
            }
        },
        onAddNewPlaylistButtonClicked = {
            viewModel.processIntent(MainScreenIntent.OpenAddNewPlaylistDialog)
        },
        mainNavController = mainNavController,
        onPlaylistScreenLoad = { playlist ->
            viewModel.processIntent(
                MainScreenIntent.LaunchNewScreen(
                    route = NavigationRoute.Channels(
                        launchMode = LaunchMode.LOAD_FROM_PLAYLIST,
                        playlistId = playlist.id
                    ).route
                )
            )
        },
        onChannelsScreenLoad = { channel ->
            viewModel.processIntent(
                MainScreenIntent.LaunchNewRootScreen(
                    route = NavigationRoute.Player(
                        channelId = channel.id,
                        videoUrl = Uri.encode(channel.url)
                    ).route
                )
            )
        }
    )

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