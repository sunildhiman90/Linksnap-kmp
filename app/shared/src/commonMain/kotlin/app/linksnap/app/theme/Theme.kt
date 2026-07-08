package app.linksnap.app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import app.linksnap.app.theme.LuminaError
import app.linksnap.app.theme.LuminaErrorContainer
import app.linksnap.app.theme.LuminaOnError
import app.linksnap.app.theme.LuminaOnErrorContainer
import app.linksnap.app.theme.LuminaOnPrimary
import app.linksnap.app.theme.LuminaOnPrimaryContainer
import app.linksnap.app.theme.LuminaOnSecondary
import app.linksnap.app.theme.LuminaOnSecondaryContainer
import app.linksnap.app.theme.LuminaOnSurface
import app.linksnap.app.theme.LuminaOnSurfaceVariant
import app.linksnap.app.theme.LuminaOnTertiary
import app.linksnap.app.theme.LuminaOnTertiaryContainer
import app.linksnap.app.theme.LuminaOutline
import app.linksnap.app.theme.LuminaOutlineVariant
import app.linksnap.app.theme.LuminaPrimary
import app.linksnap.app.theme.LuminaPrimaryContainer
import app.linksnap.app.theme.LuminaSecondary
import app.linksnap.app.theme.LuminaSecondaryContainer
import app.linksnap.app.theme.LuminaSurface
import app.linksnap.app.theme.LuminaSurfaceContainer
import app.linksnap.app.theme.LuminaSurfaceContainerHigh
import app.linksnap.app.theme.LuminaSurfaceContainerHighest
import app.linksnap.app.theme.LuminaSurfaceContainerLow
import app.linksnap.app.theme.LuminaSurfaceContainerLowest
import app.linksnap.app.theme.LuminaTertiary
import app.linksnap.app.theme.LuminaTertiaryContainer

private val LightColorScheme = lightColorScheme(
    primary = LuminaPrimary,
    onPrimary = LuminaOnPrimary,
    primaryContainer = LuminaPrimaryContainer,
    onPrimaryContainer = LuminaOnPrimaryContainer,
    secondary = LuminaSecondary,
    onSecondary = LuminaOnSecondary,
    secondaryContainer = LuminaSecondaryContainer,
    onSecondaryContainer = LuminaOnSecondaryContainer,
    tertiary = LuminaTertiary,
    onTertiary = LuminaOnTertiary,
    tertiaryContainer = LuminaTertiaryContainer,
    onTertiaryContainer = LuminaOnTertiaryContainer,
    error = LuminaError,
    onError = LuminaOnError,
    errorContainer = LuminaErrorContainer,
    onErrorContainer = LuminaOnErrorContainer,
    background = LuminaSurface,
    onBackground = LuminaOnSurface,
    surface = LuminaSurface,
    onSurface = LuminaOnSurface,
    surfaceVariant = LuminaSurfaceContainerLow,
    onSurfaceVariant = LuminaOnSurfaceVariant,
    outline = LuminaOutline,
    outlineVariant = LuminaOutlineVariant,
    
    surfaceContainerLowest = LuminaSurfaceContainerLowest,
    surfaceContainerLow = LuminaSurfaceContainerLow,
    surfaceContainer = LuminaSurfaceContainer,
    surfaceContainerHigh = LuminaSurfaceContainerHigh,
    surfaceContainerHighest = LuminaSurfaceContainerHighest
)

private val DarkColorScheme = darkColorScheme(
    primary = LuminaPrimaryContainer,
    onPrimary = LuminaOnPrimaryContainer,
    primaryContainer = LuminaPrimary,
    onPrimaryContainer = LuminaOnPrimary,
    secondary = LuminaSecondaryContainer,
    onSecondary = LuminaOnSecondaryContainer,
    tertiary = LuminaTertiaryContainer,
    onTertiary = LuminaOnTertiaryContainer,
    tertiaryContainer = LuminaTertiary,
    onTertiaryContainer = LuminaOnTertiary,
    error = LuminaErrorContainer,
    onError = LuminaOnErrorContainer,
    errorContainer = LuminaError,
    onErrorContainer = LuminaOnError,
    background = Color(0xFF141318),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF141318),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF25232A),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),
    
    surfaceContainerLowest = Color(0xFF0F0D13),
    surfaceContainerLow = Color(0xFF1D1B22),
    surfaceContainer = Color(0xFF211F26),
    surfaceContainerHigh = Color(0xFF2B2930),
    surfaceContainerHighest = Color(0xFF36343B)
)

@Composable
fun LinkSnapTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = LuminaTypography,
        content = content
    )
}
