package com.pgc.stresspredict.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pgc.stresspredict.ui.theme.StressPredictTheme

// Modelo de datos para los resultados del historial
data class HistorialTest(
    val fecha: String,
    val nivelEstres: String
)

@Composable
fun HistoryScreen() {
    // Datos de ejemplo estáticos
    val historialTests = listOf(
        HistorialTest("25/05/2025", "Medio"),
        HistorialTest("20/05/2025", "Alto"),
        HistorialTest("15/05/2025", "Bajo"),
        HistorialTest("10/05/2025", "Medio"),
        HistorialTest("05/05/2025", "Alto"),
        HistorialTest("01/05/2025", "Bajo"),
        HistorialTest("28/04/2025", "Medio"),
        HistorialTest("25/04/2025", "Bajo"),
        HistorialTest("20/04/2025", "Alto"),
        HistorialTest("15/04/2025", "Medio")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Encabezado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Historial",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }

        // Lista de resultados
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp) // Espaciado mínimo entre tarjetas
        ) {
            items(historialTests) { test ->
                HistorialTestCard(test = test)
            }
            
            // Espaciado adicional al final para evitar que la última tarjeta quede pegada a la barra de navegación
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun HistorialTestCard(test: HistorialTest) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5) // Gris claro
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Fecha del test
            Text(
                text = test.fecha,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Resultado del test
            Text(
                text = "Resultados de este test: ${test.nivelEstres}",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f)
            )
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun HistoryScreenPreview() {
    StressPredictTheme {
        HistoryScreen()
    }
}

@Preview(showBackground = true, name = "Test Card Preview")
@Composable
fun HistorialTestCardPreview() {
    StressPredictTheme {
        HistorialTestCard(
            test = HistorialTest("25/05/2025", "Medio")
        )
    }
}
