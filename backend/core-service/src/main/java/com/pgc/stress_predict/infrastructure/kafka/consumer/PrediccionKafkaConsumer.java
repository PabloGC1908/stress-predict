package com.pgc.stress_predict.infrastructure.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgc.stress_predict.application.dto.request.PrediccionEstresRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PrediccionKafkaConsumer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "predicciones-estres", groupId = "grupo-estres")
    public void escucharMensaje(String mensajeJson) {
        try {
            PrediccionEstresRequest dto = objectMapper.readValue(mensajeJson, PrediccionEstresRequest.class);

            log.info("Predicción: {}", dto.prediccion());
            log.info("GPA: {}", dto.entrada().gpa());
            log.info("Horas de estudio: {}", dto.entrada().horas_estudio());

        } catch (JsonProcessingException e) {
            System.err.println("Error al deserializar JSON: " + e.getMessage());
        }
    }
}
