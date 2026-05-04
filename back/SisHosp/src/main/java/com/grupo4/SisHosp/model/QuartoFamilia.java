package com.grupo4.SisHosp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("FAMILIA")
@Getter @Setter
@NoArgsConstructor
public class QuartoFamilia extends Quarto {

    private Integer capacidadeMaxima;
    private Integer numeroDeAmbientes;

    @Override
    public Double calcularDiaria() {
        return calcularDiaria(1);
    }

    public Double calcularDiaria(int numHospedes) {
        double percentualPorHospede = 0.10;
        double total = getValorBase() * (1 + (numHospedes * percentualPorHospede));

        // Desconto progressivo por grupo
        if (numHospedes >= 5) total *= 0.85;
        else if (numHospedes >= 3) total *= 0.92;

        return total;
    }
}