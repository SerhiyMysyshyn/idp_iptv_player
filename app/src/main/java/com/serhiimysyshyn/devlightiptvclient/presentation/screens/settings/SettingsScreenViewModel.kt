package com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings

import com.serhiimysyshyn.devlightiptvclient.data.repository.ThemeRepository
import com.serhiimysyshyn.devlightiptvclient.presentation.base.BaseViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings.contract.SettingsScreenEvent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings.contract.SettingsScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings.contract.SettingsScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings.contract.SettingsScreenState
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.AppThemeType
import com.serhiimysyshyn.devlightiptvclient.presentation.utils.safeLaunch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsScreenViewModel(
    private val settingsScreenReducer: SettingsScreenReducer,
    private val themeRepository: ThemeRepository
) : BaseViewModel<SettingsScreenIntent>() {

    private val _state = MutableStateFlow(SettingsScreenState())
    val state: StateFlow<SettingsScreenState> = _state.asStateFlow()

    init {
        getAppTheme()
    }

    override fun processIntent(intent: SettingsScreenIntent) {
        when (intent) {
            is SettingsScreenIntent.ShowAppColorsDialog -> showAppColorsDialog()
            is SettingsScreenIntent.HideAppColorsDialog -> hideAppColorsDialog()
            is SettingsScreenIntent.ShowChangeThemeDialog -> showAppThemeDialog()
            is SettingsScreenIntent.HideChangeThemeDialog -> hideAppThemeDialog()
            is SettingsScreenIntent.UpdateAppTheme -> updateAppTheme(intent.appThemeType)
        }
    }

    private fun showAppColorsDialog() {
        safeLaunch {
            _state.value = settingsScreenReducer.reduce(
                _state.value,
                SettingsScreenEvent.ShowAppColorsDialogEvent
            )
        }
    }

    private fun hideAppColorsDialog() {
        safeLaunch {
            _state.value = settingsScreenReducer.reduce(
                _state.value,
                SettingsScreenEvent.HideAppColorsDialogEvent
            )
        }
    }

    private fun showAppThemeDialog() {
        safeLaunch {
            _state.value = settingsScreenReducer.reduce(
                _state.value,
                SettingsScreenEvent.ShowAppThemeDialogEvent
            )
        }
    }

    private fun hideAppThemeDialog() {
        safeLaunch {
            _state.value = settingsScreenReducer.reduce(
                _state.value,
                SettingsScreenEvent.HideAppThemeDialogEvent
            )
        }
    }

    private fun getAppTheme() {
        safeLaunch {
            themeRepository.getTheme().collect { theme ->
                _state.value = settingsScreenReducer.reduce(
                    _state.value,
                    SettingsScreenEvent.LoadAppThemeSuccessEvent(
                        appTheme = theme
                    )
                )
            }
        }
    }

    private fun updateAppTheme(appThemeType: AppThemeType) {
        safeLaunch {
            themeRepository.updateTheme(appThemeType)

            _state.value = settingsScreenReducer.reduce(
                _state.value,
                SettingsScreenEvent.UpdateAppThemeEvent(
                    appTheme = appThemeType
                )
            )
        }
    }
}