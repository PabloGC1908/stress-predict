package com.pgc.stress_predict.application.dto.request;

public record PrediccionEstresRequest(
    EntradaRequest entrada,
    String prediccion
) {
}
