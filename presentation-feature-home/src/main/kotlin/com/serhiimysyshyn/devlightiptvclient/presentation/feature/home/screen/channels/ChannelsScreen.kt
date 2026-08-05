package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.channels

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.serhiimysyshyn.devlightiptvclient.domain.model.Channel
import com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.source.LaunchMode
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.channels.contract.ChannelsScreenIntent
import org.koin.androidx.compose.koinViewModel

/**
 * Stateful entry point: owns the ViewModel, turns route arguments into the initial load, and hands
 * a plain state object down to [ChannelsContent].
 *
 * The load is dispatched from a [LaunchedEffect] keyed on the arguments. Previously it was called
 * straight from the composable body, so every recomposition fired another `processIntent`, which
 * restarted the database subscription, which emitted new state, which recomposed — an endless
 * loop that also leaked a collector per pass.
 */
@Composable
internal fun ChannelsScreen(
    launchMode: LaunchMode,
    playlistId: Long,
    onChannelClicked: (Channel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: ChannelsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(launchMode, playlistId) {
        when (launchMode) {
            LaunchMode.LOAD_FROM_PLAYLIST ->
                viewModel.processIntent(ChannelsScreenIntent.LoadChannelsFromDatabase(playlistId))

            LaunchMode.LOAD_FROM_FAVOURITES ->
                viewModel.processIntent(ChannelsScreenIntent.LoadFavouritesChannelsFromDatabase)

            // Nothing to load — the content falls through to its empty state.
            LaunchMode.UNKNOWN -> Unit
        }
    }

    ChannelsContent(
        state = state,
        onIntent = viewModel::processIntent,
        onChannelClicked = onChannelClicked,
        modifier = modifier,
    )
}
