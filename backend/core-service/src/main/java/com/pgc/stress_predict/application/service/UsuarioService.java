package com.pgc.stress_predict.application.service;

import com.pgc.stress_predict.application.dto.response.HistorialUsuarioResponse;
import com.pgc.stress_predict.domain.model.Usuario;
import com.pgc.stress_predict.domain.port.out.UsuarioRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<HistorialUsuarioResponse> findHistorialUsuario(Long usuarioId) {
        if (usuarioId == null)
            throw new UsernameNotFoundException("El id de usuario no puede ser nulo");

        Optional<List<HistorialUsuarioResponse>> historialUsuario = Optional.ofNullable(
                usuarioRepository.findHistorialUsuarioById(usuarioId)
        );

        if (historialUsuario.isEmpty())
            throw new UsernameNotFoundException("El id de usuario no se encontro");
        else
            return historialUsuario.get();
    }
}
