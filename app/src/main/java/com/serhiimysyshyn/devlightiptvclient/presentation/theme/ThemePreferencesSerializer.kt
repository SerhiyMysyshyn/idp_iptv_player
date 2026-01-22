package com.serhiimysyshyn.devlightiptvclient.presentation.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.serhiimysyshyn.devlightiptvclient.datastore.ThemePreferences
import com.serhiimysyshyn.devlightiptvclient.datastore.ThemeTypeProto
import java.io.InputStream
import java.io.OutputStream

object ThemePreferencesSerializer : Serializer<ThemePreferences> {
    override val defaultValue: ThemePreferences = ThemePreferences
        .newBuilder()
        .setTheme(ThemeTypeProto.THEME_SYSTEM)
        .build()

    override suspend fun readFrom(input: InputStream): ThemePreferences =
        try {
            ThemePreferences.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            defaultValue
        }

    override suspend fun writeTo(t: ThemePreferences, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.themeDataStore: DataStore<ThemePreferences> by dataStore(
    fileName = "iptv_theme_prefs.pb",
    serializer = ThemePreferencesSerializer
)