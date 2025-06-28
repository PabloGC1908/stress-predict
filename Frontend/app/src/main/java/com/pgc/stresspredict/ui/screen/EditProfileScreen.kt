package com.pgc.stresspredict.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pgc.stresspredict.ui.theme.StressPredictTheme
import com.pgc.stresspredict.viewmodels.ProfileState
import com.pgc.stresspredict.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val profileState by viewModel.profileState.collectAsState()
    val scrollState = rememberScrollState()

    // Estados locales para los campos editables
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }

    // Inicializar valores cuando el perfil se carga
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Cancelar")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.updateProfile(
                                nombre = nombre,
                                apellido = apellido,
                                telefono = telefono.toIntOrNull() ?: 0,
                                dni = dni.toIntOrNull() ?: 0,
                                fechaNacimiento = fechaNacimiento
                            )
                            onNavigateBack()
                        }
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Guardar")
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombres *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text("Apellidos *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { if (it.all { c -> c.isDigit() }) telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            OutlinedTextField(
                value = dni,
                onValueChange = { if (it.all { c -> c.isDigit() }) dni = it },
                label = { Text("DNI") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = fechaNacimiento,
                onValueChange = { fechaNacimiento = it },
                label = { Text("Fecha de Nacimiento (AAAA-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ej: 1990-05-15") }
            )
        }
    }

    // Manejar errores
    LaunchedEffect(viewModel) {
        viewModel.profileState.collect { state ->
            if (state is ProfileState.Error) {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Preview
@Composable
fun EditProfileScreenPreview() {
    StressPredictTheme {
        EditProfileScreen(
            viewModel = viewModel(), // Necesitarás un ViewModel mock para el preview
            onNavigateBack = {}
        )
    }
}