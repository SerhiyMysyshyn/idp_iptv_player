package com.serhiimysyshyn.devlightiptvclient

import com.serhiimysyshyn.devlightiptvclient.datastore.ThemePreferences
import com.serhiimysyshyn.devlightiptvclient.datastore.ThemeTypeProto
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.ThemePreferencesSerializer
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class ThemePreferencesSerializerTest {

    private val serializer = ThemePreferencesSerializer

    @Test
    fun default_value_is_system() {
        val defaultPrefs = serializer.defaultValue
        assertEquals(ThemeTypeProto.THEME_SYSTEM, defaultPrefs.theme)
    }

    @Test
    fun write_and_read_returns_same_object() = runTest {
        val prefs = ThemePreferences.newBuilder()
            .setTheme(ThemeTypeProto.THEME_DARK)
            .build()

        val output = ByteArrayOutputStream()
        serializer.writeTo(prefs, output)

        val input = ByteArrayInputStream(output.toByteArray())
        val result = serializer.readFrom(input)

        assertEquals(prefs, result)
    }

    @Test
    fun read_corrupted_data_returns_default() = runTest {
        val corrupted = byteArrayOf(0x0A, 0x0B, 0x0C)
        val input = ByteArrayInputStream(corrupted)

        val result = serializer.readFrom(input)

        assertEquals(
            ThemeTypeProto.THEME_SYSTEM,
            result.theme
        )
    }
}