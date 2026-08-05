package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.R as CoreUiR

/**
 * Builds the drawer rows.
 *
 * `remember`ed against the resolved labels so the list is not rebuilt on every recomposition —
 * a new `List<MenuItem>` each pass would defeat the drawer's own skipping.
 */
@Composable
internal fun rememberMainMenuItems(): List<MenuItem> {
    val playlists = stringResource(CoreUiR.string.play_lists_str)
    val favourites = stringResource(CoreUiR.string.licked_channels_str)
    val settings = stringResource(CoreUiR.string.settings_str)

    return remember(playlists, favourites, settings) {
        listOf(
            MenuItem(
                index = MainMenuItemId.PLAYLISTS,
                icon = Icons.Default.PlayArrow,
                title = playlists,
            ),
            MenuItem(
                index = MainMenuItemId.FAVOURITES,
                icon = Icons.Default.Favorite,
                title = favourites,
            ),
            MenuItem(
                index = MainMenuItemId.SETTINGS,
                icon = Icons.Default.Settings,
                title = settings,
            ),
        )
    }
}
