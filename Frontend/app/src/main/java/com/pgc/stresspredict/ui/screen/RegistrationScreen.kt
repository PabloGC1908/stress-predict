package com.pgc.stresspredict.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pgc.stresspredict.data.model.request.UsuarioFormRequest
import com.pgc.stresspredict.ui.theme.StressPredictTheme
import com.pgc.stresspredict.util.showToast
import com.pgc.stresspredict.viewmodels.AuthViewModel

@Composable
fun RegistrationScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    // Form states
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }

    // Validation states
    var nombresError by remember { mutableStateOf(false) }
    var apellidosError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }

    // UI state
    val context = LocalContext.current
    val registrationState by viewModel.registrationState.collectAsState()

    // Handle registration state
    LaunchedEffect(registrationState) {
        when (registrationState) {
            is AuthViewModel.AuthState.Success -> {
                context.showToast("Registro exitoso")
                onRegisterSuccess()
            }
            is AuthViewModel.AuthState.Error -> {
                val error = (registrationState as AuthViewModel.AuthState.Error).message
                context.showToast(error)
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Personal Information Section
        Text(
            text = "Información Personal",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.align(Alignment.Start)
        )

        OutlinedTextField(
            value = nombres,
            onValueChange = { nombres = it.apply { nombresError = it.isEmpty() } },
            label = { Text("Nombres") },
            modifier = Modifier.fillMaxWidth(),
            isError = nombresError,
            supportingText = { if (nombresError) Text("Requerido") }
        )

        OutlinedTextField(
            value = apellidos,
            onValueChange = { apellidos = it.apply { apellidosError = it.isEmpty() } },
            label = { Text("Apellidos") },
            modifier = Modifier.fillMaxWidth(),
            isError = apellidosError,
            supportingText = { if (apellidosError) Text("Requerido") }
        )

        OutlinedTextField(
            value = dni,
            onValueChange = { dni = it },
            label = { Text("DNI") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = fechaNacimiento,
            onValueChange = { fechaNacimiento = it },
            label = { Text("Fecha Nacimiento (AAAA-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        // Account Information Section
        Text(
            text = "Información de Cuenta",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.align(Alignment.Start)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = it.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError,
            supportingText = { if (emailError) Text("Email inválido") }
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = it.length < 6 && it.isNotEmpty()
                confirmPasswordError = it != confirmPassword && confirmPassword.isNotEmpty()
            },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = passwordError,
            supportingText = { if (passwordError) Text("Mínimo 6 caracteres") }
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = it != password && it.isNotEmpty()
            },
            label = { Text("Confirmar Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = confirmPasswordError,
            supportingText = { if (confirmPasswordError) Text("Las contraseñas no coinciden") }
        )

        // Submit Button
        Button(
            onClick = {
                nombresError = nombres.isEmpty()
                apellidosError = apellidos.isEmpty()
                emailError = email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                passwordError = password.length < 6
                confirmPasswordError = password != confirmPassword

                if (!nombresError && !apellidosError && !emailError && !passwordError && !confirmPasswordError) {
                    viewModel.registerUser(
                        UsuarioFormRequest(
                            nombre = nombres,
                            apellido = apellidos,
                            email = email,
                            contrasenia = password,
                            dni = dni.toIntOrNull(),
                            telefono = telefono.ifEmpty { null },
                            fechaNacimiento = fechaNacimiento.ifEmpty { null },
                            // Nullable fields
                            horasEstudioDia = null,
                            horasExtracurricularDia = null,
                            horasSuenoDia = null,
                            horasSocialDia = null,
                            horasActividadFisicaDia = null,
                            promedioCalificaciones = null
                        )
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = registrationState !is AuthViewModel.AuthState.Loading
        ) {
            if (registrationState is AuthViewModel.AuthState.Loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp))
            } else {
                Text("Registrarse")
            }
        }

        // Login Link
        TextButton(onClick = onNavigateToLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    StressPredictTheme {
        RegistrationScreen(
            onNavigateToLogin = {},
            onRegisterSuccess = {}
        )
    }
}