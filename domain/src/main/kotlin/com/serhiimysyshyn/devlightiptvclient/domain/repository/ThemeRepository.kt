package com.serhiimysyshyn.devlightiptvclient.domain.repository

import com.serhiimysyshyn.devlightiptvclient.domain.model.AppThemeType
import kotlinx.coroutines.flow.Flow

/** Persisted app theme selection. */
interface ThemeRepository {

    fun getTheme(): Flow<AppThemeType>

    suspend fun updateTheme(theme: AppThemeType)
}
