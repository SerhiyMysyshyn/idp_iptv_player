package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer

import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.Virtualizer
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi

/**
 * Describes one equalizer band as the UI needs it: where it sits on the spectrum and how far it
 * can be pushed. [levelMillibel] is the current gain; [minMillibel]/[maxMillibel] come from the
 * hardware and differ between devices.
 */
data class EqualizerBand(
    val index: Short,
    val centerFrequencyHz: Int,
    val levelMillibel: Short,
    val minMillibel: Short,
    val maxMillibel: Short,
)

/**
 * Wraps the platform [Equalizer], [BassBoost] and [Virtualizer] for one audio session.
 *
 * @param audioSessionIdProvider read lazily — ExoPlayer only assigns a session id once it has
 *   prepared a media item, so capturing the value at construction time would always give
 *   [C.AUDIO_SESSION_ID_UNSET].
 */
class PlayerEqualizer(
    private val audioSessionIdProvider: () -> Int,
) {
    private var equalizer: Equalizer? = null
    private var bassBoost: BassBoost? = null
    private var virtualizer: Virtualizer? = null

    /**
     * The session the effects are currently bound to. Tracked so [init] can detect that ExoPlayer
     * handed out a different session and rebuild the effects against it.
     */
    private var boundSessionId: Int = C.AUDIO_SESSION_ID_UNSET

    /** True once the platform accepted an [Equalizer] for the current session. */
    val isAvailable: Boolean get() = equalizer != null

    /**
     * Binds the audio effects to the current session, rebuilding them if the session changed.
     *
     * Safe to call repeatedly: ExoPlayer reports `AUDIO_SESSION_ID_UNSET` until it has prepared a
     * media item, so the first call usually does nothing and a later one does the real work.
     *
     * @return true when the effects are bound to a live session.
     */
    @OptIn(UnstableApi::class)
    fun init(): Boolean {
        val sessionId = audioSessionIdProvider()
        if (sessionId == C.AUDIO_SESSION_ID_UNSET) return false
        if (sessionId == boundSessionId && equalizer != null) return true

        release()

        // Some devices refuse to allocate an effect for a session; audio should still play, and
        // each effect is independent so a missing Virtualizer must not cost us the Equalizer.
        equalizer = runCatching {
            Equalizer(EFFECT_PRIORITY, sessionId).apply { enabled = true }
        }.getOrNull()
        bassBoost = runCatching {
            BassBoost(EFFECT_PRIORITY, sessionId).apply { enabled = true }
        }.getOrNull()
        virtualizer = runCatching {
            Virtualizer(EFFECT_PRIORITY, sessionId).apply { enabled = true }
        }.getOrNull()

        boundSessionId = if (equalizer != null) sessionId else C.AUDIO_SESSION_ID_UNSET

        return equalizer != null
    }

    /** Snapshot of every band, or an empty list when no equalizer could be allocated. */
    fun readBands(): List<EqualizerBand> {
        val equalizer = equalizer ?: return emptyList()
        val (min, max) = equalizer.bandLevelRange.let { it[0] to it[1] }

        return (0 until equalizer.numberOfBands).map { band ->
            val index = band.toShort()
            EqualizerBand(
                index = index,
                centerFrequencyHz = equalizer.getCenterFreq(index) / MILLI_HERTZ_PER_HERTZ,
                levelMillibel = equalizer.getBandLevel(index),
                minMillibel = min,
                maxMillibel = max,
            )
        }
    }

    fun setBandLevel(index: Short, millibel: Short) {
        val equalizer = equalizer ?: return
        val (min, max) = equalizer.bandLevelRange.let { it[0] to it[1] }

        runCatching { equalizer.setBandLevel(index, millibel.coerceIn(min, max)) }
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

    /** @param strength 0..1000, the platform's own scale for both effects. */
    fun setBassBoostStrength(strength: Short) {
        val effect = bassBoost ?: return
        if (!effect.strengthSupported) return

        runCatching { effect.setStrength(strength.coerceIn(NO_STRENGTH, MAX_STRENGTH)) }
    }

    /** @param strength 0..1000, the platform's own scale for both effects. */
    fun setVirtualizerStrength(strength: Short) {
        val effect = virtualizer ?: return
        if (!effect.strengthSupported) return

        runCatching { effect.setStrength(strength.coerceIn(NO_STRENGTH, MAX_STRENGTH)) }
    }

    fun release() {
        equalizer?.release()
        bassBoost?.release()
        virtualizer?.release()
        equalizer = null
        bassBoost = null
        virtualizer = null
        boundSessionId = C.AUDIO_SESSION_ID_UNSET
    }

    private companion object {
        const val EFFECT_PRIORITY = 0
        const val MILLI_HERTZ_PER_HERTZ = 1000
        const val NO_STRENGTH: Short = 0
        const val MAX_STRENGTH: Short = 1000
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
        EqualizerPreset.NORMAL, EqualizerPreset.CUSTOM -> emptyList()
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
