package com.serhiimysyshyn.devlightiptvclient.data.preference.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.serhiimysyshyn.devlightiptvclient.data.preference.datastore.PlayerPreferences
import java.io.InputStream
import java.io.OutputStream

/**
 * Proto DataStore serializer for playback preferences.
 *
 * A corrupted file falls back to [defaultValue] rather than crashing — a broken preferences blob
 * must never stop the app from starting.
 */
object PlayerPreferencesSerializer : Serializer<PlayerPreferences> {

    /** All-defaults, which means Picture-in-Picture enabled (the flag stores the opt-out). */
    override val defaultValue: PlayerPreferences = PlayerPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): PlayerPreferences =
        try {
            PlayerPreferences.parseFrom(input)
        } catch (_: InvalidProtocolBufferException) {
            defaultValue
        }

    override suspend fun writeTo(t: PlayerPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}

internal val Context.playerDataStore: DataStore<PlayerPreferences> by dataStore(
    fileName = PLAYER_PREFERENCES_FILE_NAME,
    serializer = PlayerPreferencesSerializer,
)

private const val PLAYER_PREFERENCES_FILE_NAME = "iptv_player_prefs.pb"
