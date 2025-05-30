package com.pgc.stresspredict.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.pgc.stresspredict.ui.theme.StressPredictTheme

@Composable
fun RecommendationsScreen() {
    // Contenido de la pantalla de recomendaciones irá aquí
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun RecommendationsScreenPreview() {
    StressPredictTheme {
        RecommendationsScreen()
    }
}