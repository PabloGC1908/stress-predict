package com.pgc.stresspredict.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pgc.stresspredict.viewmodels.StressViewModel
import com.pgc.stresspredict.viewmodels.PredictionState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    viewModel: StressViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    val predictionState by viewModel.predictionState.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    var showResults by remember { mutableStateOf(false) }

    // Obtener datos de la predicción de forma segura
    val predictionResult = remember(predictionState) {
        when (predictionState) {
            is PredictionState.Success -> {
                val response = (predictionState as PredictionState.Success).data
                Pair(
                    response.nivelEstres,
                    response.mensaje.contains("disminuido", ignoreCase = true)
                )
            }
            else -> Pair("Bajo", true) // Valores por defecto
        }
    }

    val (stressLevel, stressImproved) = predictionResult

    // Animación para la aparición progresiva
    val resultsAlpha by animateFloatAsState(
        targetValue = if (showResults) 1f else 0f,
        animationSpec = tween(durationMillis = 800)
    )

    // Simulamos el cálculo del estrés
    LaunchedEffect(Unit) {
        delay(2000)
        isLoading = false
        delay(300)
        showResults = true
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Resultados de Estrés") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when {
                isLoading -> LoadingView()
                predictionState is PredictionState.Error -> {
                    val errorState = predictionState as PredictionState.Error
                    ErrorView(
                        errorMessage = errorState.message,
                        onNavigateToHome = onNavigateToHome
                    )
                }
                else -> ResultsContentView(
                    stressLevel = translateStressLevel(stressLevel),
                    stressImproved = stressImproved,
                    resultsAlpha = resultsAlpha,
                    onNavigateToHome = onNavigateToHome
                )
            }
        }
    }
}

@Composable
private fun LoadingView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = Color(0xFF0D47A1),
            strokeWidth = 6.dp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Calculando tus nuevos niveles de estrés...",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF0D47A1)
        )
    }
}

@Composable
private fun ErrorView(errorMessage: String, onNavigateToHome: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Error al calcular el estrés",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFC62828)
        )
        Text(
            text = errorMessage,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(24.dp))
        HomeButton(onNavigateToHome)
    }
}

@Composable
private fun ResultsContentView(
    stressLevel: String,
    stressImproved: Boolean,
    resultsAlpha: Float,
    onNavigateToHome: () -> Unit
) {
    Column(
        modifier = Modifier.alpha(resultsAlpha),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StressLevelCard(stressLevel, stressImproved)
        Spacer(modifier = Modifier.height(24.dp))
        HomeButton(onNavigateToHome)
    }
}

@Composable
private fun StressLevelCard(stressLevel: String, stressImproved: Boolean) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "¡Listo! Tu nivel de estrés es:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )

            Text(
                text = stressLevel,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = when (stressLevel) {
                    "Bajo" -> Color(0xFF2E7D32)
                    "Medio" -> Color(0xFFF9A825)
                    "Alto" -> Color(0xFFC62828)
                    else -> Color(0xFF0D47A1)
                }
            )

            Text(
                text = if (stressImproved) {
                    "¡Tu nivel de estrés ha disminuido! Felicidades, mantente así."
                } else {
                    "Considera realizar más actividades relajantes."
                },
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = if (stressImproved) Color(0xFF2E7D32) else Color(0xFF0D47A1)
            )
        }
    }
}

@Composable
private fun HomeButton(onNavigateToHome: () -> Unit) {
    Button(
        onClick = onNavigateToHome,
        modifier = Modifier.fillMaxWidth(0.7f),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0D47A1),
            contentColor = Color.White
        )
    ) {
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "Inicio",
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(ButtonDefaults.IconSpacing))
        Text("Volver al Inicio")
    }
}

private fun translateStressLevel(englishLevel: String): String {
    return when (englishLevel.lowercase()) {
        "low" -> "Bajo"
        "moderate" -> "Medio"
        "high" -> "Alto"
        else -> englishLevel
    }
}

@Preview(showBackground = true)
@Composable
fun ResultsScreenLoadingPreview() {
    ResultsScreen(onNavigateBack = {}, onNavigateToHome = {})
}

@Preview(showBackground = true)
@Composable
fun ResultsScreenLowStressPreview() {
    ResultsScreen(onNavigateBack = {}, onNavigateToHome = {})
}

@Preview(showBackground = true)
@Composable
fun ResultsScreenHighStressPreview() {
    ResultsScreen(onNavigateBack = {}, onNavigateToHome = {})
}