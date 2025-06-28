package com.pgc.stress_predict.application.dto.request;

public record PrediccionEstresRequest(
        String jwt,
        EntradaRequest entrada,
        String prediccion
) {
}
