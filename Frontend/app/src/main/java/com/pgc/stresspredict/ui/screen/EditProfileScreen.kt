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
import com.pgc.stresspredict.viewmodels.ProfileState
import com.pgc.stresspredict.viewmodels.ProfileViewModel
import com.pgc.stresspredict.viewmodels.UpdateState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val profileState by viewModel.profileState.collectAsState()
    val updateState by viewModel.updateState.collectAsState()

    // Form fields
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }

    // Initialize form with profile data
    LaunchedEffect(profileState) {
        if (profileState is ProfileState.Success) {
            val profile = (profileState as ProfileState.Success).profile
            nombre = profile.nombre
            apellido = profile.apellido
            telefono = profile.telefono.toString()
            dni = profile.dni.toString()
            fechaNacimiento = profile.fechaNacimiento
        }
    }

    // Handle states
    LaunchedEffect(profileState, updateState) {
        when {
            profileState is ProfileState.Error -> {
                context.showToast((profileState as ProfileState.Error).message)
            }
            updateState is UpdateState.Error -> {
                context.showToast((updateState as UpdateState.Error).message)
            }
            updateState is UpdateState.Success -> {
                context.showToast("Perfil actualizado correctamente")
                onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            EditProfileTopBar(
                onBack = onNavigateBack,
                onSave = {
                    viewModel.updateProfile(
                        nombre = nombre,
                        apellido = apellido,
                        telefono = telefono.toIntOrNull(),
                        dni = dni.toIntOrNull(),
                        fechaNacimiento = fechaNacimiento.ifEmpty { null }.toString()
                    )
                },
                isSaving = updateState is UpdateState.Loading
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            ProfileForm(
                nombre = nombre,
                onNombreChange = { nombre = it },
                apellido = apellido,
                onApellidoChange = { apellido = it },
                telefono = telefono,
                onTelefonoChange = { if (it.all { c -> c.isDigit() }) telefono = it },
                dni = dni,
                onDniChange = { if (it.all { c -> c.isDigit() }) dni = it },
                fechaNacimiento = fechaNacimiento,
                onFechaNacimientoChange = { fechaNacimiento = it },
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
            )

            if (updateState is UpdateState.Loading) {
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
            required = true
        )

        ProfileTextField(
            value = apellido,
            onValueChange = onApellidoChange,
            label = "Apellidos",
            required = true
        )

        ProfileTextField(
            value = telefono,
            onValueChange = onTelefonoChange,
            label = "Teléfono",
            keyboardType = KeyboardType.Phone
        )

        ProfileTextField(
            value = dni,
            onValueChange = onDniChange,
            label = "DNI",
            keyboardType = KeyboardType.Number
        )

        ProfileTextField(
            value = fechaNacimiento,
            onValueChange = onFechaNacimientoChange,
            label = "Fecha de Nacimiento",
            placeholder = "AAAA-MM-DD"
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
    placeholder: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("$label${if (required) " *" else ""}") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        placeholder = placeholder?.let { { Text(it) } }
    )
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