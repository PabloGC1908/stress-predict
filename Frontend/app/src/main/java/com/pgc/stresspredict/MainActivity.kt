package com.pgc.stresspredict

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pgc.stresspredict.ui.component.navigation.BottomNavigationBar
import com.pgc.stresspredict.ui.screen.*
import com.pgc.stresspredict.ui.theme.StressPredictTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StressPredictApp()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StressPredictApp() {
    StressPredictTheme {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }
        val screensWithNavBar = listOf(
            Screen.Survey,
            Screen.History,
            Screen.Main,
            Screen.Recommendations,
            Screen.Profile
        )

        if (currentScreen in screensWithNavBar) {
            AppScaffold(currentScreen = currentScreen, onScreenChange = { currentScreen = it })
        } else {
            AuthScreens(currentScreen = currentScreen, onScreenChange = { currentScreen = it })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppScaffold(
    currentScreen: Screen,
    onScreenChange: (Screen) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentScreen = currentScreen,
                onItemClick = { screen -> onScreenChange(screen) }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                Screen.Survey -> SurveyScreen(
                    onNavigate = { newScreen -> onScreenChange(newScreen) },
                    onNavigateBack = { onScreenChange(Screen.Main) },
                    onSubmitSurvey = { /* Lógica para enviar encuesta */ }
                )
                Screen.History -> HistoryScreen(
                    onNavigateBack = { onScreenChange(Screen.Main) }
                )
                Screen.Main -> MainScreen(
                    onNavigateBack = { onScreenChange(Screen.Profile) }
                )
                Screen.Recommendations -> RecommendationsScreen(
                    onNavigateBack = { onScreenChange(Screen.Main) }
                )
                Screen.Profile -> ProfileScreen(
                    onNavigateBack = { onScreenChange(Screen.Main) },
                    onEditProfile = { onScreenChange(Screen.EditProfile) }, // Nueva pantalla
                    onLogout = { onScreenChange(Screen.Login) }
                )
                else -> Unit
            }
        }
    }
}

@Composable
fun AuthScreens(
    currentScreen: Screen,
    onScreenChange: (Screen) -> Unit
) {
    when (currentScreen) {
        Screen.Login -> LoginScreen(
            onNavigateToRegister = { onScreenChange(Screen.Register) },
            onLoginSuccess = { onScreenChange(Screen.Main) }
        )
        Screen.Register -> RegistrationScreen(
            onRegisterSuccess = { onScreenChange(Screen.Profile) },
            onNavigateToLogin = { onScreenChange(Screen.Login) }
        )
        Screen.Results -> ResultsScreen(
            onNavigateBack = { onScreenChange(Screen.Survey) },
            onNavigateToHome = { onScreenChange(Screen.Main) },
            stressLevel = "Bajo",
            stressImproved = true
        )
        Screen.EditProfile -> EditProfileScreen(
            onNavigateBack = { onScreenChange(Screen.Profile) },
            viewModel = TODO()
        )
        else -> Unit
    }
}

sealed class Screen(val iconRes: Int, val label: String) {
    data object Survey : Screen(R.drawable.ic_survey, "Encuesta")
    data object History : Screen(R.drawable.ic_history, "Historial")
    data object Main : Screen(R.drawable.ic_home, "Principal")
    data object Recommendations : Screen(R.drawable.ic_recommendations, "Recomendaciones")
    data object Profile : Screen(R.drawable.ic_profile, "Perfil")

    // Pantallas sin barra de navegación
    data object Login : Screen(0, "")
    data object Register : Screen(0, "")
    data object Results : Screen(0, "")
    data object EditProfile : Screen(0, "")
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewMainActivity() {
    StressPredictTheme {
        StressPredictApp()
    }
}