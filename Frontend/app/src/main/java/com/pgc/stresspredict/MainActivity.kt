package com.pgc.stresspredict

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.pgc.stresspredict.ui.LoginScreen
import com.pgc.stresspredict.ui.RegistrationScreen
import com.pgc.stresspredict.ui.ProfileScreen
import com.pgc.stresspredict.ui.BottomNavigationBar
import com.pgc.stresspredict.ui.HistoryScreen
import com.pgc.stresspredict.ui.MainScreen
import com.pgc.stresspredict.ui.RecommendationsScreen
import com.pgc.stresspredict.ui.SurveyScreen
import com.pgc.stresspredict.ui.theme.StressPredictTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StressPredictTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }

                // Pantallas CON barra
                val screensWithNavBar = listOf(
                    Screen.Survey,
                    Screen.History,
                    Screen.Main,
                    Screen.Recommendations,
                    Screen.Profile
                )

                if (currentScreen in screensWithNavBar) {
                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(
                                currentScreen = currentScreen,
                                onItemClick = { screen -> currentScreen = screen }
                            )
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            when (currentScreen) {
                                Screen.Survey -> SurveyScreen()
                                Screen.History -> HistoryScreen()
                                Screen.Main -> MainScreen()
                                Screen.Recommendations -> RecommendationsScreen()
                                Screen.Profile -> ProfileScreen(
                                    onNavigateBack = { currentScreen = Screen.Login },
                                    onEditProfile = {
                                        // Lógica para editar perfil
                                    }
                                )
                                else -> Unit
                            }
                        }
                    }
                } else {
                    // Pantallas SIN barra (Login/Register)
                    when (currentScreen) {
                        Screen.Login -> LoginScreen(
                            onNavigateToRegister = { currentScreen = Screen.Register },
                            onLoginSuccess = { currentScreen = Screen.Main } // Redirige a Principal
                        )
                        Screen.Register -> RegistrationScreen(
                            onRegisterSuccess = { currentScreen = Screen.Profile },
                            onNavigateToLogin = { currentScreen = Screen.Login }
                        )
                        else -> Unit
                    }
                }
            }
        }
    }

}
// Enumeración para manejar las pantallas
sealed class Screen(val iconRes: Int, val label: String) {
    data object Survey : Screen(R.drawable.ic_survey, "Encuesta")
    data object History : Screen(R.drawable.ic_history, "Historial")
    data object Main : Screen(R.drawable.ic_home, "Principal")
    data object Recommendations : Screen(R.drawable.ic_recommendations, "Recomen..")
    data object Profile : Screen(R.drawable.ic_profile, "Perfil")

    // Pantallas sin barra
    data object Login : Screen(0, "")
    data object Register : Screen(0, "")
}