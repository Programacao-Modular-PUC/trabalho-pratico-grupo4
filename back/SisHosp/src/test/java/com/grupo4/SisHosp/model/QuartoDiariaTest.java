package com.grupo4.SisHosp.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class QuartoDiariaTest {

    private static final double DELTA = 0.001; 

    @Test
    @DisplayName("Individual: 1 cama, sem adicionais, retorna o valor base")
    void individualSemAdicionais() {
        QuartoIndividual quarto = new QuartoIndividual();
        quarto.setValorBase(100.0);
        quarto.setNumeroDeCamas(1);
        quarto.setPossuiAR(false);
        quarto.setPossuiHidro(false);

        assertEquals(100.0, quarto.calcularDiaria(), DELTA);
    }

    @Test
    @DisplayName("Individual: com ar-condicionado soma R$ 30")
    void individualComAr() {
        QuartoIndividual quarto = new QuartoIndividual();
        quarto.setValorBase(100.0);
        quarto.setNumeroDeCamas(1);
        quarto.setPossuiAR(true);
        quarto.setPossuiHidro(false);

        assertEquals(130.0, quarto.calcularDiaria(), DELTA);
    }

    @Test
    @DisplayName("Individual: cada cama extra soma R$ 40")
    void individualComCamasExtras() {
        QuartoIndividual quarto = new QuartoIndividual();
        quarto.setValorBase(100.0);
        quarto.setNumeroDeCamas(3); 
        quarto.setPossuiAR(false);
        quarto.setPossuiHidro(false);

        assertEquals(180.0, quarto.calcularDiaria(), DELTA);
    }


    @Test
    @DisplayName("Duplo: cama de casal soma R$ 20")
    void duploCasal() {
        QuartoDuplo quarto = new QuartoDuplo();
        quarto.setValorBase(200.0);
        quarto.setTipoCama(QuartoDuplo.TipoCama.CASAL);
        quarto.setPossuiAR(false);
        quarto.setPossuiHidro(false);

        assertEquals(220.0, quarto.calcularDiaria(), DELTA);
    }

    @Test
    @DisplayName("Duplo: cama queen/king soma R$ 60")
    void duploQueen() {
        QuartoDuplo quarto = new QuartoDuplo();
        quarto.setValorBase(200.0);
        quarto.setTipoCama(QuartoDuplo.TipoCama.QUEEN);
        quarto.setPossuiAR(false);
        quarto.setPossuiHidro(false);

        assertEquals(260.0, quarto.calcularDiaria(), DELTA);
    }

    @Test
    @DisplayName("Duplo: berço só é cobrado quando solicitado")
    void duploComBerco() {
        QuartoDuplo quarto = new QuartoDuplo();
        quarto.setValorBase(200.0);
        quarto.setTipoCama(QuartoDuplo.TipoCama.QUEEN);
        quarto.setPossuiBerco(true);
        quarto.setPossuiAR(false);
        quarto.setPossuiHidro(false);

        assertEquals(260.0, quarto.calcularDiaria(false), DELTA);
        assertEquals(290.0, quarto.calcularDiaria(true), DELTA);
    }


    @Test
    @DisplayName("Família: 2 hóspedes, sem desconto")
    void familiaSemDesconto() {
        QuartoFamilia quarto = new QuartoFamilia();
        quarto.setValorBase(300.0);
        quarto.setCapacidadeMaxima(6);

        assertEquals(360.0, quarto.calcularDiaria(2), DELTA);
    }

    @Test
    @DisplayName("Família: 3 hóspedes recebem 8% de desconto")
    void familiaDesconto8() {
        QuartoFamilia quarto = new QuartoFamilia();
        quarto.setValorBase(300.0);
        quarto.setCapacidadeMaxima(6);

        assertEquals(358.8, quarto.calcularDiaria(3), DELTA);
    }

    @Test
    @DisplayName("Família: 5 hóspedes recebem 15% de desconto")
    void familiaDesconto15() {
        QuartoFamilia quarto = new QuartoFamilia();
        quarto.setValorBase(300.0);
        quarto.setCapacidadeMaxima(6);

        assertEquals(382.5, quarto.calcularDiaria(5), DELTA);
    }
}
