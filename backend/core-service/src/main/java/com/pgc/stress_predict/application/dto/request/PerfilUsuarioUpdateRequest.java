package com.pgc.stress_predict.application.dto.request;

import java.time.LocalDate;

public record PerfilUsuarioUpdateRequest(
        String nombre,
        String apellido,
        Integer telefono,
        Integer dni,
        LocalDate fechaNacimiento
) {
}