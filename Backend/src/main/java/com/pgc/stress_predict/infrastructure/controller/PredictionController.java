package com.pgc.stress_predict.infrastructure.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/prediccion")
public class PredictionController {

    @PostMapping
    public String predecirEstres(@RequestBody Map<String, Object> datos) {
        // Simulamos una lógica de predicción de estrés basada en los datos
        double ritmoCardiaco = Double.parseDouble(datos.get("ritmoCardiaco").toString());
        double horasDeSueno = Double.parseDouble(datos.get("horasDeSueno").toString());

        String resultado;
        if (ritmoCardiaco > 100 || horasDeSueno < 5) {
            resultado = "Alto";
        } else if (ritmoCardiaco > 85 || horasDeSueno < 6) {
            resultado = "Moderado";
        } else {
            resultado = "Bajo";
        }

        return "Nivel de estrés: " + resultado;
    }
}