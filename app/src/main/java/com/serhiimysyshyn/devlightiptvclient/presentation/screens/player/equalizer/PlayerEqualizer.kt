package com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.equalizer

import android.media.audiofx.Equalizer
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer

class PlayerEqualizer(
    private val exoPlayer: ExoPlayer
) {
    private var equalizer: Equalizer? = null

    @OptIn(UnstableApi::class)
    fun init() {
        if (exoPlayer.audioSessionId != C.AUDIO_SESSION_ID_UNSET) {
            equalizer = Equalizer(0, exoPlayer.audioSessionId).apply {
                enabled = true
            }
        }
    }

    fun applyPreset(preset: EqualizerPreset) {
        val eq = equalizer ?: return
        val bands = eq.numberOfBands
        val range = eq.bandLevelRange
        val min = range[0]
        val max = range[1]

        for (band in 0 until bands) {
            val freq = eq.getCenterFreq(band.toShort()) / 1000
            val level: Short = when (preset) {
                EqualizerPreset.NORMAL -> 0

                EqualizerPreset.BASS_BOOST -> {
                    if (eq.getCenterFreq(band.toShort()) < 1_000_000) {
                        max / 2
                    } else {
                        0
                    }
                }

                EqualizerPreset.TREBLE_BOOST -> {
                    if (eq.getCenterFreq(band.toShort()) > 4_000_000) {
                        max / 2
                    } else {
                        0
                    }
                }

                EqualizerPreset.VOCAL -> {
                    if (freq in 1000..3000) {
                        max / 3
                    } else {
                        0
                    }
                }

                EqualizerPreset.POP -> when {
                    freq < 250 -> max / 4
                    freq in 1000..4000 -> max / 3
                    else -> 0
                }

                EqualizerPreset.ROCK -> when {
                    freq < 250 -> max / 2
                    freq > 4000 -> max / 3
                    else -> 0
                }

                EqualizerPreset.JAZZ -> when {
                    freq in 250..2000 -> max / 3
                    else -> 0
                }

                EqualizerPreset.CLASSICAL -> when {
                    freq < 250 -> max / 4
                    freq > 8000 -> max / 4
                    else -> 0
                }

                EqualizerPreset.DANCE -> when {
                    freq < 250 -> max / 2
                    freq > 8000 -> max / 2
                    else -> 0
                }
            }.toShort()
            eq.setBandLevel(band.toShort(), level.coerceIn(min, max))
        }
    }

    fun release() {
        equalizer?.release()
        equalizer = null
    }
}