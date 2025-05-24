package com.pgc.stress_predict.domain.models.model;

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
    @Column(name = "estado_animo")
    private String estadoAnimo;

    @Column(name = "horas_sueno")
    private Float horasSueno;

    @Column(name = "carga_academica")
    private Integer cargaAcademica;

    @Column(name = "estres_percibido")
    private Integer estresPercibido;

    @Column(name = "actividad_fisica")
    private Boolean actividadFisica;

    @Column(name = "alimentacion_saludable")
    private Boolean alimentacionSaludable;

    private String comentario;

}
