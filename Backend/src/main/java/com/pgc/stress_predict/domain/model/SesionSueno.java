package com.pgc.stress_predict.domain.models.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sesion_sueno")
public class SesionSueno {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;
    private String calidad;
    private String fuente;
    private Integer movimientos;

    @Column(name = "frecuencia_cardiaca_promedio")
    private Integer frecuenciaCardiacaPromedio;
    private Integer despertares;
}
