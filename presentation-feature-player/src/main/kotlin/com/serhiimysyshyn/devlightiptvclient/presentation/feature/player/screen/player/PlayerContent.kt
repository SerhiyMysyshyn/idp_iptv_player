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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.res.ResourcesCompat
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.ui.viewinterop.AndroidView
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.component.appbar.MainAppBar
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.component.list.CustomListItemV1
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.component.PresetDialog
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract.PlayerScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract.PlayerScreenState
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.R as CoreUiR

private const val VIDEO_ASPECT_RATIO = 16f / 9f

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

    // In fullscreen the video is the only thing on screen — no Scaffold, no app bar, black
    // background so the letterbox bars around a non-16:9 stream aren't visible.
    if (state.isFullscreen) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black),
        ) {
            VideoSurface(
                exoPlayer = exoPlayer,
                onIntent = onIntent,
                modifier = Modifier.fillMaxSize(),
            )
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

    if (state.showPresetDialog) {
        PresetDialog(
            currentPreset = state.currentPreset,
            onPresetSelected = { preset -> onIntent(PlayerScreenIntent.ApplyPreset(preset)) },
            onDismiss = { onIntent(PlayerScreenIntent.HidePresetDialog) },
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
    onIntent: (PlayerScreenIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                useController = true
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                setFullscreenButtonClickListener { onIntent(PlayerScreenIntent.ToggleFullscreen) }
            }
        },
        // Attaching the player in `update` rather than `factory` keeps the view usable
        // after the composable is reused with a different ExoPlayer instance.
        update = { view -> view.player = exoPlayer },
        onRelease = { view -> view.player = null },
        modifier = modifier,
    )
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
            onClick = { onIntent(PlayerScreenIntent.ShowPresetDialog) },
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
        shape = RoundedCornerShape(Theme.radius.m),
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
