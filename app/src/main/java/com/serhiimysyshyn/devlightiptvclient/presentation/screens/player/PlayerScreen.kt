package com.serhiimysyshyn.devlightiptvclient.presentation.screens.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.IPTVClientTheme
import com.serhiimysyshyn.devlightiptvclient.presentation.utils.DevicePreviews

@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    PlayerContent(
        modifier = modifier,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
@DevicePreviews
fun PlayerScreenPreview() {
    IPTVClientTheme {
        PlayerContent(
            onNavigateBack = {},
        )
    }
}