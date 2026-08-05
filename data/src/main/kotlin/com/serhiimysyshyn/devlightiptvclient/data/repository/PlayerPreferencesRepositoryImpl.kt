package com.serhiimysyshyn.devlightiptvclient.data.repository

import com.serhiimysyshyn.devlightiptvclient.data.preference.source.PlayerPreferenceDataSource
import com.serhiimysyshyn.devlightiptvclient.domain.repository.PlayerPreferencesRepository
import kotlinx.coroutines.flow.Flow

internal class PlayerPreferencesRepositoryImpl(
    private val playerPreferenceDataSource: PlayerPreferenceDataSource,
) : PlayerPreferencesRepository {

    override fun isPictureInPictureEnabled(): Flow<Boolean> =
        playerPreferenceDataSource.observePictureInPictureEnabled()

    override suspend fun updatePictureInPictureEnabled(isEnabled: Boolean) {
        playerPreferenceDataSource.updatePictureInPictureEnabled(isEnabled)
    }
}
