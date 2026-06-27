package com.grupo4.SisHosp.payment;

import com.grupo4.SisHosp.exception.RecursoNaoPermitidoException;


public class FormaPagamentoFactory {

    public static FormaPagamento criar(String tipo) {
        if (tipo == null) {
            throw new RecursoNaoPermitidoException("Forma de pagamento não informada.");
        }

        switch (tipo.toUpperCase()) {
            case "PIX":
                return new PagamentoPix();
            case "CARTAO_CREDITO":
                return new PagamentoCartaoCredito();
            case "DINHEIRO":
                return new PagamentoDinheiro();
            default:
                throw new RecursoNaoPermitidoException(
                        "Forma de pagamento não suportada: " + tipo);
        }
    }
}
