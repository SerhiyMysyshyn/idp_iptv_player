package com.serhiimysyshyn.devlightiptvclient.domain.repository

import kotlinx.coroutines.flow.Flow

/** Persisted playback preferences. */
interface PlayerPreferencesRepository {

    fun isPictureInPictureEnabled(): Flow<Boolean>

    suspend fun updatePictureInPictureEnabled(isEnabled: Boolean)
}
