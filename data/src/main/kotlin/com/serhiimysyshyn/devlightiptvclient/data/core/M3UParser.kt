package com.serhiimysyshyn.devlightiptvclient.data.core

import com.serhiimysyshyn.devlightiptvclient.domain.model.Channel

/**
 * Minimal `#EXTM3U` parser.
 *
 * Kept separate from the repository so it can be unit-tested on plain strings without a database
 * or an HTTP client.
 */
internal object M3UParser {

    private val NAME_REGEX = Regex("tvg-name=\"(.*?)\"")
    private val CATEGORY_REGEX = Regex("group-title=\"(.*?)\"")

    private const val INFO_PREFIX = "#EXTINF"
    private const val URL_PREFIX = "http"
    private const val UNCATEGORISED = "Без категорії"

    fun parse(content: String): List<Channel> {
        val channels = mutableListOf<Channel>()
        var currentName = ""
        var currentCategory = UNCATEGORISED

        content.lineSequence().forEach { line ->
            when {
                line.startsWith(INFO_PREFIX) -> {
                    currentName = NAME_REGEX.find(line)?.groupValues?.get(1)
                        ?: line.substringAfter(",").trim()
                    currentCategory = CATEGORY_REGEX.find(line)?.groupValues?.get(1)
                        ?: UNCATEGORISED
                }

                line.startsWith(URL_PREFIX) -> channels += Channel(
                    name = currentName,
                    url = line.trim(),
                    category = currentCategory,
                )
            }
        }

        return channels
    }
}
