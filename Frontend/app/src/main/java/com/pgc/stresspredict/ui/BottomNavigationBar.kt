package com.pgc.stresspredict.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pgc.stresspredict.Screen
import com.pgc.stresspredict.ui.theme.StressPredictTheme

@Composable
fun BottomNavigationBar(
    currentScreen: Screen,
    onItemClick: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFF4CAF50), // Color verde de fondo
        tonalElevation = 8.dp, // Sombra opcional
        contentColor = Color.Black// Color de íconos y texto (blanco para contraste)
    ) {
        // Lista de todos los items de navegación
        val navItems = listOf(
            Screen.Survey,
            Screen.History,
            Screen.Main,
            Screen.Recommendations,
            Screen.Profile
        )

        navItems.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = screen.iconRes),
                        contentDescription = screen.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(screen.label) },
                selected = currentScreen == screen,
                onClick = { onItemClick(screen) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    }
}

// Vista previa del componente
@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    StressPredictTheme {
        BottomNavigationBar(
            currentScreen = Screen.Main,
            onItemClick = {}
        )
    }
}

