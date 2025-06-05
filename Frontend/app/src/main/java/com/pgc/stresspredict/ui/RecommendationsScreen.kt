package com.pgc.stresspredict.ui
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview // ⬅️ Necesario para @Preview
import com.pgc.stresspredict.R
import com.pgc.stresspredict.ui.theme.StressPredictTheme // ⬅️ Asegúrate de tener este Theme

@Composable
fun RecommendationsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Recomendaciones",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp),
            fontWeight = FontWeight.Bold
        )

        RecommendationCard(
            title = "Dormir",
            description = "Asegúrate de dormir lo suficiente para reducir el riesgo de enfermedades como la diabetes, problemas del corazón y más.",
            icons = listOf(Icons.Default.Favorite, Icons.Default.Info, Icons.Default.Star)
        )

        RecommendationCard(
            title = "Meditación",
            description = "La meditación te ayuda a concentrarte. Toma una respiración profunda y repite los pasos para convertirla en un hábito.",
            icons = listOf(Icons.Default.Edit, Icons.Default.Refresh, Icons.Default.List)
        )

        RecommendationCard(
            title = "Nutrición",
            description = "Comer bien es clave para tu bienestar. Selecciona ingredientes saludables y anota tus progresos para notarlo a diario.",
            icons = listOf(Icons.Default.Home, Icons.Default.Search, Icons.Default.Settings)
        )


        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationBar(
            currentScreen = TODO(),
            onItemClick = TODO()
        )
    }
}

@Composable
fun RecommendationCard(title: String, description: String, icons: List<ImageVector>) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB9F6CA)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, fontSize = 14.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                icons.forEach { icon ->
                    Icon(icon, contentDescription = null, tint = Color.Black)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RecommendationScreenPreview() {
    StressPredictTheme {
        RecommendationsScreen()
    }
}