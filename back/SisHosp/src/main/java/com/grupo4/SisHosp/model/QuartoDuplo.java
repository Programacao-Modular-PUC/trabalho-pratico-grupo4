package com.grupo4.SisHosp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("DUPLO")
@Getter @Setter
@NoArgsConstructor
public class QuartoDuplo extends Quarto {

    @Enumerated(EnumType.STRING)
    private TipoCama tipoCama;

    private Boolean possuiBerco = false;

    @Override
    public Double calcularDiaria() {
        return calcularDiaria(false);
    }

    public Double calcularDiaria(boolean solicitouBerco) {
        double total = getValorBase() + calcularAdicionaisComuns();

        if (tipoCama == TipoCama.QUEEN || tipoCama == TipoCama.KING) {
            total += 60.0;
        } else {
            total += 20.0;
        }

        if (Boolean.TRUE.equals(possuiBerco) && solicitouBerco) {
            total += 30.0;
        }

        return total;
    }

    @Override
    public int getCapacidade() {
        return 2;
    }

    @Override
    public boolean permiteBerco() {
        
        return Boolean.TRUE.equals(possuiBerco);
    }

    public enum TipoCama {
        CASAL, QUEEN, KING
    }

    @Override
    public String getTipo() {
        return "DUPLO";
    }
}