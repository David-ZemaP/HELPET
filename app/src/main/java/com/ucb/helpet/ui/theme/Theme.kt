package com.ucb.helpet.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = VioletButton,          // Botones principales
    onPrimary = White,
    primaryContainer = VioletIconBackground,
    onPrimaryContainer = VioletPrimary,

    background = DarkBackground,     // Fondo de pantalla general
    onBackground = TextWhite,

    surface = CardBackground,        // Fondo de tarjetas
    onSurface = TextWhite,

    surfaceVariant = InputBackground, // Fondo de inputs (opcional, para componentes variant)
    onSurfaceVariant = TextGray,

    error = RedError,
    onError = White,

    outline = BorderColor,
    outlineVariant = InputBackground
)

// Por ahora, forzaremos el esquema oscuro para que coincida con tu diseño de Figma,
// o puedes mantener un esquema claro separado si lo deseas.
// Aquí configuro el LightColorScheme igual que el Dark para mantener consistencia visual inmediata.
private val LightColorScheme = DarkColorScheme

@Composable
fun HelpetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Si quieres soportar modo claro real en el futuro, cambia esta lógica.
    // Por ahora, priorizamos tu diseño oscuro.
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}