package com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.screen.settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.preview.DevicePreviews
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.theme.AppTheme

/** [SettingsRowItem]'s sibling for boolean preferences — same card, a Switch instead of a chevron. */
@Composable
internal fun SettingsSwitchItem(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: String = "",
) {
    Card(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = Theme.spacing.m, vertical = Theme.spacing.s),
        shape = RoundedCornerShape(Theme.radius.l),
        colors = CardDefaults.cardColors(
            containerColor = Theme.colors.semantic.background.primaryContent,
            contentColor = Theme.colors.semantic.foreground.primary,
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Theme.spacing.m),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = Theme.typography.title,
                    color = Theme.colors.semantic.text.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                if (description.isNotEmpty()) {
                    Spacer(Modifier.height(Theme.spacing.xs))

                    Text(
                        text = description,
                        style = Theme.typography.caption,
                        color = Theme.colors.semantic.text.secondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Spacer(Modifier.padding(horizontal = Theme.spacing.xs))

            Switch(checked = isChecked, onCheckedChange = onCheckedChange)
        }
    }
}

@DevicePreviews
@Composable
private fun SettingsSwitchItemPreview() {
    AppTheme {
        SettingsSwitchItem(
            title = "Картинка в картинці",
            description = "Продовжувати відтворення у маленькому вікні",
            isChecked = true,
            onCheckedChange = {},
        )
    }
}
