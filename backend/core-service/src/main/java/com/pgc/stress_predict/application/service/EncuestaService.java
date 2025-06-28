package com.pgc.stress_predict.application.service;

import com.pgc.stress_predict.application.dto.request.PrediccionEstresRequest;
import com.pgc.stress_predict.domain.model.Encuesta;
import com.pgc.stress_predict.domain.port.out.EncuestaRepository;
import org.springframework.stereotype.Service;

@Service
public class EncuestaService {
    private final EncuestaRepository encuestaRepository;

    public EncuestaService(EncuestaRepository encuestaRepository) {
        this.encuestaRepository = encuestaRepository;
    }

    public void saveEncuesta(PrediccionEstresRequest encuesta) {
        
    }
}
