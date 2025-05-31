package com.pgc.stress_predict.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "encuesta")
public class Encuesta {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    private LocalDate fecha;
    @Column(name = "horas_estudio_dia")
    private Float horasEstudioDia;

    @Column(name = "horas_extracurricular_dia")
    private Float horasExtracurricularDia;

    @Column(name = "horas_sueno_dia")
    private Integer horasSuenoDia;

    @Column(name = "horas_social_dia")
    private Integer horasSocialDia;

    @Column(name = "horas_actividad_fisica")
    private Boolean horasActividadFisica;

    private String comentario;

}
