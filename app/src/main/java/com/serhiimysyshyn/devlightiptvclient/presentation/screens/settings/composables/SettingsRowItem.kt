package com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.IPTVClientTheme
import com.serhiimysyshyn.devlightiptvclient.presentation.utils.DevicePreviews

@Composable
fun SettingsRowItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String = "",
    value: String = "",
    onItemClicked: () -> Unit,
) {
    Card(
        onClick = onItemClicked,
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = ShapeDefaults.Medium,
        colors = CardColors(
            containerColor = IPTVClientTheme.colors.onBackground,
            contentColor = IPTVClientTheme.colors.primary,
            disabledContainerColor = IPTVClientTheme.colors.onBackground,
            disabledContentColor = IPTVClientTheme.colors.primary
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (description.isNotEmpty()) {
                    Spacer(Modifier.width(4.dp))

                    Text(
                        text = description,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            if (value.isNotEmpty()) {
                Text(
                    text = value,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@Composable
@DevicePreviews
fun SettingsRowItemPreview() {
    IPTVClientTheme {
        SettingsRowItem(
            title = "Тема додатку",
            value = "Dark",
            onItemClicked = {}
        )
    }
}