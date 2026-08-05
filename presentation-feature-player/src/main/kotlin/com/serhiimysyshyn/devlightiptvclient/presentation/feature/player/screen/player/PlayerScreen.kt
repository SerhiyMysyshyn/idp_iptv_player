package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.util.Rational
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.component.PictureInPictureEffect
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract.PlayerScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer.EqualizerPreset
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer.PlayerEqualizer
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.model.selectTrack
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.model.toPlayerTracks
import org.koin.androidx.compose.koinViewModel

/**
 * Owns the ViewModel and the ExoPlayer instance.
 *
 * The player is created here rather than inside the content composable so that [PlayerContent]
 * stays a pure function of state — it only receives the already-built player to attach to a view.
 */
// PlayerEqualizer wraps media3's still-unstable audio-session API; the opt-in stops at this
// screen rather than leaking to callers.
@OptIn(UnstableApi::class)
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

    // Reported by the player once the first frame is decoded; PiP uses it to size its window.
    var videoAspectRatio by remember { mutableStateOf<Rational?>(null) }

    DisposableEffect(exoPlayer) {
        onDispose {
            equalizer.release()
            exoPlayer.release()
        }
    }

    /**
     * ExoPlayer only reports a real audio session id once playback is actually rolling, so bind
     * the effects from a playback-state listener rather than at composition time — the previous
     * version called `init()` immediately, got `AUDIO_SESSION_ID_UNSET`, and left the equalizer
     * permanently null.
     */
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState != Player.STATE_READY) return

                viewModel.processIntent(PlayerScreenIntent.PlaybackErrorOccurred(isError = false))

                if (equalizer.init()) {
                    viewModel.processIntent(
                        PlayerScreenIntent.AudioEffectsReady(equalizer.readBands()),
                    )
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                viewModel.processIntent(PlayerScreenIntent.PlaybackErrorOccurred(isError = true))
            }

            override fun onTracksChanged(tracks: Tracks) {
                viewModel.processIntent(
                    PlayerScreenIntent.TracksAvailable(tracks.toPlayerTracks()),
                )
            }

            override fun onVideoSizeChanged(videoSize: VideoSize) {
                videoAspectRatio = videoSize
                    .takeIf { it.width > 0 && it.height > 0 }
                    ?.let { Rational(it.width, it.height) }
            }
        }

        exoPlayer.addListener(listener)
        onDispose { exoPlayer.removeListener(listener) }
    }

    LaunchedEffect(state.currentPreset) {
        // CUSTOM means the curve came from the sliders — re-applying a preset would overwrite it.
        if (state.currentPreset != EqualizerPreset.CUSTOM) {
            equalizer.applyPreset(state.currentPreset)
            viewModel.processIntent(PlayerScreenIntent.AudioEffectsReady(equalizer.readBands()))
        }
    }

    LaunchedEffect(state.equalizerBands) {
        state.equalizerBands.forEach { band ->
            equalizer.setBandLevel(band.index, band.levelMillibel)
        }
    }

    LaunchedEffect(state.bassBoostStrength) {
        equalizer.setBassBoostStrength(state.bassBoostStrength)
    }

    LaunchedEffect(state.virtualizerStrength) {
        equalizer.setVirtualizerStrength(state.virtualizerStrength)
    }

    LaunchedEffect(state.currentChannel?.url) {
        val url = state.currentChannel?.url
        if (!url.isNullOrEmpty()) {
            exoPlayer.setMediaItem(MediaItem.fromUri(url))
            exoPlayer.prepare()
        }
    }

    FullscreenEffect(isFullscreen = state.isFullscreen)

    // Only worth entering PiP when the user allows it and there is a stream to keep watching.
    PictureInPictureEffect(
        enabled = state.isPictureInPictureEnabled &&
            state.currentChannel != null &&
            !state.hasPlaybackError,
        videoAspectRatio = videoAspectRatio,
    )

    // In fullscreen the system back gesture collapses back to the normal layout instead of
    // leaving the screen entirely.
    BackHandler(enabled = state.isFullscreen) {
        viewModel.processIntent(PlayerScreenIntent.ToggleFullscreen)
    }

    PlayerContent(
        state = state,
        exoPlayer = exoPlayer,
        onIntent = { intent ->
            // These two need the player itself, which the ViewModel deliberately doesn't hold.
            when (intent) {
                is PlayerScreenIntent.RetryPlayback -> exoPlayer.prepare()
                is PlayerScreenIntent.SelectTrack -> exoPlayer.selectTrack(intent.track)
                else -> Unit
            }
            viewModel.processIntent(intent)
        },
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
