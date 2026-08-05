package com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings

import com.serhiimysyshyn.devlightiptvclient.domain.model.AppThemeType
import com.serhiimysyshyn.devlightiptvclient.domain.repository.PlayerPreferencesRepository
import com.serhiimysyshyn.devlightiptvclient.domain.repository.ThemeRepository
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.base.viewmodel.BaseViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.ext.safeLaunch
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.contract.SettingsScreenEvent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.contract.SettingsScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.contract.SettingsScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.contract.SettingsScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    private val themeRepository: ThemeRepository,
    private val playerPreferencesRepository: PlayerPreferencesRepository,
    private val reducer: SettingsScreenReducer,
) : BaseViewModel<SettingsScreenIntent>() {

    private val _state = MutableStateFlow(SettingsScreenState())
    val state: StateFlow<SettingsScreenState> = _state.asStateFlow()

    init {
        observeAppTheme()
        observePictureInPicture()
    }

    override fun processIntent(intent: SettingsScreenIntent) {
        when (intent) {
            is SettingsScreenIntent.ShowAppColorsDialog -> emit(SettingsScreenEvent.ShowAppColorsDialog)
            is SettingsScreenIntent.HideAppColorsDialog -> emit(SettingsScreenEvent.HideAppColorsDialog)
            is SettingsScreenIntent.ShowChangeThemeDialog -> emit(SettingsScreenEvent.ShowAppThemeDialog)
            is SettingsScreenIntent.HideChangeThemeDialog -> emit(SettingsScreenEvent.HideAppThemeDialog)
            is SettingsScreenIntent.UpdateAppTheme -> updateAppTheme(intent.appThemeType)
            is SettingsScreenIntent.UpdatePictureInPictureEnabled ->
                updatePictureInPictureEnabled(intent.isEnabled)
        }
    }

    private fun observeAppTheme() {
        safeLaunch(onError = { emit(SettingsScreenEvent.Error) }) {
            themeRepository.getTheme().collect { theme ->
                emit(SettingsScreenEvent.AppThemeLoaded(theme))
            }
        }
    }

    /**
     * Only writes. The new value arrives back through [observeAppTheme], so the state is never
     * updated from two places and cannot disagree with what is actually persisted.
     */
    private fun updateAppTheme(appThemeType: AppThemeType) {
        safeLaunch(onError = { emit(SettingsScreenEvent.Error) }) {
            themeRepository.updateTheme(appThemeType)
        }
    }

    private fun observePictureInPicture() {
        safeLaunch(onError = { emit(SettingsScreenEvent.Error) }) {
            playerPreferencesRepository.isPictureInPictureEnabled().collect { isEnabled ->
                emit(SettingsScreenEvent.PictureInPictureEnabledLoaded(isEnabled))
            }
        }
    }

    /** Write-only, for the same reason as [updateAppTheme]. */
    private fun updatePictureInPictureEnabled(isEnabled: Boolean) {
        safeLaunch(onError = { emit(SettingsScreenEvent.Error) }) {
            playerPreferencesRepository.updatePictureInPictureEnabled(isEnabled)
        }
    }

    private fun emit(event: SettingsScreenEvent) {
        _state.value = reducer.reduce(_state.value, event)
    }
}
