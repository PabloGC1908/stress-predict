package com.pgc.stress_predict.domain.port.out;

import com.pgc.stress_predict.application.dto.response.HistorialUsuarioResponse;
import com.pgc.stress_predict.application.dto.response.PerfilUsuarioResponse;
import com.pgc.stress_predict.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String correo);

    @Query("SELECT u.id FROM Usuario u WHERE u.email=:email")
    Long findUsuarioByEmail(String email);

    @Query("""
        SELECT new com.pgc.stress_predict.application.dto.response.HistorialUsuarioResponse(
            h.fecha, h.horasEstudioDia, h.horasExtracurricularDia, h.horasSuenoDia,\s
            h.horasSocialDia, h.horasActividadFisica, h.promedioCalificaciones, h.prediccion
        )
        FROM Usuario u JOIN u.historial h\s
        WHERE u.id = :id
   \s""")
    List<HistorialUsuarioResponse> findHistorialUsuarioById(@Param("id") Long id);


    @Query("""
        SELECT new com.pgc.stress_predict.application.dto.response.PerfilUsuarioResponse(
            u_info.nombre, u_info.apellido, u_info.telefono, u_info.dni, u_info.fechaNacimiento, u_info.horasEstudioDia,
            u_info.horasExtracurricularDia, u_info.horasSuenoDia, u_info.horasEstudioDia, u_info.horasActividadFisicaDia,
            u_info.promedioCalificaciones
        )\s
        FROM Usuario u JOIN u.usuarioInfo u_info
        WHERE u.id = :id
   \s""")
    PerfilUsuarioResponse findPerfilById(@Param("id") Long id);
}