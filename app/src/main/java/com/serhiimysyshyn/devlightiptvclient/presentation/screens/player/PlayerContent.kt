package com.serhiimysyshyn.devlightiptvclient.presentation.screens.player

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.IPTVClientTheme

@Composable
internal fun PlayerContent(
    modifier: Modifier = Modifier,
    channelId: Long,
    videoUrl: String
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(videoUrl)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    // UI
    Scaffold(
        containerColor = IPTVClientTheme.colors.background,
    ) { paddings ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    start = 0.dp,
                    top = paddings.calculateTopPadding(),
                    end = 0.dp,
                    bottom = paddings.calculateBottomPadding()
                )
        ) {
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

//            Column(
//                modifier = Modifier
//                    .padding(16.dp)
//            ) {
//                Text(
//                    text = "title",
//                    style = IPTVClientTheme.typography.h2
//                )
//
//                Spacer(Modifier.height(16.dp))
//
//                Text(
//                    text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
//                    style = IPTVClientTheme.typography.body
//                )
//
//                Spacer(Modifier.height(16.dp))
//
//                Row(
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Button(
//                        onClick = { /* TODO */ },
//                        modifier = Modifier
//                            .weight(1f)
//                            .widthIn(max = 200.dp)
//                            .padding(4.dp)
//                    ) {
//                        Icon(Icons.Default.Favorite, contentDescription = null)
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text("Кнопка 1")
//                    }
//
//                    Button(
//                        onClick = { /* TODO */ },
//                        modifier = Modifier
//                            .weight(1f)
//                            .widthIn(max = 200.dp)
//                            .padding(4.dp)
//                    ) {
//                        Icon(Icons.Default.Build, contentDescription = null)
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text("Кнопка 2")
//                    }
//                }
//            }
        }
    }


    // Звільнення ресурсів при виході з Composable
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}