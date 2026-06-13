package com.grupo4.SisHosp.expetion;

public class DataInvalidaException extends RuntimeException {
    public DataInvalidaException(String mensagem) {
        super(mensagem);
    }
}
