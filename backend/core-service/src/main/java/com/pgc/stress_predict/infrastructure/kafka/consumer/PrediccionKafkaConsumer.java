package com.pgc.stress_predict.infrastructure.kafka.consumer;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgc.stress_predict.application.dto.request.PrediccionEstresRequest;
import com.pgc.stress_predict.domain.model.Encuesta;
import com.pgc.stress_predict.domain.model.Usuario;
import com.pgc.stress_predict.domain.port.out.EncuestaRepository;
import com.pgc.stress_predict.domain.port.out.UsuarioRepository;
import com.pgc.stress_predict.infrastructure.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
public class PrediccionKafkaConsumer {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final UsuarioRepository usuarioRepository;
    private final EncuestaRepository encuestaRepository;

    public PrediccionKafkaConsumer(JwtService jwtService, ObjectMapper objectMapper,
                                   UsuarioRepository usuarioRepository, EncuestaRepository encuestaRepository) {
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
        this.usuarioRepository = usuarioRepository;
        this.encuestaRepository = encuestaRepository;
    }

    @KafkaListener(topics = "predicciones-estres", groupId = "grupo-estres")
    @Transactional
    public void escucharMensaje(String mensajeJson) {
        if (mensajeJson == null || mensajeJson.trim().isEmpty()) {
            log.warn("Mensaje vacío recibido. Se ignora.");
            return;
        }

        try {
            PrediccionEstresRequest dto = objectMapper.readValue(mensajeJson, PrediccionEstresRequest.class);

            String token = dto.jwt().replace("Bearer ", "");
            DecodedJWT decodedJWT = jwtService.verifyToken(token);
            Long usuarioId = jwtService.getEspecificClaim(decodedJWT, "userId").asLong();

            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            log.info("Predicción: {}", dto.prediccion());
            log.info("GPA: {}", dto.entrada().promedio_calificaciones());
            log.info("Horas de estudio: {}", dto.entrada().horas_estudio());
            log.info("Token: {}", token);

            Encuesta encuesta = Encuesta.builder()
                    .usuario(usuario)
                    .fecha(LocalDate.now())
                    .horasEstudioDia(dto.entrada().horas_estudio())
                    .horasExtracurricularDia(dto.entrada().horas_extracurriculares())
                    .horasSuenoDia(dto.entrada().horas_sueno())
                    .horasSocialDia(dto.entrada().horas_sociales())
                    .horasActividadFisica(dto.entrada().actividad_fisica())
                    .promedioCalificaciones(dto.entrada().promedio_calificaciones())
                    .prediccion(dto.prediccion())
                    .build();

            encuestaRepository.save(encuesta);

            log.info("Encuesta guardada para usuario ID: {}", usuarioId);

        } catch (JsonProcessingException e) {
            log.error("Error al deserializar JSON: {}", e.getMessage());
        }
    }

}
