package com.grupo4.SisHosp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("INDIVIDUAL")
@Getter @Setter
@NoArgsConstructor
public class QuartoIndividual extends Quarto {

    @Column(nullable = false)
    private Integer numeroDeCamas = 1;

    @Override
    public Double calcularDiaria() {
        double base = getValorBase() + calcularAdicionaisComuns();
        if (numeroDeCamas <= 1) {
            return base;
        }
        return base + (numeroDeCamas - 1) * 40.0;
    }
}