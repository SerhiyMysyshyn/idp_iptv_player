package com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.component.appbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.preview.DevicePreviews
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.theme.AppTheme
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.R

/**
 * Top app bar with a single leading action.
 *
 * @param navigationIcon drawn in the leading slot — a hamburger on the main screen, a back arrow
 *   on pushed screens.
 * @param navigationContentDescription spoken by TalkBack for that action; required so the button
 *   is not announced as an unlabelled control.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    title: String,
    navigationIcon: ImageVector,
    navigationContentDescription: String,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = Theme.typography.h2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Theme.colors.semantic.background.primaryContent,
            scrolledContainerColor = Theme.colors.semantic.background.primaryContent,
            navigationIconContentColor = Theme.colors.semantic.foreground.primary,
            titleContentColor = Theme.colors.semantic.text.primary,
            actionIconContentColor = Theme.colors.semantic.foreground.primary,
        ),
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = navigationContentDescription,
                )
            }
        },
    )
}

@DevicePreviews
@Composable
private fun MainAppBarPreview() {
    AppTheme {
        MainAppBar(
            title = stringResource(R.string.app_name),
            navigationIcon = Icons.Default.Menu,
            navigationContentDescription = stringResource(R.string.open_menu),
            onNavigationClick = {},
        )
    }
}
