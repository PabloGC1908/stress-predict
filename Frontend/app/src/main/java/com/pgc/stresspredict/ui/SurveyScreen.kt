package com.pgc.stresspredict.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.pgc.stresspredict.ui.theme.StressPredictTheme

@Composable
fun SurveyScreen() {
    // Contenido de la pantalla de encuesta irá aquí
}

// Vista previa en modo claro
@Preview(showBackground = true, name = "Light Mode")
@Composable
fun SurveyScreenPreview() {
    StressPredictTheme {
        SurveyScreen()
    }
}