package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer

import android.media.audiofx.Equalizer
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi

/**
 * Wraps the platform [Equalizer] and applies the band curve of the selected [EqualizerPreset].
 *
 * @param audioSessionIdProvider read lazily — ExoPlayer only assigns a session id once it has
 *   prepared a media item, so capturing the value at construction time would always give
 *   [C.AUDIO_SESSION_ID_UNSET].
 */
class PlayerEqualizer(
    private val audioSessionIdProvider: () -> Int,
) {
    private var equalizer: Equalizer? = null

    @OptIn(UnstableApi::class)
    fun init() {
        val sessionId = audioSessionIdProvider()
        if (sessionId == C.AUDIO_SESSION_ID_UNSET) return

        // Some devices refuse to allocate an equalizer for a session; audio should still play.
        equalizer = runCatching {
            Equalizer(EQUALIZER_PRIORITY, sessionId).apply { enabled = true }
        }.getOrNull()
    }

    fun applyPreset(preset: EqualizerPreset) {
        val equalizer = equalizer ?: return
        val range = equalizer.bandLevelRange
        val min = range[0]
        val max = range[1]

        for (band in 0 until equalizer.numberOfBands) {
            val bandIndex = band.toShort()
            val frequencyHz = equalizer.getCenterFreq(bandIndex) / MILLI_HERTZ_PER_HERTZ
            val level = preset.levelFor(frequencyHz, max)

            equalizer.setBandLevel(bandIndex, level.coerceIn(min, max))
        }
    }

    fun release() {
        equalizer?.release()
        equalizer = null
    }

    private companion object {
        const val EQUALIZER_PRIORITY = 0
        const val MILLI_HERTZ_PER_HERTZ = 1000
    }
}

/**
 * A boost applied to every band whose centre frequency falls inside [range].
 *
 * @param divisor the boost is `maxLevel / divisor`, so a smaller divisor means a stronger lift.
 */
private data class BandBoost(val range: IntRange, val divisor: Int)

private const val LOW_HZ = 250
private const val HIGH_HZ = 8_000
private const val BASS_MAX_HZ = 1_000
private const val TREBLE_MIN_HZ = 4_000

private val SUB_LOW = 0 until LOW_HZ
private val BASS = 0 until BASS_MAX_HZ
private val TREBLE = TREBLE_MIN_HZ + 1..Int.MAX_VALUE
private val ABOVE_HIGH = HIGH_HZ + 1..Int.MAX_VALUE
private val VOCAL_RANGE = 1_000..3_000
private val PRESENCE_RANGE = 1_000..4_000
private val MID_RANGE = LOW_HZ..2_000

/**
 * The curve of each preset, expressed as data.
 *
 * Written this way rather than as a nested `when` over frequency inside a `when` over preset:
 * the nested version had a cyclomatic complexity of 26 and made it hard to see that, say, POP and
 * ROCK differ only in one band.
 */
private val EqualizerPreset.boosts: List<BandBoost>
    get() = when (this) {
        EqualizerPreset.NORMAL -> emptyList()
        EqualizerPreset.BASS_BOOST -> listOf(BandBoost(BASS, divisor = 2))
        EqualizerPreset.TREBLE_BOOST -> listOf(BandBoost(TREBLE, divisor = 2))
        EqualizerPreset.VOCAL -> listOf(BandBoost(VOCAL_RANGE, divisor = 3))
        EqualizerPreset.POP -> listOf(
            BandBoost(SUB_LOW, divisor = 4),
            BandBoost(PRESENCE_RANGE, divisor = 3),
        )
        EqualizerPreset.ROCK -> listOf(
            BandBoost(SUB_LOW, divisor = 2),
            BandBoost(TREBLE, divisor = 3),
        )
        EqualizerPreset.JAZZ -> listOf(BandBoost(MID_RANGE, divisor = 3))
        EqualizerPreset.CLASSICAL -> listOf(
            BandBoost(SUB_LOW, divisor = 4),
            BandBoost(ABOVE_HIGH, divisor = 4),
        )
        EqualizerPreset.DANCE -> listOf(
            BandBoost(SUB_LOW, divisor = 2),
            BandBoost(ABOVE_HIGH, divisor = 2),
        )
    }

/** First matching boost wins; bands outside every range stay flat. */
private fun EqualizerPreset.levelFor(frequencyHz: Int, max: Short): Short =
    boosts.firstOrNull { frequencyHz in it.range }
        ?.let { (max / it.divisor).toShort() }
        ?: 0
