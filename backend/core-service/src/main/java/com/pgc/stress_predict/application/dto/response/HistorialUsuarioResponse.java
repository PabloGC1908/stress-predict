package com.pgc.stress_predict.application.dto.response;

import java.time.LocalDate;

public record HistorialUsuarioResponse(
        LocalDate fecha,
        Float horasEstudioDia,
        Float horasExtracurricularDia,
        Float horasSuenoDia,
        Float horasSocialDia,
        Float horasActividadFisica,
        Float promedioCalificaciones,
        String prediccion
) {
}
