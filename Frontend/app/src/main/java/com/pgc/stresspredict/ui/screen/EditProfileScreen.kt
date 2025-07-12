package com.pgc.stresspredict.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pgc.stresspredict.ui.theme.StressPredictTheme
import com.pgc.stresspredict.util.showToast
import com.pgc.stresspredict.viewmodels.ProfileUiState
import com.pgc.stresspredict.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()

    // Form fields with better state management
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var formErrors by remember { mutableStateOf(emptyMap<String, String>()) }

    // Initialize form with profile data
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is ProfileUiState.Success -> {
                state.profile.let { profile ->
                    nombre = profile.nombre ?: ""
                    apellido = profile.apellido ?: ""
                    telefono = profile.telefono?.toString() ?: ""
                    dni = profile.dni?.toString() ?: ""
                    fechaNacimiento = profile.fechaNacimiento ?: ""
                }
            }
            is ProfileUiState.Error -> {
                if (state.shouldLogout) {
                    context.showToast("Sesión expirada")
                    viewModel.logout(silent = true)
                } else {
                    context.showToast(state.message)
                }
            }
            else -> {}
        }
    }

    // Handle update states
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is ProfileUiState.Success -> {
                state.updateError?.let {
                    context.showToast(it)
                    formErrors = parseUpdateErrors(it)
                }
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            EditProfileTopBar(
                onBack = {
                    if (uiState !is ProfileUiState.Success || !(uiState as ProfileUiState.Success).isUpdating) {
                        onNavigateBack()
                    }
                },
                onSave = {
                    if (validateForm(nombre, apellido, telefono, dni, fechaNacimiento)) {
                        viewModel.updateProfile(
                            nombre = nombre.trim(),
                            apellido = apellido.trim(),
                            telefono = telefono.toIntOrNull(),
                            dni = dni.toIntOrNull(),
                            fechaNacimiento = fechaNacimiento.ifEmpty { null }.toString()
                        )
                    }
                },
                isSaving = uiState is ProfileUiState.Success && (uiState as ProfileUiState.Success).isUpdating
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            ProfileUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ProfileUiState.Error -> {
                ErrorEditProfile(
                    errorState = state,
                    onRetry = { viewModel.loadProfile() },
                    onBack = onNavigateBack
                )
            }
            is ProfileUiState.Success -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    ProfileForm(
                        nombre = nombre,
                        onNombreChange = { nombre = it; formErrors = formErrors - "nombre" },
                        apellido = apellido,
                        onApellidoChange = { apellido = it; formErrors = formErrors - "apellido" },
                        telefono = telefono,
                        onTelefonoChange = { if (it.all { c -> c.isDigit() }) telefono = it; formErrors = formErrors - "telefono" },
                        dni = dni,
                        onDniChange = { if (it.all { c -> c.isDigit() }) dni = it; formErrors = formErrors - "dni" },
                        fechaNacimiento = fechaNacimiento,
                        onFechaNacimientoChange = { fechaNacimiento = it; formErrors = formErrors - "fechaNacimiento" },
                        errors = formErrors,
                        modifier = Modifier
                            .padding(innerPadding)
                            .verticalScroll(scrollState)
                    )

                    if (state.isUpdating) {
                        Surface(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
            ProfileUiState.LoggedOut -> {
                LaunchedEffect(Unit) { onNavigateBack() }
            }
        }
    }
}

@Composable
private fun ErrorEditProfile(
    errorState: ProfileUiState.Error,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Error al cargar el perfil",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = errorState.message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text("Volver")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileTopBar(
    onBack: () -> Unit,
    onSave: () -> Unit,
    isSaving: Boolean = false
) {
    CenterAlignedTopAppBar(
        title = { Text("Editar Perfil") },
        navigationIcon = {
            IconButton(
                onClick = onBack,
                enabled = !isSaving
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Cancelar")
            }
        },
        actions = {
            IconButton(
                onClick = onSave,
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Save, contentDescription = "Guardar")
                }
            }
        }
    )
}

@Composable
private fun ProfileForm(
    nombre: String,
    onNombreChange: (String) -> Unit,
    apellido: String,
    onApellidoChange: (String) -> Unit,
    telefono: String,
    onTelefonoChange: (String) -> Unit,
    dni: String,
    onDniChange: (String) -> Unit,
    fechaNacimiento: String,
    onFechaNacimientoChange: (String) -> Unit,
    errors: Map<String, String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileTextField(
            value = nombre,
            onValueChange = onNombreChange,
            label = "Nombres",
            required = true,
            isError = errors.containsKey("nombre"),
            errorMessage = errors["nombre"]
        )

        ProfileTextField(
            value = apellido,
            onValueChange = onApellidoChange,
            label = "Apellidos",
            required = true,
            isError = errors.containsKey("apellido"),
            errorMessage = errors["apellido"]
        )

        ProfileTextField(
            value = telefono,
            onValueChange = onTelefonoChange,
            label = "Teléfono",
            keyboardType = KeyboardType.Phone,
            isError = errors.containsKey("telefono"),
            errorMessage = errors["telefono"]
        )

        ProfileTextField(
            value = dni,
            onValueChange = onDniChange,
            label = "DNI",
            keyboardType = KeyboardType.Number,
            isError = errors.containsKey("dni"),
            errorMessage = errors["dni"]
        )

        ProfileTextField(
            value = fechaNacimiento,
            onValueChange = onFechaNacimientoChange,
            label = "Fecha de Nacimiento",
            placeholder = "AAAA-MM-DD",
            isError = errors.containsKey("fechaNacimiento"),
            errorMessage = errors["fechaNacimiento"]
        )
    }
}

@Composable
private fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    required: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("$label${if (required) " *" else ""}") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            placeholder = placeholder?.let { { Text(it) } },
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(text = errorMessage ?: "Campo inválido")
                }
            }
        )
    }
}

private fun validateForm(
    nombre: String,
    apellido: String,
    telefono: String,
    dni: String,
    fechaNacimiento: String
): Boolean {
    val errors = mutableMapOf<String, String>()

    if (nombre.isBlank()) {
        errors["nombre"] = "El nombre es requerido"
    }

    if (apellido.isBlank()) {
        errors["apellido"] = "El apellido es requerido"
    }

    if (telefono.isNotBlank() && !telefono.all { it.isDigit() }) {
        errors["telefono"] = "Solo números permitidos"
    }

    if (dni.isNotBlank() && !dni.all { it.isDigit() }) {
        errors["dni"] = "Solo números permitidos"
    }

    if (fechaNacimiento.isNotBlank() && !isValidDate(fechaNacimiento)) {
        errors["fechaNacimiento"] = "Formato AAAA-MM-DD"
    }

    return errors.isEmpty()
}

private fun isValidDate(date: String): Boolean {
    return try {
        val parts = date.split("-")
        parts.size == 3 && parts[0].length == 4 && parts[1].length == 2 && parts[2].length == 2
    } catch (e: Exception) {
        false
    }
}

private fun parseUpdateErrors(errorMessage: String): Map<String, String> {
    // Implement parsing logic based on your API error responses
    return emptyMap()
}

@Preview
@Composable
fun EditProfileScreenPreview() {
    StressPredictTheme {
        EditProfileScreen(
            onNavigateBack = {}
        )
    }
}