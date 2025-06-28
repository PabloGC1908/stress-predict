package com.pgc.stresspredict.ui.screen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pgc.stresspredict.R
import com.pgc.stresspredict.data.model.response.PerfilUsuarioResponse
import com.pgc.stresspredict.data.repository.UserRepository
import com.pgc.stresspredict.ui.theme.StressPredictTheme
import com.pgc.stresspredict.viewmodels.ProfileState
import com.pgc.stresspredict.viewmodels.ProfileViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val profileState by viewModel.profileState.collectAsState()
    val email by viewModel.email.collectAsState()

    // Manejar estados de error
    LaunchedEffect(profileState) {
        if (profileState is ProfileState.Error) {
            val error = (profileState as ProfileState.Error).message
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

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
                    IconButton(
                        onClick = onEditProfile,
                        enabled = profileState !is ProfileState.Loading
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar perfil")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (profileState) {
                is ProfileState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is ProfileState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error al cargar el perfil",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadProfile() }) {
                            Text("Reintentar")
                        }
                    }
                }
                is ProfileState.Success -> {
                    val profile = (profileState as ProfileState.Success).profile

                    // Foto de perfil
                    Image(
                        painter = painterResource(id = R.drawable.profile_placeholder),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(MaterialTheme.shapes.extraLarge),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Información del usuario
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${profile.nombre} ${profile.apellido}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Sección de información
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            ProfileInfoItem(
                                title = "Teléfono",
                                value = profile.telefono.toString()
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                            )
                            ProfileInfoItem(
                                title = "DNI",
                                value = profile.dni.toString()
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                            )
                            ProfileInfoItem(
                                title = "Fecha Nacimiento",
                                value = profile.fechaNacimiento.toString()
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                            )
                            ProfileInfoItem(
                                title = "Edad",
                                value = "${profile.calcularEdad()} años"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botón de cerrar sesión
                    Button(
                        onClick = {
                            viewModel.logout()
                            onLogout()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Text("Cerrar sesión")
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoItem(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    StressPredictTheme {
        ProfileScreen(
            viewModel = object : ProfileViewModel(UserRepository.mock()) {
                override fun loadProfile() {
                    _profileState.value = ProfileState.Success(
                        PerfilUsuarioResponse(
                            nombre = "Ana",
                            apellido = "Gómez",
                            telefono = 987654321,
                            dni = 87654321,
                            fechaNacimiento = "1995-05-15",
                            promedioHorasEstudioDia = 4.5f,
                            promedioHorasExtracurricularDia = TODO(),
                            promedioHorasSuenoDia = TODO(),
                            promedioHorasSocialDia = TODO(),
                            promedioHorasActividadFisica = TODO(),
                            promedioCalificaciones = TODO(),
                            // ... otros campos
                        )
                    )
                    _email.value = "ana@ejemplo.com"
                }
            },
            onNavigateBack = {},
            onEditProfile = {},
            onLogout = {}
        )
    }
}

fun UserRepository.Companion.mock(): UserRepository {
    return TODO("Provide the return value")
}
