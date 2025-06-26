package com.pgc.stress_predict.infrastructure.controller;

import com.pgc.stress_predict.application.dto.request.PerfilUsuarioUpdateRequest;
import com.pgc.stress_predict.application.dto.response.HistorialUsuarioResponse;
import com.pgc.stress_predict.application.dto.response.PerfilUsuarioResponse;
import com.pgc.stress_predict.application.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/historial")
    public ResponseEntity<List<HistorialUsuarioResponse>> getHistorialUsuarios(Authentication authentication) {
        Long usuarioId = (Long) authentication.getPrincipal();
        List<HistorialUsuarioResponse> historialUsuario = usuarioService.findHistorialUsuario(usuarioId);

        return new ResponseEntity<>(historialUsuario, HttpStatus.OK);
    }

    @GetMapping("/perfil")
    public ResponseEntity<PerfilUsuarioResponse> getPerfil(Authentication authentication) {
        Long usuarioId = (Long) authentication.getPrincipal();
        PerfilUsuarioResponse perfilUsuario = usuarioService.findPerfilUsuario(usuarioId);

        return new ResponseEntity<>(perfilUsuario, HttpStatus.OK);
    }

    @PatchMapping("/perfil")
    public ResponseEntity<String> updatePerfilUsuario(Authentication authentication, @RequestBody PerfilUsuarioUpdateRequest perfilUsuarioUpdateRequest) {
        Long usuarioId = (Long) authentication.getPrincipal();
        String respuesta = usuarioService.updatePerfilUsuario(usuarioId, perfilUsuarioUpdateRequest);

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
