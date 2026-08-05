package com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeColors
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.R as CoreUiR

/**
 * Debug view of the active palette.
 *
 * The token list is built explicitly rather than via `kotlin.reflect.memberProperties`: reflection
 * needed `kotlin-reflect` at runtime, could not see into the nested token groups, and produced
 * whatever order the JVM felt like. An explicit list also names each token the way call sites do.
 */
@Composable
internal fun AppColorsDialog(onDismiss: () -> Unit) {
    val entries = Theme.colors.toEntries()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(CoreUiR.string.settings_color_palette)) },
        text = {
            LazyColumn {
                items(entries, key = { it.name }) { entry ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Theme.spacing.s),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(SWATCH_SIZE)
                                .background(entry.color, CircleShape)
                                .border(1.dp, Theme.colors.semantic.border.primary, CircleShape),
                        )

                        Spacer(Modifier.width(Theme.spacing.s))

                        Text(text = entry.name, style = Theme.typography.bodySmall)
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

private data class ColorEntry(val name: String, val color: Color)

private fun ThemeColors.toEntries(): List<ColorEntry> = listOf(
    ColorEntry("background.primaryMain", semantic.background.primaryMain),
    ColorEntry("background.primaryContent", semantic.background.primaryContent),
    ColorEntry("background.secondaryMain", semantic.background.secondaryMain),
    ColorEntry("background.accent", semantic.background.accent),
    ColorEntry("background.accentPressed", semantic.background.accentPressed),
    ColorEntry("foreground.primary", semantic.foreground.primary),
    ColorEntry("foreground.secondary", semantic.foreground.secondary),
    ColorEntry("foreground.accent", semantic.foreground.accent),
    ColorEntry("foreground.onAccent", semantic.foreground.onAccent),
    ColorEntry("text.primary", semantic.text.primary),
    ColorEntry("text.secondary", semantic.text.secondary),
    ColorEntry("text.placeholder", semantic.text.placeholder),
    ColorEntry("text.onAccent", semantic.text.onAccent),
    ColorEntry("text.error", semantic.text.error),
    ColorEntry("border.primary", semantic.border.primary),
    ColorEntry("border.secondary", semantic.border.secondary),
    ColorEntry("border.error", semantic.border.error),
    ColorEntry("static.scrim", static.scrim),
)

private val SWATCH_SIZE = 40.dp
