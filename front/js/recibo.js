exigirLogin();
montarSidebar("historico");

const id = qs("id");
if (!id) window.location.href = "historico.html";

if (qs("nova") === "1" || document.referrer.includes("nova-reserva")) {
  document.getElementById("avisoOk").style.display = "flex";
}

document.getElementById("btnImprimir").addEventListener("click", () => window.print());
document.getElementById("btnCancelar").addEventListener("click", cancelar);
document.getElementById("btnPagar").addEventListener("click", pagar);

carregar();

async function carregar() {
  try {
    const a = await api.get("/alugueis/" + id);
    let pagamento = null;
    try { pagamento = await api.get("/alugueis/" + id + "/pagamento"); } catch (e) { pagamento = null; }

    const valorDiaria = a.qtdDiarias ? (a.valorTotal / a.qtdDiarias) : a.valorTotal;
    const endereco = a.residencia
      ? [[a.residencia.endereco, a.residencia.numero].filter(Boolean).join(", "), a.residencia.bairro, a.residencia.cidade].filter(Boolean).join(" – ")
      : "";

    const linhaPagamento = pagamento && pagamento.status === "PAGO"
      ? `<div class="recibo-linha"><span class="rotulo">Pagamento</span><span class="valor" style="color:var(--green,#2e7d32);">PAGO · ${rotuloForma(pagamento.forma)}</span></div>`
      : `<div class="recibo-linha"><span class="rotulo">Pagamento</span><span class="valor">PENDENTE</span></div>`;

    document.getElementById("conteudoRecibo").innerHTML = `
      <div class="recibo reveal">
        <div class="recibo-cabecalho">
          <h2>SisHosp Maraú</h2>
          <p>Formulário de Aluguel · Nº ${String(a.id).padStart(4, "0")}</p>
          ${a.residencia ? `<p>${a.residencia.nome}${endereco ? " – " + endereco : ""}</p>` : ""}
          <hr style="max-width:200px; margin:1rem auto;" />
        </div>
        <div class="recibo-linha"><span class="rotulo">Cliente</span><span class="valor">${a.cliente ? a.cliente.nome : "—"}</span></div>
        <div class="recibo-linha"><span class="rotulo">CPF</span><span class="valor">${a.cliente && a.cliente.cpf ? a.cliente.cpf : "—"}</span></div>
        <div class="recibo-linha"><span class="rotulo">Quarto</span><span class="valor">${a.quarto ? rotuloTipo(a.quarto.tipo) : "—"}</span></div>
        <div class="recibo-linha"><span class="rotulo">Hóspedes</span><span class="valor">${a.numHospedes}</span></div>
        <div class="recibo-linha"><span class="rotulo">Entrada</span><span class="valor">${fmtDataHora(a.dataEntrada)}</span></div>
        <div class="recibo-linha"><span class="rotulo">Saída</span><span class="valor">${fmtDataHora(a.dataSaida)}</span></div>
        <div class="recibo-linha"><span class="rotulo">Número de diárias</span><span class="valor">${a.qtdDiarias}</span></div>
        <div class="recibo-linha"><span class="rotulo">Valor da diária</span><span class="valor">${fmtMoeda(valorDiaria)}</span></div>
        ${linhaPagamento}
        ${a.status === "CANCELADO" ? `<div class="recibo-linha"><span class="rotulo">Situação</span><span class="valor" style="color:var(--red);">CANCELADO</span></div>` : ""}
        <div class="recibo-total"><span>Total</span><span>${fmtMoeda(a.valorTotal)}</span></div>

        <div class="assinaturas">
          <div class="assinatura">Assinatura do Responsável</div>
          <div class="assinatura">Assinatura do Cliente</div>
        </div>
      </div>`;

    document.getElementById("botoes").style.display = "flex";
    if (a.status === "CANCELADO") document.getElementById("btnCancelar").style.display = "none";
    const jaPago = pagamento && pagamento.status === "PAGO";
    if (a.status !== "CANCELADO" && !jaPago) {
      document.getElementById("areaPagamento").style.display = "flex";
    }
  } catch (e) {
    document.getElementById("conteudoRecibo").innerHTML = `<div class="aviso aviso-erro" style="max-width:640px; margin:0 auto;">${e.message}</div>`;
  }
}

function rotuloForma(forma) {
  return { PIX: "PIX", CARTAO_CREDITO: "Cartão de Crédito", DINHEIRO: "Dinheiro" }[forma] || forma;
}

async function pagar() {
  const forma = document.getElementById("selForma").value;
  try {
    const pag = await api.post("/alugueis/" + id + "/pagar", { forma });
    toast("Pagamento processado: " + (pag.descricao || forma), "ok");
    carregar();
  } catch (e) { toast(e.message, "err"); }
}

async function cancelar() {
  const ok = await confirmar("Cancelar reserva", "Deseja realmente cancelar esta reserva?");
  if (!ok) return;
  try {
    await api.put("/alugueis/" + id + "/cancelar");
    toast("Reserva cancelada.", "ok");
    carregar();
  } catch (e) { toast(e.message, "err"); }
}