package com.pgc.stresspredict.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pgc.stresspredict.Screen
import com.pgc.stresspredict.ui.component.navigation.BottomNavigationBar
import com.pgc.stresspredict.ui.theme.StressPredictTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyScreen(
    currentScreen: Screen = Screen.Survey,
    onNavigate: (Screen) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onSubmitSurvey: (EncuestaRequest) -> Unit = {}
) {
    // State for time inputs
    val (studyHours, setStudyHours) = remember { mutableStateOf("") }
    val (extracurricularHours, setExtracurricularHours) = remember { mutableStateOf("") }
    val (sleepHours, setSleepHours) = remember { mutableStateOf("") }
    val (socialHours, setSocialHours) = remember { mutableStateOf("") }
    val (exerciseHours, setExerciseHours) = remember { mutableStateOf("") }
    val (comment, setComment) = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Encuesta") },
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
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE3F2FD)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Por favor, responde las preguntas:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D47A1)
                        )

                        // Question 1
                        TimeQuestion(
                            question = "1. ¿Cuántas horas estudiaste el dia de hoy?",
                            value = studyHours,
                            onValueChange = setStudyHours
                        )

                        // Question 2
                        TimeQuestion(
                            question = "2. ¿Cuántas horas dedicaste a actividades extracurriculares?",
                            value = extracurricularHours,
                            onValueChange = setExtracurricularHours
                        )

                        // Question 3
                        TimeQuestion(
                            question = "3. ¿Cuántas horas dormiste?",
                            value = sleepHours,
                            onValueChange = setSleepHours
                        )

                        // Question 4
                        TimeQuestion(
                            question = "4. ¿Cuántas horas dedicaste a socializar?",
                            value = socialHours,
                            onValueChange = setSocialHours
                        )

                        // Question 5
                        TimeQuestion(
                            question = "5. ¿Cuántas horas dedicaste a alguna actividad física?",
                            value = exerciseHours,
                            onValueChange = setExerciseHours
                        )

                        // Comment field
                        Text(
                                text = "6. Comentario (opcional)",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        OutlinedTextField(
                            value = comment,
                            onValueChange = setComment,
                            label = { Text("Escribe aquí tus comentarios") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                onSubmitSurvey(
                                    EncuestaRequest(
                                        idUsuario = 1, // Should come from user session
                                        horasEstudioDia = studyHours.toFloatOrNull() ?: 0f,
                                        horasExtracurricularDia = extracurricularHours.toFloatOrNull() ?: 0f,
                                        horasSuenoDia = sleepHours.toFloatOrNull() ?: 0f,
                                        horasSocialDia = socialHours.toFloatOrNull() ?: 0f,
                                        horasActividadFisica = exerciseHours.toFloatOrNull() ?: 0f,
                                        comentario = comment
                                    )
                                )
                                onNavigate(Screen.Results)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0D47A1),
                                contentColor = Color.White
                            ),
                            enabled = studyHours.isNotEmpty() &&
                                    extracurricularHours.isNotEmpty() &&
                                    sleepHours.isNotEmpty() &&
                                    socialHours.isNotEmpty() &&
                                    exerciseHours.isNotEmpty()
                        ) {
                            Text("Enviar Registro")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimeQuestion(
    question: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(
            text = question,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.toFloatOrNull() != null) {
                    onValueChange(newValue)
                }
            },
            label = { Text("Horas") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            suffix = { Text(text = "horas") }
        )
    }
}

data class EncuestaRequest(
    val idUsuario: Int,
    val horasEstudioDia: Float,
    val horasExtracurricularDia: Float,
    val horasSuenoDia: Float,
    val horasSocialDia: Float,
    val horasActividadFisica: Float,
    val comentario: String
)

@Preview(showBackground = true)
@Composable
fun SurveyScreenPreview() {
    StressPredictTheme {
        SurveyScreen(
            currentScreen = Screen.Survey,
            onNavigate = {},
            onNavigateBack = {},
            onSubmitSurvey = {}
        )
    }
}