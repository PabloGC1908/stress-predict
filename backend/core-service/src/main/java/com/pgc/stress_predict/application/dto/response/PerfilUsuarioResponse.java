package com.pgc.stress_predict.application.dto.response;

import java.time.LocalDate;

public record PerfilUsuarioResponse(
        String nombre,
        String apellido,
        Integer telefono,
        Integer dni,
        LocalDate fechaNacimiento,
        Float promedioHorasEstudioDia,
        Float promedioHorasExtracurricularDia,
        Float promedioHorasSuenoDia,
        Float promedioHorasSocialDia,
        Float promedioHorasActividadFisica,
        Float promedioCalificaciones
) {
}
