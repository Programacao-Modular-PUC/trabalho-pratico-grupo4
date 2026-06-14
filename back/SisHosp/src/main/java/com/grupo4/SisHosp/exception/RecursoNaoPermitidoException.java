package com.grupo4.SisHosp.exception;

public class RecursoNaoPermitidoException extends RuntimeException {
    public RecursoNaoPermitidoException(String mensagem) {
        super(mensagem);
    }
}