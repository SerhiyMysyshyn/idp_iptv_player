package com.serhiimysyshyn.devlightiptvclient.presentation.composables.organism

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.IPTVClientTheme
import com.serhiimysyshyn.devlightiptvclient.presentation.utils.DevicePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    modifier: Modifier = Modifier,
    title: String,
    openMenuIcon: ImageVector,
    onClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = IPTVClientTheme.typography.h2,
                maxLines = 1
            )
        },
        colors = TopAppBarColors(
            containerColor = IPTVClientTheme.colors.onBackground,
            scrolledContainerColor = IPTVClientTheme.colors.onBackground,
            navigationIconContentColor = IPTVClientTheme.colors.primary,
            titleContentColor = IPTVClientTheme.colors.primary,
            actionIconContentColor = IPTVClientTheme.colors.primary
        ),
        navigationIcon = {
            IconButton(onClick = { onClick.invoke() }) {
                Icon(openMenuIcon, contentDescription = "Відкрити меню")
            }
        }
    )
}

@Composable
@DevicePreviews
fun MainAppBarPreview() {
    IPTVClientTheme {
        MainAppBar(
            title = "Test title",
            openMenuIcon = Icons.Default.Menu,
            onClick = {}
        )
    }
}