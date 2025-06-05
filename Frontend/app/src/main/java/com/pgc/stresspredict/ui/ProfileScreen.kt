@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package com.pgc.stresspredict.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pgc.stresspredict.R
import com.pgc.stresspredict.Screen
import com.pgc.stresspredict.ui.theme.StressPredictTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    currentScreen: Screen = Screen.Profile, // Nuevo parámetro
    onNavigate: (Screen) -> Unit = {},     // Para la barra de navegación
    onNavigateBack: () -> Unit,           // Para el botón de retroceso
    onEditProfile: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Perfil de Usuario") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onEditProfile) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar perfil")
                    }
                }
            )
        },
        bottomBar = { // Agregamos la barra de navegación
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto de perfil
            Image(
                painter = painterResource(id = R.drawable.profile_placeholder),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Nombre del usuario
            Text(
                text = "Nombre Apellido",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Email
            Text(
                text = "usuario@ejemplo.com",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Sección de información
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileInfoItem(title = "Edad", value = "30 años")
                    HorizontalDivider()
                    ProfileInfoItem(title = "Género", value = "Masculino")
                    HorizontalDivider()
                    ProfileInfoItem(title = "Ocupación", value = "Ingeniero")
                    HorizontalDivider()
                    ProfileInfoItem(title = "Nivel de estrés", value = "Moderado")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de cerrar sesión
            Button(
                onClick = { /* Lógica para cerrar sesión */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}

@Composable
fun ProfileInfoItem(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(text = value)
    }
}

// Vista previa en modo claro
@Preview(showBackground = true, name = "Vista Previa - Modo Claro")
@Composable
fun ProfileScreenPreview() {
    StressPredictTheme { // Usa el tema de tu app
        ProfileScreen(
            currentScreen = Screen.Profile,
            onNavigate = {},
            onNavigateBack = {},
            onEditProfile = {}
        )
    }
}