package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer.PlayerEqualizer
import org.koin.androidx.compose.koinViewModel

/**
 * Owns the ViewModel and the ExoPlayer instance.
 *
 * The player is created here rather than inside the content composable so that [PlayerContent]
 * stays a pure function of state — it only receives the already-built player to attach to a view.
 */
@Composable
fun PlayerScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: PlayerViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply { playWhenReady = true }
    }
    val equalizer = remember(exoPlayer) {
        PlayerEqualizer(audioSessionIdProvider = { exoPlayer.audioSessionId })
    }

    DisposableEffect(exoPlayer) {
        equalizer.init()
        onDispose {
            equalizer.release()
            exoPlayer.release()
        }
    }

    LaunchedEffect(state.currentPreset) {
        equalizer.applyPreset(state.currentPreset)
    }

    LaunchedEffect(state.currentChannel?.url) {
        val url = state.currentChannel?.url
        if (!url.isNullOrEmpty()) {
            exoPlayer.setMediaItem(MediaItem.fromUri(url))
            exoPlayer.prepare()
        }
    }

    PlayerContent(
        state = state,
        exoPlayer = exoPlayer,
        onIntent = viewModel::processIntent,
        onNavigateBack = onNavigateBack,
        modifier = modifier,
    )
}
