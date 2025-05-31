package com.pgc.stress_predict.application.dto.request;

import java.time.LocalDate;

public record UsuarioFormRequest(
        String nombre,
        String apellido,
        Integer dni,
        LocalDate fechaNacimiento,
        String telefono,
        String email,
        String contrasenia,
        Float horasEstudioDia,
        Float horasExtracurricularDia,
        Float horasSuenoDia,
        Float horasSocialDia,
        Float horasActividadFisicaDia,
        Float promedioCalificaciones
) {
}

