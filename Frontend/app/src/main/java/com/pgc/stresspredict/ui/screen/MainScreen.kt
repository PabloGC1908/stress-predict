package com.pgc.stresspredict.ui.screen

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.pgc.stresspredict.R
import com.pgc.stresspredict.Screen
import com.pgc.stresspredict.ui.component.navigation.BottomNavigationBar
import com.pgc.stresspredict.ui.theme.StressPredictTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    currentScreen: Screen = Screen.Main,
    onNavigate: (Screen) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Hola, Ronald Díaz") },
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
            // Sección École con gráfica
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE3F2FD)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Gráfica de progreso
                        Image(
                            painter = painterResource(R.drawable.grafica1),
                            contentDescription = "Progreso en manejo de estrés",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tu progreso semanal en manejo de estrés",
                            fontSize = 14.sp,
                            color = Color(0xFF0D47A1)
                        )
                    }
                }
            }

            // Sección Blogs
            item {
                Text(
                    text = "Artículos Recomendados",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Lista de artículos con enlaces reales
            items(getStressManagementArticles()) { article ->
                BlogArticleCard(
                    article = article,
                    onOpenArticle = { url ->
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        ContextCompat.startActivity(context, intent, null)
                    }
                )
            }
        }
    }
}

data class BlogArticle(
    val title: String,
    val summary: String,
    val source: String,
    val url: String,
    val color: Color
)

@Composable
fun BlogArticleCard(
    article: BlogArticle,
    onOpenArticle: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = article.color.copy(alpha = 0.1f),
            contentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = article.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = article.color
            )

            Text(
                text = article.summary,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Fuente: ${article.source}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Button(
                    onClick = { onOpenArticle(article.url) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = article.color,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Spacer(Modifier.width(8.dp))
                    Text("Leer artículo")
                }
            }
        }
    }
}

fun getStressManagementArticles(): List<BlogArticle> {
    return listOf(
        BlogArticle(
            title = "Cómo manejar el estrés: Guía completa",
            summary = "La Clínica Mayo explica técnicas científicas para identificar y controlar los factores estresantes en tu vida diaria.",
            source = "Clínica Mayo",
            url = "https://www.mayoclinic.org/es/healthy-lifestyle/stress-management/basics/stress-basics/hlv-20049495",
            color = Color(0xFF2E7D32)
        ),
        BlogArticle(
            title = "Estrategias para reducir la ansiedad",
            summary = "La APA ofrece consejos prácticos basados en evidencia para manejar la ansiedad y el estrés en diferentes contextos.",
            source = "APA",
            url = "https://www.apa.org/topics/stress",
            color = Color(0xFF1565C0)
        )
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    StressPredictTheme {
        MainScreen(
            currentScreen = Screen.Main,
            onNavigate = {},
            onNavigateBack = {}
        )
    }
}