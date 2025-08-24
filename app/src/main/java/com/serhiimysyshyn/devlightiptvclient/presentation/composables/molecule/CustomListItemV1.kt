package com.serhiimysyshyn.devlightiptvclient.presentation.composables.molecule

import android.graphics.drawable.Drawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.IPTVClientTheme

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CustomListItemV1(
    modifier: Modifier = Modifier,
    title: String,
    description: String = "",
    icon: Drawable? = null,
    onItemClicked: () -> Unit,
    functionalIcon: ImageVector? = null,
    onFunctionalIconClicked: () -> Unit = {},
) {
    Card(
        onClick = onItemClicked,
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
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
                .fillMaxHeight()
                .fillMaxWidth(1f)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            if (icon != null) {
                GlideImage(
                    model = icon,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = IPTVClientTheme.colors.primary),
                    modifier = Modifier
                        .size(36.dp, 36.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            ) {
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

            Spacer(Modifier.width(8.dp))

            if (functionalIcon != null) {
                Icon(
                    functionalIcon,
                    null,
                    modifier = Modifier
                        .clickable {
                            onFunctionalIconClicked.invoke()
                        }
                )
            }
        }
    }
}