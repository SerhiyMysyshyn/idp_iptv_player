package com.serhiimysyshyn.devlightiptvclient.presentation.utils

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import com.serhiimysyshyn.devlightiptvclient.R
import com.serhiimysyshyn.devlightiptvclient.data.models.MenuItem

fun generateMenuItems(context: Context) = listOf(
    MenuItem(
        index = 0,
        icon = Icons.Default.PlayArrow,
        title = context.getString(R.string.play_lists_str)
    ),
    MenuItem(
        index = 1,
        icon = Icons.Default.Favorite,
        title = context.getString(R.string.licked_channels_str)
    ),
    MenuItem(
        index = 2,
        icon = Icons.Default.Settings,
        title = context.getString(R.string.settings_str)
    ),
)