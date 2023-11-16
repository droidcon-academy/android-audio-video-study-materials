package com.droidcon.videomate.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.graphics.toColorInt

private val DarkColorScheme = darkColorScheme(
    primary = Color("#78E0C4".toColorInt()),
    onPrimary = Color("#3A3A3A".toColorInt()),
    background = Color("#FEFEFE".toColorInt()),
    onBackground = Color("#2B2B2B".toColorInt()),
    surface = Color("#E6E6E6".toColorInt()),
    surfaceVariant = Color("#2B2B2B".toColorInt()),
    onSurfaceVariant = Color("#FFFFFF".toColorInt()),
    onSurface = Color("#FFFFFF".toColorInt()),
)

@Composable
fun VideoMateTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )

}