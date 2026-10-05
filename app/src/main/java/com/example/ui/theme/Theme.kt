package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = RailPrimaryDark,
    onPrimary = RailOnPrimaryDark,
    primaryContainer = RailPrimaryContainerDark,
    onPrimaryContainer = RailOnPrimaryContainerDark,
    secondary = RailSecondaryDark,
    onSecondary = Color(0xFF003830),
    tertiary = PurpleTech,
    surface = RailSurfaceDark,
    onSurface = TextLight,
    surfaceVariant = SlateNavy,
    onSurfaceVariant = TextMuted,
    background = RailBackgroundDark,
    onBackground = TextLight,
    outline = BorderSlate
  )

private val LightColorScheme =
  lightColorScheme(
    primary = RailPrimaryLight,
    onPrimary = RailOnPrimaryLight,
    primaryContainer = RailPrimaryContainerLight,
    onPrimaryContainer = RailOnPrimaryContainerLight,
    secondary = RailSecondaryLight,
    onSecondary = Color.White,
    tertiary = PurpleTech,
    surface = RailSurfaceLight,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),
    background = RailBackgroundLight,
    onBackground = Color(0xFF0F172A),
    outline = Color(0xFFCBD5E1)
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our designed branded colors
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
