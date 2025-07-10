package com.pgc.stresspredict

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.pgc.stresspredict.ui.component.navigation.BottomNavigationBar
import com.pgc.stresspredict.ui.screen.EditProfileScreen
import com.pgc.stresspredict.ui.screen.HistoryScreen
import com.pgc.stresspredict.ui.screen.LoginScreen
import com.pgc.stresspredict.ui.screen.MainScreen
import com.pgc.stresspredict.ui.screen.ProfileScreen
import com.pgc.stresspredict.ui.screen.RecommendationsScreen
import com.pgc.stresspredict.ui.screen.RegistrationScreen
import com.pgc.stresspredict.ui.screen.ResultsScreen
import com.pgc.stresspredict.ui.screen.SurveyScreen
import com.pgc.stresspredict.ui.theme.StressPredictTheme
import com.pgc.stresspredict.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent()  // Cambiado de StressPredictApp() a AppContent()
        }
    }
}

@Composable
fun AppContent() {  // Renombrado para evitar conflicto con la clase Application
    StressPredictTheme {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }
        val screensWithNavBar = listOf(
            Screen.Survey,
            Screen.History,
            Screen.Main,
            Screen.Recommendations,
            Screen.Profile
        )

        // Auto-login check
        val authViewModel: AuthViewModel = hiltViewModel()
        LaunchedEffect(Unit) {
            if (authViewModel.isLoggedIn()) {
                currentScreen = Screen.Main
            }
        }

        if (currentScreen in screensWithNavBar) {
            AppScaffold(
                currentScreen = currentScreen,
                onScreenChange = { currentScreen = it }
            )
        } else {
            AuthScreens(
                currentScreen = currentScreen,
                onScreenChange = { currentScreen = it }
            )
        }
    }
}

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
                    onNavigateBack = { onScreenChange(Screen.Main) }
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
                    onEditProfile = { onScreenChange(Screen.EditProfile) },
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
            onNavigateToHome = { onScreenChange(Screen.Main) }
        )
        Screen.EditProfile -> EditProfileScreen(
            onNavigateBack = { onScreenChange(Screen.Profile) }
        )
        else -> Unit
    }
}

sealed class Screen(val iconRes: Int, val label: String) {
    object Survey : Screen(R.drawable.ic_survey, "Encuesta")
    object History : Screen(R.drawable.ic_history, "Historial")
    object Main : Screen(R.drawable.ic_home, "Principal")
    object Recommendations : Screen(R.drawable.ic_recommendations, "Recomendaciones")
    object Profile : Screen(R.drawable.ic_profile, "Perfil")

    object Login : Screen(0, "")
    object Register : Screen(0, "")
    object Results : Screen(0, "")
    object EditProfile : Screen(0, "")
}

@Preview(showBackground = true)
@Composable
fun PreviewMainActivity() {
    StressPredictTheme {
        AppContent()  // Actualizado para usar el nuevo nombre
    }
}