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
import com.pgc.stresspredict.ui.screen.LoginScreen
import com.pgc.stresspredict.ui.screen.RegistrationScreen
import com.pgc.stresspredict.ui.screen.ProfileScreen
import com.pgc.stresspredict.ui.component.navigation.BottomNavigationBar
import com.pgc.stresspredict.ui.screen.HistoryScreen
import com.pgc.stresspredict.ui.screen.MainScreen
import com.pgc.stresspredict.ui.screen.RecommendationsScreen
import com.pgc.stresspredict.ui.screen.SurveyScreen
import com.pgc.stresspredict.ui.screen.ResultsScreen
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
                                Screen.Survey -> SurveyScreen(
                                    currentScreen = currentScreen,
                                    onNavigate = { newScreen -> currentScreen = newScreen },
                                    onNavigateBack = { currentScreen = Screen.Main },
                                    onSubmitSurvey = {
                                        // Lógica para enviar la encuesta
                                    }
                                )
                                Screen.History -> HistoryScreen(
                                    currentScreen = currentScreen,
                                    onNavigate = { newScreen -> currentScreen = newScreen },
                                    onNavigateBack = { currentScreen = Screen.Main }
                                )
                                Screen.Main -> MainScreen(
                                    currentScreen = currentScreen,
                                    onNavigate = { newScreen -> currentScreen = newScreen },
                                    onNavigateBack = { currentScreen = Screen.Profile }
                                )
                                Screen.Recommendations -> RecommendationsScreen(
                                    currentScreen = currentScreen,
                                    onNavigate = { newScreen -> currentScreen = newScreen },
                                    onNavigateBack = { currentScreen = Screen.Main }
                                )
                                Screen.Profile -> ProfileScreen(
                                    currentScreen = currentScreen, // Pasamos la pantalla actual
                                    onNavigate = { newScreen -> currentScreen = newScreen }, // Manejo de navegación
                                    onNavigateBack = { currentScreen = Screen.Main }, // O la pantalla que corresponda
                                    onEditProfile = {
                                        // Lógica para editar perfil
                                    }
                                )
                                else -> Unit
                            }

                        }
                    }
                } else {
                    // Pantallas SIN barra
                    when (currentScreen) {
                        Screen.Login -> LoginScreen(
                            onNavigateToRegister = { currentScreen = Screen.Register },
                            onLoginSuccess = { currentScreen = Screen.Main } // Redirige a Principal
                        )
                        Screen.Register -> RegistrationScreen(
                            onRegisterSuccess = { currentScreen = Screen.Profile },
                            onNavigateToLogin = { currentScreen = Screen.Login }
                        )
                        Screen.Results -> ResultsScreen(
                            onNavigateBack = { currentScreen = Screen.Survey },
                            onNavigateToHome = { currentScreen = Screen.Main },
                            stressLevel = "Bajo",
                            stressImproved = true
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
    data object Results : Screen(0, "")
}