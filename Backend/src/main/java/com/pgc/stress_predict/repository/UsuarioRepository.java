package com.pgc.stress_predict.repository;

import com.pgc.stress_predict.domain.models.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailAndPassword(String correo, String contraseña);
    Optional<Usuario> findByEmail(String correo);
}