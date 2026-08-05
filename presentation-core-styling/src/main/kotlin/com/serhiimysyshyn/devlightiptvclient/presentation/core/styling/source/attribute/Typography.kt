package com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.attribute

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.ThemeTypography

internal val attributeTypography = ThemeTypography(
    h1 = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
    h2 = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
    title = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
    body = TextStyle(fontSize = 16.sp),
    bodySmall = TextStyle(fontSize = 14.sp),
    caption = TextStyle(fontSize = 12.sp),
)
