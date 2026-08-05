package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * A navigation drawer row.
 *
 * Holds an [ImageVector], so it is a presentation type and lives with the screen that renders it —
 * it used to sit in `:data` alongside database models.
 */
@Immutable
data class MenuItem(
    val index: Long,
    val icon: ImageVector,
    val title: String,
)
