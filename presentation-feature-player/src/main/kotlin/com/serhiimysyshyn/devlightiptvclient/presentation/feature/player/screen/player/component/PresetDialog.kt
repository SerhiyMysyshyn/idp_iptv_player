package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer.EqualizerPreset
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.R as CoreUiR

@Composable
internal fun PresetDialog(
    currentPreset: EqualizerPreset,
    onPresetSelected: (EqualizerPreset) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(CoreUiR.string.player_select_preset)) },
        text = {
            // Nine presets overflow a short dialog on small screens, so the list scrolls.
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                EqualizerPreset.entries.forEach { preset ->
                    val isSelected = preset == currentPreset

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = isSelected,
                                role = Role.RadioButton,
                                onClick = { onPresetSelected(preset) },
                            )
                            .padding(vertical = Theme.spacing.s),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected = isSelected, onClick = null)

                        Text(
                            text = preset.name,
                            style = Theme.typography.body,
                            modifier = Modifier.padding(start = Theme.spacing.s),
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(CoreUiR.string.action_close))
            }
        },
    )
}
