package cl.duoc.reservasem.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val LocalModoOscuroTactico = staticCompositionLocalOf { false }

private val LightColorScheme = lightColorScheme(
    primary = OliveTacticalPrimary,
    onPrimary = Color(0xFFF6F6F2),
    primaryContainer = ForestMilitaryContainer,
    onPrimaryContainer = Color(0xFFF6F6F2),
    secondary = SandTacticalSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE5DFCC),
    onSecondaryContainer = Color(0xFF1F1B10),
    tertiary = MilitaryGoldGalon,
    onTertiary = Color(0xFF1B1607),
    tertiaryContainer = Color(0xFFF4E7C3),
    onTertiaryContainer = Color(0xFF261E08),
    background = TacticalBoneBackground,
    onBackground = Color(0xFF141814),
    surface = TacticalBoneSurface,
    onSurface = Color(0xFF141814),
    surfaceVariant = TacticalBoneVariant,
    onSurfaceVariant = Color(0xFF313830),
    outline = Color(0xFF687066),
    error = BadgeRechazadaOxido,
    onError = Color.White,
    errorContainer = MantencionContainerLight,
    onErrorContainer = Color(0xFF410E0B)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9AC498),
    onPrimary = Color(0xFF102110),
    primaryContainer = ForestMilitaryContainer,
    onPrimaryContainer = Color(0xFFE3F3E2),
    secondary = KhakiTacticalLight,
    onSecondary = Color(0xFF231E12),
    secondaryContainer = Color(0xFF3E382A),
    onSecondaryContainer = Color(0xFFEBE4D3),
    tertiary = MilitaryGoldBright,
    onTertiary = Color(0xFF241C04),
    tertiaryContainer = Color(0xFF594922),
    onTertiaryContainer = Color(0xFFF9ECC8),
    background = TacticalGraphiteBackground,
    onBackground = Color(0xFFECEFE9),
    surface = TacticalAsphaltSurface,
    onSurface = Color(0xFFECEFE9),
    surfaceVariant = TacticalAsphaltVariant,
    onSurfaceVariant = Color(0xFFD0D7CE),
    outline = Color(0xFF8D968B),
    error = Color(0xFFFF897D),
    onError = Color(0xFF3B0906),
    errorContainer = MantencionContainerDark,
    onErrorContainer = Color(0xFFFFDAD6)
)

@Composable
fun tacticalCommandBackgroundBrush(): Brush {
    val isDark = LocalModoOscuroTactico.current
    return if (isDark) {
        Brush.verticalGradient(
            colors = listOf(
                CommandDarkGradientTop,
                CommandDarkGradientMid,
                CommandDarkGradientBottom
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                CommandLightGradientTop,
                CommandLightGradientMid,
                CommandLightGradientBottom
            )
        )
    }
}

@Composable
fun EscuelaMilitarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    CompositionLocalProvider(LocalModoOscuroTactico provides darkTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}
