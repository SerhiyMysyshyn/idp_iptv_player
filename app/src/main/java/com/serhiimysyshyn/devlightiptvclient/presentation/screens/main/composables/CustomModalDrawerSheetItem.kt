package com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.IPTVClientTheme
import com.serhiimysyshyn.devlightiptvclient.presentation.utils.DevicePreviews

@Composable
fun CustomModalDrawerSheetItem(
    title: String,
    icon: ImageVector? = null,
    isSelected: Boolean = false,
    onClicked: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .padding(0.dp, 8.dp, 36.dp, 8.dp)
            .height(42.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 36.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 36.dp
                )
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    color = IPTVClientTheme.colors.onSurface
                )
            ) {
                onClicked.invoke()
            },
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 36.dp,
            bottomStart = 0.dp,
            bottomEnd = 36.dp
        ),
        colors = CardColors(
            containerColor = if (isSelected) {
                IPTVClientTheme.colors.surface
            } else {
                IPTVClientTheme.colors.onBackground
            },
            contentColor = IPTVClientTheme.colors.primary,
            disabledContainerColor = IPTVClientTheme.colors.onBackground,
            disabledContentColor = IPTVClientTheme.colors.primary
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(1f)
                .padding(horizontal = 16.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                )
            }

            Spacer(Modifier.width(16.dp))

            Text(
                text = title,
                fontSize = 18.sp,
                maxLines = 1,
                style = if (isSelected) {
                    IPTVClientTheme.typography.h2
                } else {
                    IPTVClientTheme.typography.body
                },
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
@DevicePreviews
fun CustomModalDrawerSheetItemPreview() {
    IPTVClientTheme {
        CustomModalDrawerSheetItem(
            title = "Плейлисти",
            icon = Icons.Default.PlayArrow,
            isSelected = true,
            onClicked = {}
        )
    }
}