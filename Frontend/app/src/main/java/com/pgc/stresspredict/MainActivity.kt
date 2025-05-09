package com.pgc.stresspredict

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.pgc.stresspredict.ui.LoginScreen
import com.pgc.stresspredict.ui.RegistrationScreen
import com.pgc.stresspredict.ui.theme.StressPredictTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StressPredictTheme {
                // Estado para controlar qué pantalla mostrar
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }

                // Selector de pantalla basado en el estado
                when (currentScreen) {
                    Screen.Login -> LoginScreen(
                        onNavigateToRegister = { currentScreen = Screen.Register },
                        onLoginSuccess = { /* Lógica después de login exitoso */ }
                    )
                    Screen.Register -> RegistrationScreen(
                        onRegisterSuccess = { /* Lógica después de registro exitoso */ }
                    )
                }
            }
        }
    }
}

// Enumeración para manejar las pantallas
sealed class Screen {
    data object Login : Screen()
    data object Register : Screen()
}