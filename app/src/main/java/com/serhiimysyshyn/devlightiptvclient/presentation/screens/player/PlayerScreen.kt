package com.serhiimysyshyn.devlightiptvclient.presentation.screens.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.IPTVClientTheme
import com.serhiimysyshyn.devlightiptvclient.presentation.utils.DevicePreviews

@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    channelId: Long,
    videoUrl: String
) {
    PlayerContent(
        modifier = modifier,
        channelId = channelId,
        videoUrl = videoUrl
    )
}

@Composable
@DevicePreviews
fun PlayerScreenPreview() {
    IPTVClientTheme {
        PlayerContent(
            channelId = 1,
            videoUrl = ""
        )
    }
}