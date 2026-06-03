package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = CandyPrimary,
    onPrimary = Color.White,
    primaryContainer = CandyPrimaryContainer,
    onPrimaryContainer = CandyOnPrimaryContainer,
    secondary = CandySecondary,
    onSecondary = Color.White,
    secondaryContainer = CandySecondaryContainer,
    onSecondaryContainer = CandyOnSecondaryContainer,
    tertiary = CandyTertiary,
    onTertiary = Color.White,
    tertiaryContainer = CandyTertiaryContainer,
    onTertiaryContainer = CandyOnTertiaryContainer,
    background = CandyBackground,
    onBackground = CandyOnBackground,
    surface = CandySurface,
    onSurface = CandyOnSurface,
    surfaceVariant = CandySurfaceVariant,
    onSurfaceVariant = CandyOnSurfaceVariant,
    outline = CandyOutline,
    outlineVariant = CandyOutlineVariant,
    error = CandyError,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = CandyPrimary,
    onPrimary = Color.White,
    primaryContainer = CandyOnPrimaryContainer,
    onPrimaryContainer = CandyPrimaryContainer,
    secondary = CandySecondary,
    onSecondary = Color.White,
    secondaryContainer = CandyOnSecondaryContainer,
    onSecondaryContainer = CandySecondaryContainer,
    tertiary = CandyTertiary,
    onTertiary = Color.White,
    tertiaryContainer = CandyOnTertiaryContainer,
    onTertiaryContainer = CandyTertiaryContainer,
    background = CandyOnBackground,
    onBackground = CandyBackground,
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = CandyOutlineVariant,
    outlineVariant = CandyOutline,
    error = CandyError,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
