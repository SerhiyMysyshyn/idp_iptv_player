package com.serhiimysyshyn.devlightiptvclient.data.core

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Covers the attribute extraction, which is where real playlists differ most: `tvg-logo` and
 * `tvg-name` are both optional and plenty of channels ship neither.
 */
class M3UParserTest {

    @Test
    fun `parses logo url when tvg-logo is present`() {
        val content = """
            #EXTM3U
            #EXTINF:-1 tvg-name="1+1" tvg-logo="https://cdn.example/1plus1.png" group-title="Розваги",1+1
            https://stream.example/1plus1.m3u8
        """.trimIndent()

        val channels = M3UParser.parse(content)

        assertEquals(1, channels.size)
        assertEquals("https://cdn.example/1plus1.png", channels.first().logoUrl)
    }

    @Test
    fun `logo url is empty when tvg-logo is absent`() {
        val content = """
            #EXTM3U
            #EXTINF:-1 tvg-name="СТБ" group-title="Розваги",СТБ
            https://stream.example/stb.m3u8
        """.trimIndent()

        val channels = M3UParser.parse(content)

        assertEquals("", channels.first().logoUrl)
    }

    /**
     * The parser carries attributes in mutable vars across lines, so a channel without a logo
     * following one with a logo must not inherit it.
     */
    @Test
    fun `logo url does not leak from the previous channel`() {
        val content = """
            #EXTM3U
            #EXTINF:-1 tvg-logo="https://cdn.example/first.png",Перший
            https://stream.example/first.m3u8
            #EXTINF:-1,Другий
            https://stream.example/second.m3u8
        """.trimIndent()

        val channels = M3UParser.parse(content)

        assertEquals(2, channels.size)
        assertEquals("https://cdn.example/first.png", channels[0].logoUrl)
        assertEquals("", channels[1].logoUrl)
    }

    @Test
    fun `falls back to the name after the comma when tvg-name is missing`() {
        val content = """
            #EXTM3U
            #EXTINF:-1 group-title="Новини",Суспільне
            https://stream.example/suspilne.m3u8
        """.trimIndent()

        val channels = M3UParser.parse(content)

        assertEquals("Суспільне", channels.first().name)
        assertEquals("Новини", channels.first().category)
    }
}
