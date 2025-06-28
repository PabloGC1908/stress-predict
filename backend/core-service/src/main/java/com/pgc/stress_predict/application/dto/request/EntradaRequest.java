package com.pgc.stress_predict.application.dto.request;

public record EntradaRequest(
        Float horas_estudio,
        Float horas_extracurriculares,
        Float horas_sueno,
        Float horas_sociales,
        Float actividad_fisica,
        Float promedio_calificaciones
) {
}
