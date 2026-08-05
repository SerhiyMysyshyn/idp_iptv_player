package com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: SettingsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsContent(
        state = state,
        onIntent = viewModel::processIntent,
        onNavigateBack = onNavigateBack,
        modifier = modifier,
    )
}
