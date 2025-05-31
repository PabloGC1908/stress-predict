package com.pgc.stress_predict.domain.port.out;

import com.pgc.stress_predict.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailAndPassword(String correo, String contrasena);
    Optional<Usuario> findByEmail(String correo);

    @Query("SELECT u.id FROM Usuario u WHERE u.email=:email")
    Long findUsuarioByEmail(String email);
}