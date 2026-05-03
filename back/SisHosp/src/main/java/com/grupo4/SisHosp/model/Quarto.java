package com.grupo4.SisHosp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_quarto")
@Getter 
@Setter
@NoArgsConstructor


public abstract class Quarto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double valorBase;
    private Boolean possuiAR;
    private Boolean possuiHidro;

    @ManyToOne
    @JoinColumn(name = "residencia_id")

    private Residencia residencia;

    public abstract Double calcularDiaria();

    protected Double calcularAdicionaisComuns() {
        double adicionais = 0.0;
        if (Boolean.TRUE.equals(possuiAR))    adicionais += 30.0;
        if (Boolean.TRUE.equals(possuiHidro)) adicionais += 50.0;
        return adicionais;
    }
}
