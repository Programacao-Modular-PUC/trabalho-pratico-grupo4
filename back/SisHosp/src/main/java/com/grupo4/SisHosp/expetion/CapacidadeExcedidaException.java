package com.grupo4.SisHosp.expetion;

public class CapacidadeExcedidaException extends RuntimeException {  
    public CapacidadeExcedidaException(String mensagem) {
        super(mensagem);
    }
}

