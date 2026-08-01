package com.example.habittracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.habittracker.R

// ---- Notion tokens ----
val Ink = Color(0xFF37352F)
val InkLight = Color(0xCC37352F)      // ~0.80
val InkFaint = Color(0x7337352F)      // ~0.45
val Canvas = Color(0xFFFFFFFF)
val Hover = Color(0x0F37352F)         // ~0.06
val HoverStrong = Color(0x1737352F)   // ~0.09
val BorderCol = Color(0x1737352F)
val BorderStrong = Color(0x2937352F)

val Satoshi = FontFamily(
    Font(R.font.satoshi_light, FontWeight.Light),
    Font(R.font.satoshi_regular, FontWeight.Normal),
    Font(R.font.satoshi_medium, FontWeight.Medium),
    Font(R.font.satoshi_bold, FontWeight.Bold)
)

private val AppTypography = Typography().run {
    val base = this
    copy(
        displayLarge = base.displayLarge.satoshi(),
        displayMedium = base.displayMedium.satoshi(),
        displaySmall = base.displaySmall.satoshi(),
        headlineLarge = base.headlineLarge.satoshi(),
        headlineMedium = base.headlineMedium.satoshi(),
        headlineSmall = base.headlineSmall.satoshi(),
        titleLarge = base.titleLarge.satoshi(),
        titleMedium = base.titleMedium.satoshi(),
        titleSmall = base.titleSmall.satoshi(),
        bodyLarge = base.bodyLarge.satoshi(),
        bodyMedium = base.bodyMedium.satoshi(),
        bodySmall = base.bodySmall.satoshi(),
        labelLarge = base.labelLarge.satoshi(),
        labelMedium = base.labelMedium.satoshi(),
        labelSmall = base.labelSmall.satoshi()
    )
}

private fun TextStyle.satoshi() = copy(fontFamily = Satoshi)

private val ColorScheme = lightColorScheme(
    primary = Ink,
    onPrimary = Color.White,
    background = Canvas,
    onBackground = Ink,
    surface = Canvas,
    onSurface = Ink,
    surfaceVariant = Hover,
    onSurfaceVariant = InkLight,
    outline = BorderStrong,
    outlineVariant = BorderCol
)

@Composable
fun HabitTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = AppTypography,
        content = content
    )
}
