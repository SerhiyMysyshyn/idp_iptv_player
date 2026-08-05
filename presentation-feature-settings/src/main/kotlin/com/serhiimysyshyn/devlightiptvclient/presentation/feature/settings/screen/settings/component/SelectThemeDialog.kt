package com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.serhiimysyshyn.devlightiptvclient.domain.model.AppThemeType
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.R as CoreUiR

@Composable
internal fun SelectThemeDialog(
    currentTheme: AppThemeType,
    onThemeSelected: (AppThemeType) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(CoreUiR.string.settings_select_theme)) },
        text = {
            Column {
                AppThemeType.entries.forEach { theme ->
                    val isSelected = theme == currentTheme

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            // `selectable` instead of `clickable` so the whole row is announced
                            // as one radio option rather than as a button plus a radio button.
                            .selectable(
                                selected = isSelected,
                                role = Role.RadioButton,
                                onClick = { onThemeSelected(theme) },
                            )
                            .padding(vertical = Theme.spacing.s),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected = isSelected, onClick = null)

                        Text(
                            text = theme.label(),
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
