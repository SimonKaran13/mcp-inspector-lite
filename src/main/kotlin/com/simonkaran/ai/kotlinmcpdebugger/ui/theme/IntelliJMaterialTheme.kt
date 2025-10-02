package com.simonkaran.ai.kotlinmcpdebugger.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.intellij.ui.JBColor
import com.intellij.util.ui.UIUtil
import java.awt.Color as AWTColor


fun AWTColor.toComposeColor(): Color {
    return Color(red, green, blue, alpha)
}

@Composable
fun createIntelliJColorScheme(): ColorScheme {
    val isLightMode = JBColor.isBright()
    
    return if (isLightMode) {
        val lightBackground = UIUtil.getPanelBackground().toComposeColor()
        val intellijForeground = UIUtil.getLabelForeground()
        val lightForeground = if (intellijForeground.red < 100 && intellijForeground.green < 100 && intellijForeground.blue < 100) {
            intellijForeground.toComposeColor()
        } else {
            Color(0xFF1A1A1A)
        }
        
        lightColorScheme(
            primary = Color(0xFF2470B3),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFD0E2F3),
            onPrimaryContainer = Color(0xFF001D36),
            secondary = Color(0xFF5355CA),
            onSecondary = Color.White,
            background = lightBackground,
            onBackground = lightForeground,
            surface = lightBackground,
            onSurface = lightForeground,
            surfaceVariant = Color(0xFFF5F5F5),
            onSurfaceVariant = lightForeground,
            error = Color(0xFFE53935),
            onError = Color.White,
            errorContainer = Color(0xFFFFEBEE),
            onErrorContainer = Color(0xFFC62828),
            outline = Color(0xFFBDBDBD),
            outlineVariant = Color(0xFFE0E0E0),
        )
    } else {
        val darkBackground = UIUtil.getPanelBackground().toComposeColor()
        val darkForeground = UIUtil.getLabelForeground().toComposeColor()
        
        darkColorScheme(
            primary = Color(0xFF589DF6),
            onPrimary = Color(0xFF003258),
            primaryContainer = Color(0xFF004A77),
            onPrimaryContainer = Color(0xFFD0E9FF),
            secondary = Color(0xFF7A7AFF),
            onSecondary = Color.White,
            background = darkBackground,
            onBackground = darkForeground,
            surface = darkBackground,
            onSurface = darkForeground,
            surfaceVariant = Color(0xFF313335),
            onSurfaceVariant = darkForeground,
            error = Color(0xFFFF6B68),
            onError = Color(0xFF690005),
            errorContainer = Color(0xFF5A1F1F),
            onErrorContainer = Color(0xFFFFDAD6),
            outline = Color(0xFF5A5D61),
            outlineVariant = Color(0xFF43474E),
        )
    }
}

@Composable
fun IntelliJMaterialTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = createIntelliJColorScheme()
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography()
    ) {
        CompositionLocalProvider(
            LocalContentColor provides colorScheme.onBackground
        ) {
            content()
        }
    }
}

