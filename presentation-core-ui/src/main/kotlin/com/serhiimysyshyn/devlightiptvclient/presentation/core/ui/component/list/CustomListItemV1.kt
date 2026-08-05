package com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.component.list

import android.graphics.drawable.Drawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.preview.DevicePreviews
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.theme.AppTheme

/**
 * Standard content row: leading image, title with optional description, optional trailing action.
 *
 * Used for both playlists and channels.
 *
 * @param functionalIcon trailing icon; when null the trailing slot is omitted entirely.
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CustomListItemV1(
    title: String,
    onItemClicked: () -> Unit,
    modifier: Modifier = Modifier,
    description: String = "",
    icon: Drawable? = null,
    functionalIcon: ImageVector? = null,
    onFunctionalIconClicked: () -> Unit = {},
) {
    Card(
        onClick = onItemClicked,
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        shape = RoundedCornerShape(Theme.radius.m),
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
            if (icon != null) {
                GlideImage(
                    model = icon,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Theme.colors.semantic.foreground.primary),
                    modifier = Modifier.size(Theme.size.iconL),
                )

                Spacer(Modifier.width(Theme.spacing.m))
            }

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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            if (functionalIcon != null) {
                Spacer(Modifier.width(Theme.spacing.s))

                Icon(
                    imageVector = functionalIcon,
                    contentDescription = null,
                    tint = Theme.colors.semantic.foreground.accent,
                    modifier = Modifier
                        .size(Theme.size.iconM)
                        .clickable(onClick = onFunctionalIconClicked),
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun CustomListItemV1Preview() {
    AppTheme {
        CustomListItemV1(
            title = "Перший канал",
            description = "Новини",
            functionalIcon = Icons.Default.FavoriteBorder,
            onItemClicked = {},
        )
    }
}
