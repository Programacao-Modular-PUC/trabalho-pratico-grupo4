package com.grupo4.SisHosp.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AluguelDiariasTest {

    @Test
    @DisplayName("Entrada e saída no mesmo dia contam pelo menos 1 diária")
    void minimoUmaDiaria() {
        LocalDateTime entrada = LocalDateTime.of(2026, 1, 10, 14, 0);
        LocalDateTime saida = LocalDateTime.of(2026, 1, 10, 18, 0);

        assertEquals(1, Aluguel.calcularQtdDiarias(entrada, saida));
    }

    @Test
    @DisplayName("Duas noites com saída antes do meio-dia contam 2 diárias")
    void duasDiarias() {
        LocalDateTime entrada = LocalDateTime.of(2026, 1, 10, 10, 0);
        LocalDateTime saida = LocalDateTime.of(2026, 1, 12, 10, 0);

        assertEquals(2, Aluguel.calcularQtdDiarias(entrada, saida));
    }

    @Test
    @DisplayName("Saída após o meio-dia cobra uma diária extra")
    void saidaAposMeioDia() {
        LocalDateTime entrada = LocalDateTime.of(2026, 1, 10, 10, 0);
        LocalDateTime saida = LocalDateTime.of(2026, 1, 11, 13, 0);

        assertEquals(2, Aluguel.calcularQtdDiarias(entrada, saida));
    }

    @Test
    @DisplayName("Saída às 12h30 também cobra diária extra")
    void saidaDozeEMeia() {
        LocalDateTime entrada = LocalDateTime.of(2026, 1, 10, 10, 0);
        LocalDateTime saida = LocalDateTime.of(2026, 1, 11, 12, 30);

        assertEquals(2, Aluguel.calcularQtdDiarias(entrada, saida));
    }

    @Test
    @DisplayName("Saída exatamente ao meio-dia não cobra diária extra")
    void saidaMeioDiaCravado() {
        LocalDateTime entrada = LocalDateTime.of(2026, 1, 10, 10, 0);
        LocalDateTime saida = LocalDateTime.of(2026, 1, 11, 12, 0);

        assertEquals(1, Aluguel.calcularQtdDiarias(entrada, saida));
    }
}
