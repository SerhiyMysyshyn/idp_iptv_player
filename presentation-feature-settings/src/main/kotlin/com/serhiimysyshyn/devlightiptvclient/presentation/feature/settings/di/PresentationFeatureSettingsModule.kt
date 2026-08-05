package com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.di

import com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.SettingsViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.contract.SettingsScreenReducer
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val presentationFeatureSettingsModule = module {
    viewModelOf(::SettingsViewModel)
    factoryOf(::SettingsScreenReducer)
}
