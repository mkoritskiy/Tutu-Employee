package ru.tutu.tutuemployee.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = TutuPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = TutuPrimaryLight,
    onPrimaryContainer = Color(0xFFFFFFFF),

    secondary = TutuSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = Color(0xFFFFDCC1),
    onSecondaryContainer = Color(0xFF2C1600),

    tertiary = TutuTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = Color(0xFFA7F3E3),
    onTertiaryContainer = Color(0xFF002019),

    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,

    background = LightBackground,
    onBackground = LightOnBackground,

    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color(0xFF49454F),

    outline = LightOutline,
    outlineVariant = LightOutlineVariant,

    scrim = Color(0xFF000000),

    inverseSurface = Color(0xFF313033),
    inverseOnSurface = Color(0xFFF4EFF4),
    inversePrimary = Color(0xFF90CAF9)
)

private val DarkColorScheme = darkColorScheme(
    primary = TutuPrimaryLight,
    onPrimary = DarkOnPrimary,
    primaryContainer = TutuPrimaryDark,
    onPrimaryContainer = Color(0xFFD0E4FF),

    secondary = TutuSecondaryLight,
    onSecondary = DarkOnSecondary,
    secondaryContainer = TutuSecondaryDark,
    onSecondaryContainer = Color(0xFFFFDCC1),

    tertiary = TutuTertiaryLight,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = TutuTertiaryDark,
    onTertiaryContainer = Color(0xFFA7F3E3),

    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,

    background = DarkBackground,
    onBackground = DarkOnBackground,

    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFCAC4D0),

    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,

    scrim = Color(0xFF000000),

    inverseSurface = Color(0xFFE6E1E5),
    inverseOnSurface = Color(0xFF313033),
    inversePrimary = TutuPrimary
)

@Composable
fun TutuEmployeeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TutuTypography,
        shapes = TutuShapes,
        content = content
    )
}
