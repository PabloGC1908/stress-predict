package com.pgc.stress_predict.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "encuesta")
public class Encuesta {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    private LocalDate fecha;
    @Column(name = "horas_estudio_dia")
    private Float horasEstudioDia;

    @Column(name = "horas_extracurricular_dia")
    private Float horasExtracurricularDia;

    @Column(name = "horas_sueno_dia")
    private Float horasSuenoDia;

    @Column(name = "horas_social_dia")
    private Float horasSocialDia;

    @Column(name = "horas_actividad_fisica")
    private Float horasActividadFisica;

    @Column(name = "promedio_calificaciones")
    private Float promedioCalificaciones;

    private String prediccion;

}
