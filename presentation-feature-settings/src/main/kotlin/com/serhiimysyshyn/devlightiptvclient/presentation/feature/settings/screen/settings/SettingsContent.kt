package com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.preview.DevicePreviews
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.theme.AppTheme
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.component.appbar.MainAppBar
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.component.AppColorsDialog
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.component.SelectThemeDialog
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.component.SettingsRowItem
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.component.SettingsSwitchItem
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.component.label
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.contract.SettingsScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.contract.SettingsScreenState
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.R as CoreUiR

@Composable
internal fun SettingsContent(
    state: SettingsScreenState,
    onIntent: (SettingsScreenIntent) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier,
        containerColor = Theme.colors.semantic.background.primaryMain,
        topBar = {
            // The app bar belongs in the Scaffold's topBar slot; previously it was rendered
            // inside the content column, so it scrolled away and the top inset was applied twice.
            MainAppBar(
                title = stringResource(CoreUiR.string.settings_str),
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                navigationContentDescription = stringResource(CoreUiR.string.navigate_back),
                onNavigationClick = onNavigateBack,
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState),
        ) {
            SettingsRowItem(
                title = stringResource(CoreUiR.string.settings_app_colors),
                onItemClicked = { onIntent(SettingsScreenIntent.ShowAppColorsDialog) },
            )

            SettingsRowItem(
                title = stringResource(CoreUiR.string.settings_app_theme),
                value = state.currentAppTheme.label(),
                onItemClicked = { onIntent(SettingsScreenIntent.ShowChangeThemeDialog) },
            )

            SettingsSwitchItem(
                title = stringResource(CoreUiR.string.settings_picture_in_picture),
                description = stringResource(CoreUiR.string.settings_picture_in_picture_hint),
                isChecked = state.isPictureInPictureEnabled,
                onCheckedChange = { isEnabled ->
                    onIntent(SettingsScreenIntent.UpdatePictureInPictureEnabled(isEnabled))
                },
            )
        }
    }

    if (state.showAppColorsDialog) {
        AppColorsDialog(
            onDismiss = { onIntent(SettingsScreenIntent.HideAppColorsDialog) },
        )
    }

    if (state.showChangeThemeDialog) {
        SelectThemeDialog(
            currentTheme = state.currentAppTheme,
            onThemeSelected = { theme ->
                onIntent(SettingsScreenIntent.UpdateAppTheme(theme))
                onIntent(SettingsScreenIntent.HideChangeThemeDialog)
            },
            onDismiss = { onIntent(SettingsScreenIntent.HideChangeThemeDialog) },
        )
    }
}

@DevicePreviews
@Composable
private fun SettingsContentPreview() {
    AppTheme {
        SettingsContent(
            state = SettingsScreenState(isLoading = false),
            onIntent = {},
            onNavigateBack = {},
        )
    }
}
