package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.preview.DevicePreviews
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.theme.AppTheme

/** One drawer row, with a rounded right edge that highlights when selected. */
@Composable
internal fun CustomModalDrawerSheetItem(
    title: String,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isSelected: Boolean = false,
) {
    val shape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = Theme.radius.xl,
        bottomStart = 0.dp,
        bottomEnd = Theme.radius.xl,
    )

    Card(
        // `Card(onClick = …)` gives the correct ripple, shape clipping and click semantics for
        // free; the previous hand-rolled `Modifier.clickable` had to reimplement all three.
        onClick = onClicked,
        modifier = modifier
            .padding(end = Theme.spacing.xl)
            .height(Theme.size.rowHeight)
            .fillMaxWidth(),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                Theme.colors.semantic.background.accent
            } else {
                Theme.colors.semantic.background.primaryContent
            },
            contentColor = if (isSelected) {
                Theme.colors.semantic.foreground.onAccent
            } else {
                Theme.colors.semantic.foreground.primary
            },
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(horizontal = Theme.spacing.m),
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(Theme.size.iconM),
                )

                Spacer(Modifier.width(Theme.spacing.m))
            }

            Text(
                text = title,
                style = if (isSelected) Theme.typography.title else Theme.typography.body,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@DevicePreviews
@Composable
private fun CustomModalDrawerSheetItemPreview() {
    AppTheme {
        CustomModalDrawerSheetItem(
            title = "Плейлисти",
            icon = Icons.Default.PlayArrow,
            isSelected = true,
            onClicked = {},
        )
    }
}
