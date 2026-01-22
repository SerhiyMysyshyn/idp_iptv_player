package com.serhiimysyshyn.devlightiptvclient.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class AppColors(
    val background: Color,
    val onBackground: Color,
    val backgroundSecondary: Color,
    val primary: Color,
    val onPrimary: Color,
    val surface: Color,
    val onSurface: Color
)

val LightColors = AppColors(
    background = Color(0xFFEBEDF1),
    onBackground = Color(0xFF00B0A7),
    backgroundSecondary = Color(0xFFD7D8DC),
    primary = Color(0xFF000000),
    onPrimary = Color.White,
    surface = Color(0xFFBA3518),
    onSurface = Color(0xFF9F2E15)
)

val DarkColors = AppColors(
    background = Color(0xFF054B5D),
    onBackground = Color(0xFF232C47),
    backgroundSecondary = Color(0xFF06556C),
    primary = Color(0xFFFFFFFF),
    onPrimary = Color.Black,
    surface = Color(0xFFBA3518),
    onSurface = Color(0xFF9F2E15)
)

val LocalAppColors = staticCompositionLocalOf { LightColors }

data class AppTypography(
    val h1: TextStyle,
    val h2: TextStyle,
    val body: TextStyle,
    val caption: TextStyle,
)

val BaseTypography = AppTypography(
    h1 = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
    h2 = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
    body = TextStyle(fontSize = 16.sp),
    caption = TextStyle(fontSize = 12.sp, color = Color.Gray)
)

val LocalAppTypography = staticCompositionLocalOf { BaseTypography }

@Composable
fun IPTVClientTheme(
    appTheme: AppThemeType = AppThemeType.SYSTEM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (appTheme) {
        AppThemeType.LIGHT -> false
        AppThemeType.DARK -> true
        AppThemeType.SYSTEM -> isSystemInDarkTheme()
    }

    val colors = if (darkTheme) DarkColors else LightColors
    val typography = BaseTypography

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypography provides typography
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
        ) {
            content()
        }
    }
}

object IPTVClientTheme {
    val colors: AppColors
        @Composable get() = LocalAppColors.current

    val typography: AppTypography
        @Composable get() = LocalAppTypography.current
}
