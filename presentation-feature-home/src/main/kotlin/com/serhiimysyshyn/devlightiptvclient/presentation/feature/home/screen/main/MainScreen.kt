package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.core.ext.navigateSafe
import com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.core.provider.LocalAppNavController
import com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.source.LaunchMode
import com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.source.LocalShortcutDestinationBus
import com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.source.NavigationRoute
import com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.source.ShortcutDestination
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.contract.MainScreenEffect
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.contract.MainScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.model.MainMenuItemId
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.playlists.component.AddNewPlaylistDialog
import org.koin.androidx.compose.koinViewModel

/**
 * Host for the playlists/channels tabs and the navigation drawer.
 *
 * Owns two controllers: its own nested one for the list destinations, and the root one from
 * [LocalAppNavController] for escaping to full-screen destinations.
 */
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val viewModel: MainViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val rootNavController = LocalAppNavController.current
    val mainNavController = rememberNavController()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MainScreenEffect.LaunchNewScreen -> mainNavController.navigateSafe(effect.route)
                is MainScreenEffect.LaunchNewRootScreen -> rootNavController.navigateSafe(effect.route)
            }
        }
    }

    // A launcher shortcut lands on this screen, then jumps to the tab it asked for. Routed through
    // the same handler as a drawer tap so the drawer selection stays in sync.
    val shortcutDestinationBus = LocalShortcutDestinationBus.current

    LaunchedEffect(shortcutDestinationBus, viewModel) {
        shortcutDestinationBus?.destinations?.collect { destination ->
            when (destination) {
                ShortcutDestination.FAVOURITES ->
                    viewModel.onMenuItemClicked(MainMenuItemId.FAVOURITES)
            }
        }
    }

    MainContent(
        state = state,
        mainNavController = mainNavController,
        onIntent = viewModel::processIntent,
        onMenuItemClicked = { index -> viewModel.onMenuItemClicked(index) },
        modifier = modifier,
    )

    if (state.showAddNewPlayListDialog) {
        AddNewPlaylistDialog(
            onConfirmClicked = { url ->
                viewModel.processIntent(MainScreenIntent.StartDownloadingNewPlaylist(url))
            },
            onDismiss = {
                viewModel.processIntent(MainScreenIntent.HideAddNewPlaylistDialog)
            },
        )
    }
}

/**
 * Translates a drawer selection into the intents it implies.
 *
 * Kept next to the screen rather than inline in the drawer callback so the `when` over menu ids
 * reads as one decision instead of three nested lambdas.
 */
private fun MainViewModel.onMenuItemClicked(index: Long) {
    when (index) {
        MainMenuItemId.PLAYLISTS -> {
            processIntent(MainScreenIntent.LaunchNewScreen(NavigationRoute.Playlists.DESTINATION))
            processIntent(MainScreenIntent.UpdateSelectedMenuItemIndex(index))
        }

        MainMenuItemId.FAVOURITES -> {
            val route = NavigationRoute.Channels(
                launchMode = LaunchMode.LOAD_FROM_FAVOURITES,
                playlistId = NavigationRoute.Channels.NO_PLAYLIST_ID,
            ).route
            processIntent(MainScreenIntent.LaunchNewScreen(route))
            processIntent(MainScreenIntent.UpdateSelectedMenuItemIndex(index))
        }

        // Settings is a root destination, so the drawer selection is intentionally not updated —
        // the user comes back to whichever tab they left.
        MainMenuItemId.SETTINGS ->
            processIntent(MainScreenIntent.LaunchNewRootScreen(NavigationRoute.Settings.DESTINATION))
    }
}
