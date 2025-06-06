package com.pgc.stresspredict.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pgc.stresspredict.Screen
import com.pgc.stresspredict.ui.component.navigation.BottomNavigationBar
import com.pgc.stresspredict.ui.theme.StressPredictTheme

// Modelo de datos para los resultados del historial
data class HistorialTest(
    val fecha: String,
    val nivelEstres: String,
    val detalles: String = "Resultados detallados del test de estrés"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    currentScreen: Screen = Screen.History,
    onNavigate: (Screen) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    // Datos de ejemplo
    val historialTests = listOf(
        HistorialTest("25/05/2025", "Medio", "Nivel de estrés dentro del rango normal"),
        HistorialTest("20/05/2025", "Alto", "Se recomienda realizar técnicas de relajación"),
        HistorialTest("15/05/2025", "Bajo", "Buen manejo del estrés"),
        HistorialTest("10/05/2025", "Medio", "Algunos indicadores elevados"),
        HistorialTest("05/05/2025", "Alto", "Niveles preocupantes de estrés")
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Historial de Tests") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentScreen = currentScreen,
                onItemClick = { screen ->
                    if (screen != currentScreen) {
                        onNavigate(screen)
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(historialTests) { test ->
                HistorialTestCard(test = test)
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun HistorialTestCard(test: HistorialTest) {
    val colorFondo = when(test.nivelEstres) {
        "Alto" -> Color(0xFFFFEBEE) // Rojo claro
        "Medio" -> Color(0xFFFFF8E1) // Amarillo claro
        else -> Color(0xFFE8F5E9) // Verde claro
    }

    val colorTextoNivel = when(test.nivelEstres) {
        "Alto" -> Color(0xFFD32F2F) // Rojo
        "Medio" -> Color(0xFFF57C00) // Naranja
        else -> Color(0xFF388E3C) // Verde
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorFondo,
            contentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Fila superior con fecha y nivel
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = test.fecha,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = test.nivelEstres.uppercase(),
                    color = colorTextoNivel,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Detalles
            Text(
                text = test.detalles,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    StressPredictTheme {
        HistoryScreen(
            currentScreen = Screen.History,
            onNavigate = {},
            onNavigateBack = {}
        )
    }
}

