package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer.EqualizerBand
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer.EqualizerPreset
import kotlin.math.roundToInt
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.R as CoreUiR

private const val MAX_EFFECT_STRENGTH = 1000f
private const val MILLIBEL_PER_DECIBEL = 100f
private val BAND_SLIDER_HEIGHT = 160.dp
private val BAND_COLUMN_WIDTH = 64.dp

/**
 * The audio panel: preset chips, one vertical slider per hardware equalizer band, and the two
 * global effects.
 *
 * A sheet rather than a dialog so the video stays visible and audible while dragging — the whole
 * point is hearing the change as it happens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AudioSettingsBottomSheet(
    currentPreset: EqualizerPreset,
    bands: List<EqualizerBand>,
    bassBoostStrength: Short,
    virtualizerStrength: Short,
    onPresetSelected: (EqualizerPreset) -> Unit,
    onBandLevelChange: (Short, Short) -> Unit,
    onBassBoostChange: (Short) -> Unit,
    onVirtualizerChange: (Short) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Theme.colors.semantic.background.primaryMain,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Theme.spacing.m)
                .padding(bottom = Theme.spacing.xl),
        ) {
            Text(
                text = stringResource(CoreUiR.string.player_audio_settings),
                style = Theme.typography.h2,
                color = Theme.colors.semantic.text.primary,
            )

            SectionLabel(CoreUiR.string.player_audio_presets)

            PresetChips(currentPreset = currentPreset, onPresetSelected = onPresetSelected)

            if (bands.isEmpty()) {
                Text(
                    text = stringResource(CoreUiR.string.player_audio_unavailable),
                    style = Theme.typography.body,
                    color = Theme.colors.semantic.text.secondary,
                    modifier = Modifier.padding(top = Theme.spacing.m),
                )
            } else {
                SectionLabel(CoreUiR.string.player_audio_bands)

                BandSliders(bands = bands, onBandLevelChange = onBandLevelChange)
            }

            SectionLabel(CoreUiR.string.player_audio_bass_boost)

            EffectSlider(strength = bassBoostStrength, onChange = onBassBoostChange)

            SectionLabel(CoreUiR.string.player_audio_virtualizer)

            EffectSlider(strength = virtualizerStrength, onChange = onVirtualizerChange)

            TextButton(
                onClick = onReset,
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(stringResource(CoreUiR.string.player_audio_reset))
            }
        }
    }
}

@Composable
private fun SectionLabel(textRes: Int) {
    Text(
        text = stringResource(textRes),
        style = Theme.typography.body,
        color = Theme.colors.semantic.text.secondary,
        modifier = Modifier.padding(top = Theme.spacing.m, bottom = Theme.spacing.s),
    )
}

@Composable
private fun PresetChips(
    currentPreset: EqualizerPreset,
    onPresetSelected: (EqualizerPreset) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.s),
    ) {
        EqualizerPreset.entries.forEach { preset ->
            // CUSTOM is a state the user lands in by dragging a slider, never one they pick.
            if (preset == EqualizerPreset.CUSTOM && currentPreset != EqualizerPreset.CUSTOM) {
                return@forEach
            }

            FilterChip(
                selected = preset == currentPreset,
                onClick = { onPresetSelected(preset) },
                enabled = preset != EqualizerPreset.CUSTOM,
                label = { Text(preset.name) },
            )
        }
    }
}

@Composable
private fun BandSliders(
    bands: List<EqualizerBand>,
    onBandLevelChange: (Short, Short) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.s),
    ) {
        bands.forEach { band ->
            BandSlider(band = band, onLevelChange = { onBandLevelChange(band.index, it) })
        }
    }
}

@Composable
private fun BandSlider(
    band: EqualizerBand,
    onLevelChange: (Short) -> Unit,
) {
    Column(
        modifier = Modifier.width(BAND_COLUMN_WIDTH),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(
                CoreUiR.string.player_audio_band_level,
                (band.levelMillibel / MILLIBEL_PER_DECIBEL).roundToInt(),
            ),
            style = Theme.typography.caption,
            color = Theme.colors.semantic.text.primary,
        )

        VerticalSlider(
            value = band.levelMillibel.toFloat(),
            valueRange = band.minMillibel.toFloat()..band.maxMillibel.toFloat(),
            onValueChange = { onLevelChange(it.roundToInt().toShort()) },
            modifier = Modifier.height(BAND_SLIDER_HEIGHT),
        )

        Text(
            text = band.frequencyLabel(),
            style = Theme.typography.caption,
            color = Theme.colors.semantic.text.secondary,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Material3 has no vertical slider, so a horizontal one is rotated a quarter turn.
 *
 * `graphicsLayer` alone would rotate the visuals but keep the original hit box, so a custom
 * `layout` swaps the measured width and height — that keeps touch handling aligned with what the
 * user sees.
 */
@Composable
private fun VerticalSlider(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Slider(
        value = value,
        valueRange = valueRange,
        onValueChange = onValueChange,
        modifier = modifier
            .graphicsLayer { rotationZ = 270f }
            .layout { measurable, constraints ->
                val placeable = measurable.measure(
                    constraints.copy(
                        minWidth = constraints.minHeight,
                        maxWidth = constraints.maxHeight,
                        minHeight = constraints.minWidth,
                        maxHeight = constraints.maxWidth,
                    ),
                )

                layout(placeable.height, placeable.width) {
                    placeable.place(
                        x = -(placeable.width / 2 - placeable.height / 2),
                        y = -(placeable.height / 2 - placeable.width / 2),
                    )
                }
            },
    )
}

@Composable
private fun EffectSlider(
    strength: Short,
    onChange: (Short) -> Unit,
) {
    Slider(
        value = strength.toFloat(),
        valueRange = 0f..MAX_EFFECT_STRENGTH,
        onValueChange = { onChange(it.roundToInt().toShort()) },
        modifier = Modifier.fillMaxWidth(),
    )
}

/** Sub-kilohertz bands read better as "230 Hz"; above that as "3.6 kHz". */
@Composable
private fun EqualizerBand.frequencyLabel(): String = if (centerFrequencyHz < 1_000) {
    stringResource(CoreUiR.string.player_audio_band_hz, centerFrequencyHz)
} else {
    val kilohertz = centerFrequencyHz / 1_000f
    stringResource(CoreUiR.string.player_audio_band_khz, formatKilohertz(kilohertz))
}

/** Drops the decimal when it would read "14.0". */
private fun formatKilohertz(value: Float): String =
    if (value % 1f == 0f) value.toInt().toString() else ((value * 10).roundToInt() / 10f).toString()
