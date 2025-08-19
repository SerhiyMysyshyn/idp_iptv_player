package com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import com.serhiimysyshyn.devlightiptvclient.data.models.MenuItem
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.IPTVClientTheme

@Composable
fun CustomModalDrawerSheet(
    menuItems: List<MenuItem>,
    selectedMenuItemIndex: Long,
    onMenuItemClicked: (MenuItem) -> Unit,
) {
    ModalDrawerSheet(
        drawerContainerColor = IPTVClientTheme.colors.onBackground
    ) {
        LazyColumn {
            items(menuItems) { item ->
                CustomModalDrawerSheetItem(
                    title = item.title,
                    icon = item.icon,
                    isSelected = item.index == selectedMenuItemIndex,
                    onClicked = {
                        onMenuItemClicked.invoke(item)
                    }
                )
            }
        }
    }
}