package com.pgc.stress_predict.application.dto.request;

public record EncuestaRequest(
        Integer idUsuario,
        Float horasEstudioDia,
        Float horasExtracurricularDia,
        Float horasSuenoDia,
        Float horasSocialDia,
        Float horasActividadFisica,
        String comentario
) {
}
