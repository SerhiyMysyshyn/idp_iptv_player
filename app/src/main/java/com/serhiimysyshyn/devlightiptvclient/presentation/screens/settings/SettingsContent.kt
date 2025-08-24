package com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.serhiimysyshyn.devlightiptvclient.R
import com.serhiimysyshyn.devlightiptvclient.presentation.composables.organism.MainAppBar
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings.composables.AppColorsDialog
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings.composables.SelectThemeDialog
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings.composables.SettingsRowItem
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings.contract.SettingsScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.IPTVClientTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    val viewModel: SettingsScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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
                title = context.getString(R.string.settings_str),
                openMenuIcon = Icons.AutoMirrored.Filled.ArrowBack
            ) {
                onNavigateBack.invoke()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                SettingsRowItem(
                    title = "Кольорова гама додатку",
                    onItemClicked = {
                        viewModel.processIntent(SettingsScreenIntent.ShowAppColorsDialog)
                    }
                )

                SettingsRowItem(
                    title = "Поточна тема додатку",
                    value = state.currentAppTheme.displayName,
                    onItemClicked = {
                        viewModel.processIntent(SettingsScreenIntent.ShowChangeThemeDialog)
                    }
                )
            }
        }

        if (state.showAppColorsDialog) {
            AppColorsDialog(
                onDismiss = {
                    viewModel.processIntent(SettingsScreenIntent.HideAppColorsDialog)
                }
            )
        }

        if (state.showChangeThemeDialog) {
            SelectThemeDialog(
                currentTheme = state.currentAppTheme,
                onThemeSelected = { selectedTheme ->
                    viewModel.processIntent(SettingsScreenIntent.UpdateAppTheme(selectedTheme))
                    viewModel.processIntent(SettingsScreenIntent.HideChangeThemeDialog)
                },
                onDismiss = {
                    viewModel.processIntent(SettingsScreenIntent.HideChangeThemeDialog)
                }
            )
        }
    }
}