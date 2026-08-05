package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer

enum class EqualizerPreset {
    NORMAL,
    BASS_BOOST,
    TREBLE_BOOST,
    VOCAL,
    POP,
    ROCK,
    JAZZ,
    CLASSICAL,
    DANCE,

    /**
     * Not a curve of its own — the state the equalizer falls into once the user drags a band
     * slider, so the UI can stop claiming a named preset is active.
     */
    CUSTOM,
}
