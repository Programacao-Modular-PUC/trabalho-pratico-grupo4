package com.grupo4.SisHosp.payment;

import com.grupo4.SisHosp.exception.RecursoNaoPermitidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormaPagamentoTest {

    @Test
    @DisplayName("Factory retorna a estrategia correta para cada tipo")
    void factoryRetornaEstrategiaCorreta() {
        assertEquals("PIX", FormaPagamentoFactory.criar("PIX").getNome());
        assertEquals("CARTAO_CREDITO", FormaPagamentoFactory.criar("CARTAO_CREDITO").getNome());
        assertEquals("DINHEIRO", FormaPagamentoFactory.criar("DINHEIRO").getNome());
    }

    @Test
    @DisplayName("Factory aceita tipo em qualquer caixa (maiuscula/minuscula)")
    void factoryIgnoraCaixa() {
        assertEquals("PIX", FormaPagamentoFactory.criar("pix").getNome());
    }

    @Test
    @DisplayName("Forma de pagamento invalida lanca excecao")
    void formaInvalidaLancaExcecao() {
        assertThrows(RecursoNaoPermitidoException.class,
                () -> FormaPagamentoFactory.criar("BITCOIN"));
    }

    @Test
    @DisplayName("Cada estrategia processa o valor e gera uma mensagem")
    void estrategiaProcessaValor() {
        FormaPagamento pix = new PagamentoPix();
        String msg = pix.processar(880.0);
        assertNotNull(msg);
        assertTrue(msg.contains("PIX"));
    }
}
