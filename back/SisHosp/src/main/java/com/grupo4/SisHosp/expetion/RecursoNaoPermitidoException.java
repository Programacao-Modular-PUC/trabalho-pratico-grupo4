package com.grupo4.SisHosp.expetion;

public class RecursoNaoPermitidoException extends RuntimeException {
    public RecursoNaoPermitidoException(String mensagem) {
        super(mensagem);
    }
}