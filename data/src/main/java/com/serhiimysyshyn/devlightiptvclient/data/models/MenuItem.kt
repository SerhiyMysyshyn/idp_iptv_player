package com.serhiimysyshyn.devlightiptvclient.data.models

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class MenuItem(
    val index: Long,
    val icon: ImageVector,
    val title: String
)
