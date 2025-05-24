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
@Table(name = "analisis_estres")
public class AnalisisEstres {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Usuario usuario;

    private LocalDate fecha;

    @Column(name = "nivel_estres")
    private Integer nivelEstres;
    private String categoria;

    @Column(columnDefinition = "jsonb")
    private String factores;

    @Column(name = "modelo_usado")
    private String modeloUsado;
    private Float confianza;
    private String recomendacion;
}