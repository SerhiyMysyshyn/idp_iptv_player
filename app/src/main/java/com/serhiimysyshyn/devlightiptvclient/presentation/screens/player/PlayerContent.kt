package com.serhiimysyshyn.devlightiptvclient.presentation.screens.player

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.serhiimysyshyn.devlightiptvclient.R
import com.serhiimysyshyn.devlightiptvclient.presentation.composables.molecule.CustomListItemV1
import com.serhiimysyshyn.devlightiptvclient.presentation.composables.organism.MainAppBar
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.composables.PresetDialog
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.contract.PlayerScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.equalizer.PlayerEqualizer
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.IPTVClientTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun PlayerContent(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    val viewModel: PlayerViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = true
        }
    }

    val equalizer = remember { PlayerEqualizer(exoPlayer) }
    var showDialog by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
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
            val mediaItem = MediaItem.fromUri(url)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        }
    }

    Scaffold(
        containerColor = IPTVClientTheme.colors.background,
    ) { paddings ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    start = 0.dp,
                    end = 0.dp,
                    bottom = paddings.calculateBottomPadding()
                )
        ) {
            MainAppBar(
                title = state.currentChannel?.name ?: "Невідомо",
                openMenuIcon = Icons.AutoMirrored.Filled.ArrowBack
            ) {
                onNavigateBack.invoke()
            }

            AndroidView(
                factory = {
                    PlayerView(it).apply {
                        player = exoPlayer
                        useController = true
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Відеопотік: ${state.currentChannel?.name ?: "Невідомо"}",
                style = IPTVClientTheme.typography.h2,
                color = IPTVClientTheme.colors.primary,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Button(
                    onClick = {
                        state.currentChannel?.id?.let {
                            viewModel.processIntent(
                                PlayerScreenIntent.AddChannelToFavourite(
                                    it
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                ) {
                    Icon(
                        imageVector = if (state.currentChannel?.isFavorite ?: false) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (state.currentChannel?.isFavorite ?: false) {
                            "Додано в улюблені канали"
                        } else {
                            "Додати в улюблені канали"
                        }
                    )
                }

                Button(
                    onClick = {
                        showDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    GlideImage(
                        model = ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.outline_equalizer_24,
                            context.theme
                        ),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = IPTVClientTheme.colors.primary),
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Еквалайзер: ${state.currentPreset.name}")
                }
            }

            Spacer(Modifier.height(16.dp))

            if (state.likedChannels.isNotEmpty()) {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    shape = ShapeDefaults.Medium,
                    colors = CardColors(
                        containerColor = IPTVClientTheme.colors.backgroundSecondary,
                        contentColor = IPTVClientTheme.colors.primary,
                        disabledContainerColor = IPTVClientTheme.colors.onBackground,
                        disabledContentColor = IPTVClientTheme.colors.primary
                    )

                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.likedChannels) { channel ->
                            CustomListItemV1(
                                title = channel.name,
                                icon = ResourcesCompat.getDrawable(
                                    context.resources,
                                    R.drawable.outline_media_link_24,
                                    context.theme
                                ),
                                onItemClicked = {
                                    viewModel.processIntent(
                                        PlayerScreenIntent.LoadChannel(
                                            channelId = channel.id
                                        )
                                    )
                                },
                                functionalIcon = Icons.Default.Favorite
                            )
                        }
                    }
                }
            }

            if (showDialog) {
                PresetDialog(
                    currentPreset = state.currentPreset,
                    onPresetSelected = { preset ->
                        viewModel.processIntent(PlayerScreenIntent.ApplyPreset(preset))
                        showDialog = false
                    },
                    onDismiss = { showDialog = false }
                )
            }
        }
    }
}