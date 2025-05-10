package com.pgc.stress_predict.controller;

import com.pgc.stress_predict.model.Usuario;
import com.pgc.stress_predict.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/registro")
    public String registrar(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            return "Correo ya registrado";
        }
        usuarioRepository.save(usuario);
        return "Usuario registrado exitosamente";
    }

    @PostMapping("/login")
    public String login(@RequestBody Usuario usuario) {
        return usuarioRepository.findByCorreoAndContraseña(usuario.getCorreo(), usuario.getContraseña())
                .map(u -> "Login exitoso")
                .orElse("Credenciales incorrectas");
    }
}