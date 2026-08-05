package com.serhiimysyshyn.devlightiptvclient.data.preference

import com.serhiimysyshyn.devlightiptvclient.data.preference.core.ThemePreferencesSerializer
import com.serhiimysyshyn.devlightiptvclient.data.preference.datastore.ThemePreferences
import com.serhiimysyshyn.devlightiptvclient.data.preference.datastore.ThemeTypeProto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class ThemePreferencesSerializerTest {

    private val serializer = ThemePreferencesSerializer

    @Test
    fun `default value is system`() {
        val defaultPrefs = serializer.defaultValue

        assertEquals(ThemeTypeProto.THEME_SYSTEM, defaultPrefs.theme)
    }

    @Test
    fun `write and read returns same object`() = runTest {
        val prefs = ThemePreferences.newBuilder()
            .setTheme(ThemeTypeProto.THEME_DARK)
            .build()

        val output = ByteArrayOutputStream()
        serializer.writeTo(prefs, output)

        val result = serializer.readFrom(ByteArrayInputStream(output.toByteArray()))

        assertEquals(prefs, result)
    }

    @Test
    fun `read corrupted data returns default`() = runTest {
        val corrupted = byteArrayOf(0x0A, 0x0B, 0x0C)

        val result = serializer.readFrom(ByteArrayInputStream(corrupted))

        assertEquals(ThemeTypeProto.THEME_SYSTEM, result.theme)
    }
}
