package com.pgc.stresspredict.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pgc.stresspredict.util.showToast
import com.pgc.stresspredict.viewmodels.PredictionState
import com.pgc.stresspredict.viewmodels.StressFormField
import com.pgc.stresspredict.viewmodels.StressViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyScreen(
    viewModel: StressViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToResults: () -> Unit
) {
    val context = LocalContext.current
    val formState by viewModel.formData.collectAsState()
    val predictionState by viewModel.predictionState.collectAsState()
    val formValidation by viewModel.formValidation.collectAsState()

    // Manejador de estados de predicción
    LaunchedEffect(predictionState) {
        when (predictionState) {
            is PredictionState.Success -> {
                context.showToast("Predicción completada")
                onNavigateToResults()
            }
            is PredictionState.Error -> {
                val error = (predictionState as PredictionState.Error).message
                context.showToast("Error: $error")
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Encuesta de Estrés") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campos del formulario con validación
            NumberInputField(
                value = formState.horasEstudio.toString(),
                onValueChange = { viewModel.updateFormField(StressFormField.STUDY_HOURS, it) },
                label = "Horas de estudio",
                isError = formValidation[StressFormField.STUDY_HOURS] == false,
                errorMessage = "Debe ser mayor a 0",
                modifier = Modifier.fillMaxWidth()
            )

            NumberInputField(
                value = formState.horasExtracurriculares.toString(),
                onValueChange = { viewModel.updateFormField(StressFormField.EXTRACURRICULAR_HOURS, it) },
                label = "Horas extracurriculares",
                modifier = Modifier.fillMaxWidth()
            )

            NumberInputField(
                value = formState.horasSueno.toString(),
                onValueChange = { viewModel.updateFormField(StressFormField.SLEEP_HOURS, it) },
                label = "Horas de sueño",
                isError = formValidation[StressFormField.SLEEP_HOURS] == false,
                errorMessage = "Debe ser mayor a 0",
                modifier = Modifier.fillMaxWidth()
            )

            NumberInputField(
                value = formState.horasSociales.toString(),
                onValueChange = { viewModel.updateFormField(StressFormField.SOCIAL_HOURS, it) },
                label = "Horas sociales",
                modifier = Modifier.fillMaxWidth()
            )

            NumberInputField(
                value = formState.horasActividadFisica.toString(),
                onValueChange = { viewModel.updateFormField(StressFormField.PHYSICAL_ACTIVITY, it) },
                label = "Horas de actividad física",
                modifier = Modifier.fillMaxWidth()
            )

            NumberInputField(
                value = formState.promedioCalificaciones.toString(),
                onValueChange = { viewModel.updateFormField(StressFormField.GPA, it) },
                label = "Promedio de calificaciones",
                isError = formValidation[StressFormField.GPA] == false,
                errorMessage = "Debe ser entre 0 y 20",
                modifier = Modifier.fillMaxWidth(),
                isGPA = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de envío
            Button(
                onClick = { viewModel.predictStress() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = viewModel.isFormValid() && predictionState !is PredictionState.Loading
            ) {
                if (predictionState is PredictionState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp))
                } else {
                    Text("Calcular Nivel de Estrés")
                }
            }
        }
    }
}

@Composable
private fun NumberInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    isGPA: Boolean = false
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                    onValueChange(newValue)
                }
            },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = isError,
            suffix = { Text(if (isGPA) "puntos" else "horas") }
        )

        if (isError && !errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}