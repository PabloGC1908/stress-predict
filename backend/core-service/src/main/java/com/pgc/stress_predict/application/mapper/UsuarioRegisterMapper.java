package com.pgc.stress_predict.application.mapper;

import com.pgc.stress_predict.application.dto.request.UsuarioFormRequest;
import com.pgc.stress_predict.application.service.RolService;
import com.pgc.stress_predict.domain.model.Rol;
import com.pgc.stress_predict.domain.model.Usuario;
import com.pgc.stress_predict.domain.model.UsuarioInfo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.function.Function;

@Service
public class UsuarioRegisterMapper implements Function<UsuarioFormRequest, Usuario> {
    private final RolService rolService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioRegisterMapper(RolService rolService, PasswordEncoder passwordEncoder) {
        this.rolService = rolService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario apply(UsuarioFormRequest usuarioFormRequest) {
        ZoneOffset offset = ZoneOffset.of("-05:00");

        UsuarioInfo usuarioInfo = UsuarioInfo.builder()
                .nombre(usuarioFormRequest.nombre())
                .apellido(usuarioFormRequest.apellido())
                .telefono(usuarioFormRequest.telefono())
                .dni(usuarioFormRequest.dni())
                .fechaNacimiento(usuarioFormRequest.fechaNacimiento())
                .horasEstudioDia(usuarioFormRequest.horasEstudioDia())
                .horasExtracurricularDia(usuarioFormRequest.horasExtracurricularDia())
                .horasSuenoDia(usuarioFormRequest.horasSuenoDia())
                .horasSocialDia(usuarioFormRequest.horasSocialDia())
                .horasActividadFisicaDia(usuarioFormRequest.horasActividadFisicaDia())
                .promedioCalificaciones(usuarioFormRequest.promedioCalificaciones())
                .build();

        Rol rol = rolService.findById(1);

        return Usuario.builder()
                .email(usuarioFormRequest.email())
                .password(passwordEncoder.encode(usuarioFormRequest.contrasenia()))
                .isEnabled(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .rol(rol)
                .usuarioInfo(usuarioInfo)
                .fechaCreacion(OffsetDateTime.now(offset))
                .ultimoInicioSesion(OffsetDateTime.now(offset))
                .build();
    }
}
