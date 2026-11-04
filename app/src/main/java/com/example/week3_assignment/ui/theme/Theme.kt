package com.example.week3_assignment.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF0A84FF),
    primaryContainer = Color(0xFF003B73),
    onPrimaryContainer = Color(0xFFDCEBFF),
    secondary = Color(0xFF64D2FF),
    background = IosGroupedBgDark,
    surface = IosCardSurfaceDark,
    onBackground = Color(0xFFF2F2F7),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF2C2C2E),
    onSurfaceVariant = Color(0xFFAEAEB2),
    outline = Color(0xFF636366),
    outlineVariant = Color(0xFF3A3A3C)
)

private val LightColorScheme = lightColorScheme(
    primary = IosPrimaryBlue,
    primaryContainer = IosPrimaryBlueContainer,
    onPrimaryContainer = IosOnPrimaryBlueContainer,
    secondary = Color(0xFF475569),
    background = IosGroupedBgLight,
    surface = IosCardSurfaceLight,
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFF94A3B8),
    outlineVariant = Color(0xFFCBD5E1)
)

@Composable
fun Week3_assignmentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
