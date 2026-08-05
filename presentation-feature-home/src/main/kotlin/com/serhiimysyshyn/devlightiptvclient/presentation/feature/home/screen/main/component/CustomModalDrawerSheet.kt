package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.model.MenuItem

@Composable
internal fun CustomModalDrawerSheet(
    menuItems: List<MenuItem>,
    selectedMenuItemIndex: Long,
    onMenuItemClicked: (MenuItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalDrawerSheet(
        modifier = modifier,
        drawerContainerColor = Theme.colors.semantic.background.primaryContent,
    ) {
        LazyColumn(modifier = Modifier.padding(vertical = Theme.spacing.s)) {
            items(
                items = menuItems,
                key = { item -> item.index },
            ) { item ->
                CustomModalDrawerSheetItem(
                    title = item.title,
                    icon = item.icon,
                    isSelected = item.index == selectedMenuItemIndex,
                    onClicked = { onMenuItemClicked(item) },
                    modifier = Modifier.padding(vertical = Theme.spacing.s),
                )
            }
        }
    }
}
