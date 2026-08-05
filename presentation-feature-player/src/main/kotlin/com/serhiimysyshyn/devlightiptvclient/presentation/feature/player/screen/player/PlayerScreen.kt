package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract.PlayerScreenIntent
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

    FullscreenEffect(isFullscreen = state.isFullscreen)

    // In fullscreen the system back gesture collapses back to the normal layout instead of
    // leaving the screen entirely.
    BackHandler(enabled = state.isFullscreen) {
        viewModel.processIntent(PlayerScreenIntent.ToggleFullscreen)
    }

    PlayerContent(
        state = state,
        exoPlayer = exoPlayer,
        onIntent = viewModel::processIntent,
        onNavigateBack = onNavigateBack,
        modifier = modifier,
    )
}

/**
 * Drives the window-level side of fullscreen: landscape orientation and hidden system bars.
 *
 * Both are restored on dispose so leaving the player mid-fullscreen doesn't leak a locked
 * orientation into the rest of the app.
 */
@Composable
private fun FullscreenEffect(isFullscreen: Boolean) {
    val activity = LocalContext.current.findActivity() ?: return

    DisposableEffect(isFullscreen) {
        val controller = WindowCompat.getInsetsController(activity.window, activity.window.decorView)

        if (isFullscreen) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller.hide(WindowInsetsCompat.Type.systemBars())
        } else {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            controller.show(WindowInsetsCompat.Type.systemBars())
        }

        onDispose {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            controller.show(WindowInsetsCompat.Type.systemBars())
        }
    }
}

/** Compose hands out a `ContextWrapper`, so unwrap until the hosting [Activity] surfaces. */
private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
