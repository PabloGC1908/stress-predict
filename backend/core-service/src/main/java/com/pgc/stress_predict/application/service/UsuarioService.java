package com.pgc.stress_predict.application.service;

import com.pgc.stress_predict.application.dto.request.PerfilUsuarioUpdateRequest;
import com.pgc.stress_predict.application.dto.response.HistorialUsuarioResponse;
import com.pgc.stress_predict.application.dto.response.PerfilUsuarioResponse;
import com.pgc.stress_predict.domain.model.Usuario;
import com.pgc.stress_predict.domain.model.UsuarioInfo;
import com.pgc.stress_predict.domain.port.out.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
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

    public PerfilUsuarioResponse findPerfilUsuario(Long usuarioId) {
        if (usuarioId == null)
            throw new UsernameNotFoundException("El id de usuario no puede ser nulo");

        Optional<PerfilUsuarioResponse> perfilUsuario = Optional.ofNullable(
                usuarioRepository.findPerfilById(usuarioId)
        );

        if (perfilUsuario.isEmpty())
            throw new UsernameNotFoundException("El id de usuario no se encontro");
        else
            return perfilUsuario.get();
    }


    public String updatePerfilUsuario(Long usuarioId, PerfilUsuarioUpdateRequest perfilUsuarioUpdateRequest) {
        if (usuarioId == null)
            throw new UsernameNotFoundException("El id de usuario no puede ser nulo");

        if (perfilUsuarioUpdateRequest == null)
            throw new UsernameNotFoundException("La perfil no puede ser nula");

        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);

        if (usuario.isEmpty()) {
            throw new UsernameNotFoundException("El id de usuario no existe");
        }
        else {
            UsuarioInfo usuarioInfo = usuario.get().getUsuarioInfo();
            log.info("Datos del contacto: {}", usuarioInfo.toString());
            log.info("Datos a actualizar: {}", perfilUsuarioUpdateRequest);

            if (perfilUsuarioUpdateRequest.nombre() != null)
                usuarioInfo.setNombre(perfilUsuarioUpdateRequest.nombre());

            if (perfilUsuarioUpdateRequest.apellido() != null)
                usuarioInfo.setApellido(perfilUsuarioUpdateRequest.apellido());

            if (perfilUsuarioUpdateRequest.telefono() != null)
                usuarioInfo.setTelefono(perfilUsuarioUpdateRequest.telefono());

            if (perfilUsuarioUpdateRequest.dni() != null)
                usuarioInfo.setDni(perfilUsuarioUpdateRequest.dni());

            if (perfilUsuarioUpdateRequest.fechaNacimiento() != null)
                usuarioInfo.setFechaNacimiento(perfilUsuarioUpdateRequest.fechaNacimiento());

            usuario.get().setUsuarioInfo(usuarioInfo);

            usuarioRepository.save(usuario.get());
        }

        return "Se actualizo el perfil de usuario correctamente";
    }
}
