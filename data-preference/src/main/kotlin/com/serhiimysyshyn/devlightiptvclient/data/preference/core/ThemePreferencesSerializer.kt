package com.serhiimysyshyn.devlightiptvclient.data.preference.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.serhiimysyshyn.devlightiptvclient.data.preference.datastore.ThemePreferences
import com.serhiimysyshyn.devlightiptvclient.data.preference.datastore.ThemeTypeProto
import java.io.InputStream
import java.io.OutputStream

/**
 * Proto DataStore serializer for the persisted theme selection.
 *
 * A corrupted file falls back to [defaultValue] rather than crashing — a broken preferences blob
 * must never stop the app from starting.
 */
object ThemePreferencesSerializer : Serializer<ThemePreferences> {

    override val defaultValue: ThemePreferences = ThemePreferences
        .newBuilder()
        .setTheme(ThemeTypeProto.THEME_SYSTEM)
        .build()

    override suspend fun readFrom(input: InputStream): ThemePreferences =
        try {
            ThemePreferences.parseFrom(input)
        } catch (_: InvalidProtocolBufferException) {
            defaultValue
        }

    override suspend fun writeTo(t: ThemePreferences, output: OutputStream) {
        t.writeTo(output)
    }
}

internal val Context.themeDataStore: DataStore<ThemePreferences> by dataStore(
    fileName = THEME_PREFERENCES_FILE_NAME,
    serializer = ThemePreferencesSerializer,
)

private const val THEME_PREFERENCES_FILE_NAME = "iptv_theme_prefs.pb"
