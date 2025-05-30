package com.pgc.stresspredict.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.pgc.stresspredict.ui.theme.StressPredictTheme

@Composable
fun HistoryScreen() {
    // Contenido de la pantalla de historial irá aquí
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun HistoryScreenPreview() {
    StressPredictTheme {
        HistoryScreen()
    }
}
