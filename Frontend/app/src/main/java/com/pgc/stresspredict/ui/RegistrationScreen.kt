package com.pgc.stresspredict.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pgc.stresspredict.ui.theme.StressPredictTheme

@Composable
fun RegistrationScreen(
    onRegisterSuccess: () -> Unit = {},
) {
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var codigo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Encabezado
        Text(
            text = "Registro",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campos del formulario
        OutlinedTextField(
            value = nombres,
            onValueChange = { nombres = it },
            label = { Text("Nombres") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = apellidos,
            onValueChange = { apellidos = it },
            label = { Text("Apellidos") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Institucional") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = codigo,
            onValueChange = { codigo = it },
            label = { Text("Código de estudiante") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botones
        Button(
            onClick = onRegisterSuccess,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Crear Cuenta")
        }

        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    StressPredictTheme {
        RegistrationScreen()
    }
}

