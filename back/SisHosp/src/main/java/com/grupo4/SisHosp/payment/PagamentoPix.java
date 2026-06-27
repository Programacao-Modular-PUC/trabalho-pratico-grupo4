package com.grupo4.SisHosp.payment;

public class PagamentoPix implements FormaPagamento {

    @Override
    public String processar(Double valor) {
        String codigo = "PIX-" + System.currentTimeMillis();
        return String.format(
                "Pagamento via PIX no valor de R$ %.2f aprovado. Código: %s",
                valor, codigo);
    }

    @Override
    public String getNome() {
        return "PIX";
    }
}
