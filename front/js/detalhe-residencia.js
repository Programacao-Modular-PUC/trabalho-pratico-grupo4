
exigirLogin();
montarSidebar("residencias");

const id = qs("id");
if (!id) { window.location.href = "residencias.html"; }
document.getElementById("btnReservar").href = "nova-reserva.html?residenciaId=" + id;

carregar();

async function carregar() {
  try {
    const r = await api.get("/residencias/" + id);
    document.getElementById("nome").textContent = r.nome;
    document.getElementById("bcNome").textContent = r.nome;
    const partes = [
      [r.endereco, r.numero].filter(Boolean).join(", "),
      r.bairro, r.cidade, r.telefone, r.email
    ].filter(Boolean);
    document.getElementById("endereco").textContent = partes.join("  ·  ");

    montarQuartos(r.quartos || []);
    montarHistorico();
  } catch (e) {
    toast(e.message, "err");
  }
}

function montarQuartos(quartos) {
  const cont = document.getElementById("quartos");
  if (quartos.length === 0) {
    cont.innerHTML = `<div class="vazio"><div class="icone">🛏️</div>Nenhum quarto cadastrado. Edite a residência para adicionar.</div>`;
    return;
  }
  cont.innerHTML = quartos.map((q, i) => {
    const amen = [];
    if (q.possuiAR) amen.push('<span class="badge badge-azul">Ar-condicionado</span>');
    if (q.possuiHidro) amen.push('<span class="badge badge-azul">Hidromassagem</span>');
    if (q.tipo === "DUPLO" && q.possuiBerco) amen.push('<span class="badge badge-amber">Berço</span>');
    if (amen.length === 0) amen.push('<span class="badge badge-cinza">Sem adicionais</span>');

    let extra = "";
    if (q.tipo === "INDIVIDUAL") extra = `${q.numeroDeCamas} cama(s)`;
    if (q.tipo === "DUPLO") extra = `Cama ${q.tipoCama}`;
    if (q.tipo === "FAMILIA") extra = `Até ${q.capacidadeMaxima} hóspedes`;

    return `
      <div class="quarto-card reveal" style="animation-delay:${i * 0.05}s">
        <div style="display:flex; justify-content:space-between; align-items:center;">
          <div class="quarto-tipo">Quarto ${rotuloTipo(q.tipo)}</div>
          <span class="badge badge-cinza">${extra}</span>
        </div>
        <div class="quarto-preco">${fmtMoeda(q.valorBase)} <small>/ diária base</small></div>
        <div class="amenidades">${amen.join("")}</div>
        <hr />
        <div class="acoes">
          <a href="nova-reserva.html?residenciaId=${id}&quartoId=${q.id}" class="btn btn-primario btn-peq">Reservar</a>
          <button class="btn btn-perigo btn-peq" data-del="${q.id}">Remover</button>
        </div>
      </div>`;
  }).join("");

  cont.querySelectorAll("[data-del]").forEach(b =>
    b.addEventListener("click", () => removerQuarto(b.dataset.del)));
}

async function removerQuarto(idQuarto) {
  const ok = await confirmar("Remover quarto", "Deseja remover este quarto?");
  if (!ok) return;
  try {
    await api.del("/quartos/" + idQuarto);
    toast("Quarto removido.", "ok");
    carregar();
  } catch (e) { toast(e.message, "err"); }
}

async function montarHistorico() {
  const tb = document.getElementById("historico");
  try {
    const alugueis = await api.get("/alugueis/residencia/" + id);
    if (alugueis.length === 0) {
      tb.innerHTML = `<tr><td colspan="7" class="text-muted">Nenhum aluguel registrado.</td></tr>`;
      return;
    }
    tb.innerHTML = alugueis
      .sort((a, b) => new Date(b.dataEntrada) - new Date(a.dataEntrada))
      .map(a => `
        <tr>
          <td>${a.cliente ? a.cliente.nome : "—"}</td>
          <td>${a.quarto ? rotuloTipo(a.quarto.tipo) : "—"}</td>
          <td>${fmtDataHora(a.dataEntrada)}</td>
          <td>${fmtDataHora(a.dataSaida)}</td>
          <td>${a.qtdDiarias}</td>
          <td>${fmtMoeda(a.valorTotal)}</td>
          <td>${badgeStatus(a.status)}</td>
        </tr>`).join("");
  } catch (e) {
    tb.innerHTML = `<tr><td colspan="7" class="aviso aviso-erro">${e.message}</td></tr>`;
  }
}

function badgeStatus(status) {
  return status === "CANCELADO"
    ? '<span class="badge badge-coral">Cancelado</span>'
    : '<span class="badge badge-verde">Ativo</span>';
}
