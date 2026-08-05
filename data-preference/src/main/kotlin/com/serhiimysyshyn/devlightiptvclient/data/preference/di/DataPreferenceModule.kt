package com.serhiimysyshyn.devlightiptvclient.data.preference.di

import com.serhiimysyshyn.devlightiptvclient.data.preference.source.PlayerPreferenceDataSource
import com.serhiimysyshyn.devlightiptvclient.data.preference.source.PlayerPreferenceDataSourceImpl
import com.serhiimysyshyn.devlightiptvclient.data.preference.source.ThemePreferenceDataSource
import com.serhiimysyshyn.devlightiptvclient.data.preference.source.ThemePreferenceDataSourceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataPreferenceModule = module {
    single<ThemePreferenceDataSource> { ThemePreferenceDataSourceImpl(androidContext()) }
    single<PlayerPreferenceDataSource> { PlayerPreferenceDataSourceImpl(androidContext()) }
}
