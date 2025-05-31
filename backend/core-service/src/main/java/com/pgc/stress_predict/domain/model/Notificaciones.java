package com.pgc.stress_predict.domain.models.model;

import com.pgc.stress_predict.domain.model.Usuario;
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
@Table(name = "notificaciones")
public class Notificaciones {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    private String tipo;

    private String mensaje;

    @Column(name = "enviada_en")
    private LocalDateTime enviadaEn = LocalDateTime.now();
}
