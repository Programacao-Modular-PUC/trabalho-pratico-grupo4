package com.grupo4.SisHosp.payment;


public interface FormaPagamento {

    String processar(Double valor);
    String getNome();
    
}
