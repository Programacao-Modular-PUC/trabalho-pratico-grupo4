package com.grupo4.SisHosp.payment;

public class PagamentoCartaoCredito implements FormaPagamento {

    @Override
    public String processar(Double valor) {
        return String.format(
                "Pagamento via Cartão de Crédito no valor de R$ %.2f autorizado em até 2 dias úteis.",
                valor);
    }

    @Override
    public String getNome() {
        return "CARTAO_CREDITO";
    }
}
