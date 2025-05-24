package com.pgc.stress_predict.infrastructure.controller;

import com.pgc.stress_predict.domain.models.model.Usuario;
import com.pgc.stress_predict.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/register")
    public String registrar(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return "Correo ya registrado";
        }
        usuarioRepository.save(usuario);
        return "Usuario registrado exitosamente";
    }

    @PostMapping("/login")
    public String login(@RequestBody Usuario usuario) {
        return usuarioRepository.findByEmailAndPassword(usuario.getEmail(), usuario.getPassword())
                .map(u -> "Login exitoso")
                .orElse("Credenciales incorrectas");
    }
}