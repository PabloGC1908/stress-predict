package com.pgc.stresspredict.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pgc.stresspredict.data.model.request.UsuarioFormRequest
import com.pgc.stresspredict.ui.theme.StressPredictTheme
import com.pgc.stresspredict.viewmodels.AuthViewModel

@Composable
fun RegistrationScreen(
    viewModel: AuthViewModel = viewModel(),
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    // Estados del formulario
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }

    // Estados de validación
    var nombresError by remember { mutableStateOf(false) }
    var apellidosError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }

    // Contexto y estado
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val registrationState by viewModel.registrationState.collectAsState()

    // Manejar respuesta del registro
    LaunchedEffect(registrationState) {
        when (registrationState) {
            is AuthViewModel.AuthState.Success -> {
                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                onRegisterSuccess()
            }
            is AuthViewModel.AuthState.Error -> {
                val error = (registrationState as AuthViewModel.AuthState.Error).message
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)  // Habilita el scroll
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Registro",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo Nombres
        OutlinedTextField(
            value = nombres,
            onValueChange = {
                nombres = it
                nombresError = it.isEmpty()
            },
            label = { Text("Nombres *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = nombresError
        )
        if (nombresError) {
            Text(
                text = "El nombre es obligatorio",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Apellidos
        OutlinedTextField(
            value = apellidos,
            onValueChange = {
                apellidos = it
                apellidosError = it.isEmpty()
            },
            label = { Text("Apellidos *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = apellidosError
        )
        if (apellidosError) {
            Text(
                text = "Los apellidos son obligatorios",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Email
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = it.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
            },
            label = { Text("Correo Electrónico *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError
        )
        if (emailError) {
            Text(
                text = "Ingrese un email válido",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Teléfono
        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo DNI
        OutlinedTextField(
            value = dni,
            onValueChange = { dni = it },
            label = { Text("DNI") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Fecha de Nacimiento
        OutlinedTextField(
            value = fechaNacimiento,
            onValueChange = { fechaNacimiento = it },
            label = { Text("Fecha de Nacimiento (AAAA-MM-DD)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = it.length < 6 && it.isNotEmpty()
                confirmPasswordError = it != confirmPassword && confirmPassword.isNotEmpty()
            },
            label = { Text("Contraseña *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = passwordError
        )
        if (passwordError) {
            Text(
                text = "La contraseña debe tener al menos 6 caracteres",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Confirmar Contraseña
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = it != password && it.isNotEmpty()
            },
            label = { Text("Confirmar Contraseña *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = confirmPasswordError
        )
        if (confirmPasswordError) {
            Text(
                text = "Las contraseñas no coinciden",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de Registro
        Button(
            onClick = {
                // Validar campos
                nombresError = nombres.isEmpty()
                apellidosError = apellidos.isEmpty()
                emailError = email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                passwordError = password.length < 6
                confirmPasswordError = password != confirmPassword

                if (!nombresError && !apellidosError && !emailError && !passwordError && !confirmPasswordError) {
                    val userData = UsuarioFormRequest(
                        nombre = nombres,
                        apellido = apellidos,
                        dni = dni.toIntOrNull(),
                        fechaNacimiento = fechaNacimiento.ifEmpty { null },
                        telefono = telefono.ifEmpty { null },
                        email = email,
                        contrasenia = password,
                        horasEstudioDia = null,
                        horasExtracurricularDia = null,
                        horasSuenoDia = null,
                        horasSocialDia = null,
                        horasActividadFisicaDia = null,
                        promedioCalificaciones = null
                    )
                    viewModel.registerUser(userData)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = registrationState !is AuthViewModel.AuthState.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White
            )
        ) {
            if (registrationState is AuthViewModel.AuthState.Loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text("Crear Cuenta")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enlace a Login
        TextButton(onClick = onNavigateToLogin) {
            Text("¿Ya tienes cuenta? Inicia Sesión")
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