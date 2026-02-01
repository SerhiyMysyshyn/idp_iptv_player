package com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    SettingsContent(
        modifier = modifier,
        onNavigateBack = onNavigateBack,
    )
}