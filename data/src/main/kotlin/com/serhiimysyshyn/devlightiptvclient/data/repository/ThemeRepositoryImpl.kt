package com.serhiimysyshyn.devlightiptvclient.data.repository

import com.serhiimysyshyn.devlightiptvclient.data.preference.source.ThemePreferenceDataSource
import com.serhiimysyshyn.devlightiptvclient.domain.model.AppThemeType
import com.serhiimysyshyn.devlightiptvclient.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow

internal class ThemeRepositoryImpl(
    private val themePreferenceDataSource: ThemePreferenceDataSource,
) : ThemeRepository {

    override fun getTheme(): Flow<AppThemeType> = themePreferenceDataSource.observeTheme()

    override suspend fun updateTheme(theme: AppThemeType) {
        themePreferenceDataSource.updateTheme(theme)
    }
}
