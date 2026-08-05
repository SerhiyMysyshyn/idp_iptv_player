package com.serhiimysyshyn.devlightiptvclient.data.preference.source

import android.content.Context
import com.serhiimysyshyn.devlightiptvclient.data.preference.core.playerDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Reads and writes playback preferences.
 *
 * Callers see a plain "is it enabled" boolean; that the stored proto field records the opt-out
 * instead is a storage detail that stays in this module.
 */
interface PlayerPreferenceDataSource {

    fun observePictureInPictureEnabled(): Flow<Boolean>

    suspend fun updatePictureInPictureEnabled(isEnabled: Boolean)
}

internal class PlayerPreferenceDataSourceImpl(
    private val context: Context,
) : PlayerPreferenceDataSource {

    override fun observePictureInPictureEnabled(): Flow<Boolean> =
        context.playerDataStore.data.map { preferences -> !preferences.pictureInPictureDisabled }

    override suspend fun updatePictureInPictureEnabled(isEnabled: Boolean) {
        context.playerDataStore.updateData { preferences ->
            preferences.toBuilder()
                .setPictureInPictureDisabled(!isEnabled)
                .build()
        }
    }
}
