package com.serhiimysyshyn.devlightiptvclient.data.repository

import android.content.Context
import com.serhiimysyshyn.devlightiptvclient.datastore.ThemeTypeProto
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.AppThemeType
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.themeDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ThemeRepository {
    fun getTheme(): Flow<AppThemeType>
    suspend fun updateTheme(theme: AppThemeType)
}

class ThemeRepositoryImpl(
    private val context: Context
): ThemeRepository {
    val themeFlow: Flow<AppThemeType> = context.themeDataStore.data
        .map { prefs ->
            when (prefs.theme) {
                ThemeTypeProto.THEME_DARK -> AppThemeType.DARK
                ThemeTypeProto.THEME_LIGHT -> AppThemeType.LIGHT
                else -> AppThemeType.SYSTEM
            }
        }

    override fun getTheme(): Flow<AppThemeType> = themeFlow

    override suspend fun updateTheme(theme: AppThemeType) {
        context.themeDataStore.updateData { prefs ->
            prefs.toBuilder()
                .setTheme(
                    when (theme) {
                        AppThemeType.DARK -> ThemeTypeProto.THEME_DARK
                        AppThemeType.LIGHT -> ThemeTypeProto.THEME_LIGHT
                        else -> ThemeTypeProto.THEME_SYSTEM
                    }
                )
                .build()
        }
    }
}