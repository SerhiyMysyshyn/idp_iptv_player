package com.serhiimysyshyn.devlightiptvclient.data.preference.source

import android.content.Context
import com.serhiimysyshyn.devlightiptvclient.data.preference.core.themeDataStore
import com.serhiimysyshyn.devlightiptvclient.data.preference.datastore.ThemeTypeProto
import com.serhiimysyshyn.devlightiptvclient.domain.model.AppThemeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Reads and writes the persisted theme.
 *
 * The generated protobuf types stay inside this module — callers only ever see [AppThemeType],
 * so the storage format can change without touching anything above the data layer.
 */
interface ThemePreferenceDataSource {

    fun observeTheme(): Flow<AppThemeType>

    suspend fun updateTheme(theme: AppThemeType)
}

internal class ThemePreferenceDataSourceImpl(
    private val context: Context,
) : ThemePreferenceDataSource {

    override fun observeTheme(): Flow<AppThemeType> =
        context.themeDataStore.data.map { preferences -> preferences.theme.toDomain() }

    override suspend fun updateTheme(theme: AppThemeType) {
        context.themeDataStore.updateData { preferences ->
            preferences.toBuilder()
                .setTheme(theme.toProto())
                .build()
        }
    }
}

private fun ThemeTypeProto.toDomain(): AppThemeType = when (this) {
    ThemeTypeProto.THEME_LIGHT -> AppThemeType.LIGHT
    ThemeTypeProto.THEME_DARK -> AppThemeType.DARK
    // THEME_SYSTEM and UNRECOGNIZED both mean "follow the system".
    else -> AppThemeType.SYSTEM
}

private fun AppThemeType.toProto(): ThemeTypeProto = when (this) {
    AppThemeType.LIGHT -> ThemeTypeProto.THEME_LIGHT
    AppThemeType.DARK -> ThemeTypeProto.THEME_DARK
    AppThemeType.SYSTEM -> ThemeTypeProto.THEME_SYSTEM
}
