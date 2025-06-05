package com.pgc.stresspredict.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pgc.stresspredict.Screen
import com.pgc.stresspredict.ui.theme.StressPredictTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsScreen(
    currentScreen: Screen = Screen.Recommendations,
    onNavigate: (Screen) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Recomendaciones") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                RecommendationCard(
                    title = "Gestión del Sueño",
                    description = "Dormir 7-8 horas mejora tu resistencia al estrés.",
                    icons = listOf(Icons.Default.Favorite, Icons.Default.Info, Icons.Default.Star),
                    color = Color(0xFFE1F5FE)
                )
            }

            item {
                RecommendationCard(
                    title = "Técnicas de Relajación",
                    description = "Practica respiración profunda regularmente.",
                    icons = listOf(Icons.Default.Edit, Icons.Default.Refresh, Icons.Default.List),
                    color = Color(0xFFE8F5E9)
                )
            }

            item {
                RecommendationCard(
                    title = "Actividad Física",
                    description = "30 minutos de ejercicio diario reducen el cortisol.",
                    icons = listOf(Icons.Default.Home, Icons.Default.Search, Icons.Default.Settings),
                    color = Color(0xFFFFF8E1)
                )
            }
        }
    }
}

@Composable
fun RecommendationCard(
    title: String,
    description: String,
    icons: List<ImageVector>,
    color: Color
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color,
            contentColor = Color.Black
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = description,
                fontSize = 14.sp
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                icons.forEach { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecommendationsScreenPreview() {
    StressPredictTheme {
        RecommendationsScreen(
            currentScreen = Screen.Recommendations,
            onNavigate = {},
            onNavigateBack = {}
        )
    }
}