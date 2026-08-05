package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.res.ResourcesCompat
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.ui.viewinterop.AndroidView
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.component.appbar.MainAppBar
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.component.list.CustomListItemV1
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.component.AudioSettingsBottomSheet
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.component.PlayerGestureHud
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.component.isInPictureInPictureMode
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.component.PlayerGestureState
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.component.rememberPlayerGestureModifier
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.component.TrackSelectorBottomSheet
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract.PlayerScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract.PlayerScreenState
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.R as CoreUiR

private const val VIDEO_ASPECT_RATIO = 16f / 9f
private const val ERROR_OVERLAY_ALPHA = 0.7f

@Composable
internal fun PlayerContent(
    state: PlayerScreenState,
    exoPlayer: ExoPlayer,
    onIntent: (PlayerScreenIntent) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val unknownChannel = stringResource(CoreUiR.string.player_unknown_channel)
    val channelName = state.currentChannel?.name ?: unknownChannel

    // A PiP window is far too small for the app bar and the favourites list, so it reuses the
    // fullscreen layout: video only, on black.
    val isInPipMode = isInPictureInPictureMode()

    // In fullscreen the video is the only thing on screen — no Scaffold, no app bar, black
    // background so the letterbox bars around a non-16:9 stream aren't visible.
    if (state.isFullscreen || isInPipMode) {
        // Gestures are fullscreen-only: in the normal layout a vertical drag belongs to the
        // scrolling column underneath.
        var gestureState by remember { mutableStateOf<PlayerGestureState?>(null) }
        val gestureModifier = rememberPlayerGestureModifier(
            // A PiP window is a thumbnail — too small to aim a brightness drag at.
            enabled = !isInPipMode,
            onGestureChange = { gestureState = it },
        )

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
                .then(gestureModifier),
        ) {
            VideoSurface(
                exoPlayer = exoPlayer,
                hasPlaybackError = state.hasPlaybackError,
                showController = !isInPipMode,
                onIntent = onIntent,
                modifier = Modifier.fillMaxSize(),
            )

            gestureState?.let { gesture ->
                PlayerGestureHud(
                    state = gesture,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    } else {
        Scaffold(
            modifier = modifier,
            containerColor = Theme.colors.semantic.background.primaryMain,
            topBar = {
                MainAppBar(
                    title = channelName,
                    navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                    navigationContentDescription = stringResource(CoreUiR.string.navigate_back),
                    onNavigationClick = onNavigateBack,
                )
            },
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                VideoSurface(
                    exoPlayer = exoPlayer,
                    hasPlaybackError = state.hasPlaybackError,
                    showController = true,
                    onIntent = onIntent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(VIDEO_ASPECT_RATIO),
                )

                Spacer(Modifier.height(Theme.spacing.m))

                Text(
                    text = stringResource(CoreUiR.string.player_stream_title, channelName),
                    style = Theme.typography.h2,
                    color = Theme.colors.semantic.text.primary,
                    modifier = Modifier.padding(horizontal = Theme.spacing.m),
                )

                Spacer(Modifier.height(Theme.spacing.m))

                PlayerActions(state = state, onIntent = onIntent)

                Spacer(Modifier.height(Theme.spacing.m))

                if (state.likedChannels.isNotEmpty()) {
                    FavouriteChannels(
                        state = state,
                        onIntent = onIntent,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }

    if (state.showAudioSettings) {
        AudioSettingsBottomSheet(
            currentPreset = state.currentPreset,
            bands = state.equalizerBands,
            bassBoostStrength = state.bassBoostStrength,
            virtualizerStrength = state.virtualizerStrength,
            onPresetSelected = { preset -> onIntent(PlayerScreenIntent.ApplyPreset(preset)) },
            onBandLevelChange = { index, millibel ->
                onIntent(PlayerScreenIntent.ChangeBandLevel(index, millibel))
            },
            onBassBoostChange = { onIntent(PlayerScreenIntent.ChangeBassBoost(it)) },
            onVirtualizerChange = { onIntent(PlayerScreenIntent.ChangeVirtualizer(it)) },
            onReset = { onIntent(PlayerScreenIntent.ResetAudioEffects) },
            onDismiss = { onIntent(PlayerScreenIntent.HideAudioSettings) },
        )
    }

    if (state.showTrackSelector) {
        TrackSelectorBottomSheet(
            tracks = state.tracks,
            onTrackSelected = { track -> onIntent(PlayerScreenIntent.SelectTrack(track)) },
            onDismiss = { onIntent(PlayerScreenIntent.HideTrackSelector) },
        )
    }
}

/**
 * The video surface.
 *
 * The fullscreen toggle is media3's own controller button — it only renders once a
 * [PlayerView.setFullscreenButtonClickListener] is attached, and media3 swaps the enter/exit
 * icon itself based on the flag passed back to the listener.
 */
@Composable
private fun VideoSurface(
    exoPlayer: ExoPlayer,
    hasPlaybackError: Boolean,
    showController: Boolean,
    onIntent: (PlayerScreenIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    setFullscreenButtonClickListener {
                        onIntent(PlayerScreenIntent.ToggleFullscreen)
                    }
                }
            },
            // Attaching the player in `update` rather than `factory` keeps the view usable
            // after the composable is reused with a different ExoPlayer instance.
            update = { view ->
                view.player = exoPlayer
                view.useController = showController
                if (!showController) view.hideController()
            },
            onRelease = { view -> view.player = null },
            modifier = Modifier.fillMaxSize(),
        )

        if (hasPlaybackError) {
            PlaybackErrorOverlay(onRetry = { onIntent(PlayerScreenIntent.RetryPlayback) })
        }
    }
}

/** Covers the video with a retry affordance — IPTV streams drop often enough to need one. */
@Composable
private fun PlaybackErrorOverlay(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = ERROR_OVERLAY_ALPHA))
            .padding(Theme.spacing.m),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(CoreUiR.string.player_playback_error),
            style = Theme.typography.body,
            color = Color.White,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(Theme.spacing.s))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = Theme.colors.semantic.background.accent,
                contentColor = Theme.colors.semantic.foreground.onAccent,
            ),
        ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)

            Spacer(Modifier.width(Theme.spacing.s))

            Text(stringResource(CoreUiR.string.player_retry))
        }
    }
}

