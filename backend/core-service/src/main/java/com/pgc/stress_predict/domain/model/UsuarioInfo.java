package com.pgc.stress_predict.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "contacto")
public class UsuarioInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String apellido;
    private String telefono;

    @Column(unique = true)
    private Integer dni;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "horas_estudio_dia")
    private Float horasEstudioDia;

    @Column(name = "horas_extracurricular_dia")
    private Float horasExtracurricularDia;

    @Column(name = "horas_sueno_dia")
    private Float horasSuenoDia;

    @Column(name = "horas_social_dia")
    private Float horasSocialDia;

    @Column(name = "horas_actividad_fisica")
    private Float horasActividadFisicaDia;

    @Column(name = "promedio_calificaciones")
    private Float promedioCalificaciones;

    @OneToOne(mappedBy = "usuarioInfo")
    private Usuario usuario;

    @Override
    public String toString() {
        return "UsuarioInfo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", telefono='" + telefono + '\'' +
                ", dni=" + dni +
                ", fechaNacimiento=" + fechaNacimiento +
                ", horasEstudioDia=" + horasEstudioDia +
                ", horasExtracurricularDia=" + horasExtracurricularDia +
                ", horasSuenoDia=" + horasSuenoDia +
                ", horasSocialDia=" + horasSocialDia +
                ", horasActividadFisicaDia=" + horasActividadFisicaDia +
                ", promedioCalificaciones=" + promedioCalificaciones +
                ", usuario=" + usuario +
                '}';
    }
}
