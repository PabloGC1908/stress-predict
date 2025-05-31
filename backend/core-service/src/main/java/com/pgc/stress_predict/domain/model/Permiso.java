package com.pgc.stress_predict.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "permiso")
public class Permiso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "permiso", nullable = false, unique = true, updatable = false)
    private String permiso;
}