@Composable
private fun PlayerActions(
    state: PlayerScreenState,
    onIntent: (PlayerScreenIntent) -> Unit,
) {
    val isFavourite = state.currentChannel?.isFavorite == true

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Theme.colors.semantic.background.accent,
        contentColor = Theme.colors.semantic.foreground.onAccent,
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.spacing.m),
    ) {
        Button(
            onClick = {
                state.currentChannel?.id?.let { id ->
                    onIntent(PlayerScreenIntent.ToggleChannelFavourite(id))
                }
            },
            enabled = state.currentChannel != null,
            colors = buttonColors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Theme.spacing.xs),
        ) {
            Icon(
                imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
            )

            Spacer(Modifier.width(Theme.spacing.s))

            Text(
                stringResource(
                    if (isFavourite) {
                        CoreUiR.string.player_in_favourites
                    } else {
                        CoreUiR.string.player_add_to_favourites
                    },
                ),
            )
        }

        Button(
            onClick = { onIntent(PlayerScreenIntent.ShowAudioSettings) },
            colors = buttonColors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Theme.spacing.xs),
        ) {
            Icon(
                painter = painterResource(CoreUiR.drawable.outline_equalizer_24),
                contentDescription = null,
                modifier = Modifier.size(Theme.size.iconM),
            )

            Spacer(Modifier.width(Theme.spacing.s))

            Text(stringResource(CoreUiR.string.player_equalizer, state.currentPreset.name))
        }

        // Single-track streams have nothing to choose between, so the button stays hidden.
        if (state.tracks.isNotEmpty()) {
            Button(
                onClick = { onIntent(PlayerScreenIntent.ShowTrackSelector) },
                colors = buttonColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Theme.spacing.xs),
            ) {
                Icon(
                    imageVector = Icons.Default.Subtitles,
                    contentDescription = null,
                    modifier = Modifier.size(Theme.size.iconM),
                )

                Spacer(Modifier.width(Theme.spacing.s))

                Text(stringResource(CoreUiR.string.player_tracks))
            }
        }
    }
}

@Composable
private fun FavouriteChannels(
    state: PlayerScreenState,
    onIntent: (PlayerScreenIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.spacing.m),
        shape = RoundedCornerShape(Theme.radius.l),
        colors = CardDefaults.cardColors(
            containerColor = Theme.colors.semantic.background.secondaryMain,
            contentColor = Theme.colors.semantic.foreground.primary,
        ),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(Theme.spacing.m),
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.m),
        ) {
            items(
                items = state.likedChannels,
                key = { channel -> channel.id },
            ) { channel ->
                CustomListItemV1(
                    title = channel.name,
                    icon = ResourcesCompat.getDrawable(
                        context.resources,
                        CoreUiR.drawable.outline_media_link_24,
                        context.theme,
                    ),
                    onItemClicked = { onIntent(PlayerScreenIntent.LoadChannel(channel.id)) },
                    functionalIcon = Icons.Default.Favorite,
                    onFunctionalIconClicked = {
                        onIntent(PlayerScreenIntent.ToggleChannelFavourite(channel.id))
                    },
                )
            }
        }
    }
}
