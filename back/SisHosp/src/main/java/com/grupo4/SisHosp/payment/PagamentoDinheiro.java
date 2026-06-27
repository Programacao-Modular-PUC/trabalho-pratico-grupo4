package com.grupo4.SisHosp.payment;

public class PagamentoDinheiro implements FormaPagamento {

    @Override
    public String processar(Double valor) {
        return String.format(
                "Pagamento em Dinheiro no valor de R$ %.2f a ser realizado na recepção.",
                valor);
    }

    @Override
    public String getNome() {
        return "DINHEIRO";
    }
}
