package com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.serhiimysyshyn.devlightiptvclient.data.models.Channel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.utils.LaunchMode

@Composable
fun ChannelsScreen(
    modifier: Modifier = Modifier,
    launchMode: LaunchMode,
    playlistId: Long,
    onChannelClicked: (Channel) -> Unit
) {
    ChannelsContent(
        modifier = modifier,
        launchMode = launchMode,
        playlistId = playlistId,
        onChannelClicked = onChannelClicked
    )
}