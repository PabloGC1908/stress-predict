package com.pgc.stresspredict.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pgc.stresspredict.ui.theme.StressPredictTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    stressLevel: String = "Bajo",
    stressImproved: Boolean = true,
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(true) }
    var showResults by remember { mutableStateOf(false) }

    // Animación para la aparición progresiva
    val resultsAlpha by animateFloatAsState(
        targetValue = if (showResults) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "resultsAlpha"
    )

    // Simulamos el cálculo del estrés
    LaunchedEffect(Unit) {
        delay(2000) // Tiempo de "cálculo"
        isLoading = false
        delay(300) // Pequeño retraso antes de mostrar resultados
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
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                LoadingView()
            } else {
                ResultsContentView(
                    stressLevel = stressLevel,
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
                    "Bajo" -> Color(0xFF2E7D32) // Verde
                    "Medio" -> Color(0xFFF9A825) // Amarillo
                    "Alto" -> Color(0xFFC62828) // Rojo
                    else -> Color(0xFF0D47A1) // Azul por defecto
                }
            )

            if (stressImproved) {
                Text(
                    text = "¡Tu nivel de estrés ha disminuido! Felicidades, mantente así.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF2E7D32)
                )
            } else {
                Text(
                    text = "Considera realizar más actividades relajantes.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF0D47A1)
                )
            }
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

@Preview(showBackground = true)
@Composable
fun ResultsScreenLoadingPreview() {
    StressPredictTheme {
        ResultsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ResultsScreenLowStressPreview() {
    StressPredictTheme {
        ResultsScreen(
            stressLevel = "Bajo",
            stressImproved = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ResultsScreenHighStressPreview() {
    StressPredictTheme {
        ResultsScreen(
            stressLevel = "Alto",
            stressImproved = false
        )
    }
}