exigirLogin();
montarSidebar("reserva");

let residencias = [];
let quartoSelecionado = null;

const elResidencia = document.getElementById("selResidencia");
const elQuartos = document.getElementById("quartos");
const elCliente = document.getElementById("selCliente");
const elEntrada = document.getElementById("entrada");
const elSaida = document.getElementById("saida");
const elHospedes = document.getElementById("numHospedes");
const elBerco = document.getElementById("berco");
const elLinhaBerco = document.getElementById("linhaBerco");
const elResumo = document.getElementById("resumo");
const btn = document.getElementById("btnConfirmar");

[elEntrada, elSaida, elHospedes, elBerco].forEach(el => el.addEventListener("input", atualizarResumo));
elResidencia.addEventListener("change", () => carregarQuartos(elResidencia.value));
btn.addEventListener("click", confirmar);

inicializar();

async function inicializar() {
  const hoje = new Date(); hoje.setHours(12, 0, 0, 0);
  const amanha = new Date(hoje); amanha.setDate(amanha.getDate() + 1);
  elEntrada.value = paraInputLocal(hoje);
  elSaida.value = paraInputLocal(amanha);

  try {
    const [resids, clientes] = await Promise.all([api.get("/residencias"), api.get("/clientes")]);
    residencias = resids;

    elResidencia.innerHTML = `<option value="">Selecione…</option>` +
      resids.map(r => `<option value="${r.id}">${r.nome}</option>`).join("");

    elCliente.innerHTML = clientes.length
      ? `<option value="">Selecione…</option>` + clientes.map(c => `<option value="${c.id}">${c.nome}${c.cpf ? " — " + c.cpf : ""}</option>`).join("")
      : `<option value="">Nenhum cliente cadastrado</option>`;
    elCliente.addEventListener("change", atualizarResumo);

    const residenciaId = qs("residenciaId");
    const quartoId = qs("quartoId");
    if (residenciaId) {
      elResidencia.value = residenciaId;
      await carregarQuartos(residenciaId, quartoId);
    }
  } catch (e) {
    toast(e.message, "err");
  }
}

async function carregarQuartos(residenciaId, preSelecionar) {
  quartoSelecionado = null;
  if (!residenciaId) { elQuartos.innerHTML = `<p class="text-muted">Selecione uma residência.</p>`; atualizarResumo(); return; }

  const r = residencias.find(x => x.id == residenciaId) || await api.get("/residencias/" + residenciaId);
  const quartos = r.quartos || [];

  if (quartos.length === 0) {
    elQuartos.innerHTML = `<div class="aviso aviso-info">Esta residência não tem quartos cadastrados.</div>`;
    atualizarResumo();
    return;
  }

  elQuartos.innerHTML = quartos.map(q => cardQuarto(q)).join("");
  elQuartos.querySelectorAll("[data-quarto]").forEach(card =>
    card.addEventListener("click", () => selecionarQuarto(quartos.find(q => q.id == card.dataset.quarto), card)));

  if (preSelecionar) {
    const card = elQuartos.querySelector(`[data-quarto="${preSelecionar}"]`);
    if (card) selecionarQuarto(quartos.find(q => q.id == preSelecionar), card);
  }
}

function cardQuarto(q) {
  let extra = "";
  if (q.tipo === "INDIVIDUAL") extra = `${q.numeroDeCamas} cama(s)`;
  if (q.tipo === "DUPLO") extra = `Cama ${q.tipoCama}${q.possuiBerco ? " · berço disponível" : ""}`;
  if (q.tipo === "FAMILIA") extra = `Até ${q.capacidadeMaxima} hóspedes`;
  return `
    <div class="quarto-card clicavel mb-1" data-quarto="${q.id}" style="margin-bottom:.7rem;">
      <div style="display:flex; justify-content:space-between; align-items:center;">
        <div class="quarto-tipo">Quarto ${rotuloTipo(q.tipo)}</div>
        <span class="badge badge-cinza">${extra}</span>
      </div>
      <div class="quarto-preco" style="font-size:1.15rem;">${fmtMoeda(q.valorBase)} <small>/ diária base</small></div>
    </div>`;
}

function selecionarQuarto(quarto, card) {
  quartoSelecionado = quarto;
  elQuartos.querySelectorAll(".quarto-card").forEach(c => c.classList.remove("selecionado"));
  card.classList.add("selecionado");

  const permiteBerco = quarto.tipo === "DUPLO" && quarto.possuiBerco;
  elLinhaBerco.style.display = permiteBerco ? "flex" : "none";
  if (!permiteBerco) elBerco.checked = false;

  atualizarResumo();
}

function atualizarResumo() {
  const temTudo = quartoSelecionado && elEntrada.value && elSaida.value && elCliente.value;
  btn.disabled = !temTudo;

  if (!quartoSelecionado || !elEntrada.value || !elSaida.value) {
    elResumo.innerHTML = `<p class="text-muted">Selecione um quarto e o período.</p>`;
    return;
  }

  const entrada = elEntrada.value, saida = elSaida.value;
  if (new Date(saida) <= new Date(entrada)) {
    elResumo.innerHTML = `<div class="aviso aviso-erro">A saída deve ser depois da entrada.</div>`;
    btn.disabled = true;
    return;
  }

  const hospedes = parseInt(elHospedes.value) || 1;
  const berco = elBerco.checked;
  const diarias = calcularDiarias(entrada, saida);
  const valorDiaria = calcularDiaria(quartoSelecionado, hospedes, berco);
  const total = valorDiaria * diarias;

  elResumo.innerHTML = `
    <div class="recibo-linha"><span class="rotulo">Quarto</span><span class="valor">${rotuloTipo(quartoSelecionado.tipo)}</span></div>
    <div class="recibo-linha"><span class="rotulo">Entrada</span><span class="valor">${fmtDataHora(entrada)}</span></div>
    <div class="recibo-linha"><span class="rotulo">Saída</span><span class="valor">${fmtDataHora(saida)}</span></div>
    <div class="recibo-linha"><span class="rotulo">Diárias</span><span class="valor">${diarias}</span></div>
    <div class="recibo-linha"><span class="rotulo">Valor da diária</span><span class="valor">${fmtMoeda(valorDiaria)}</span></div>
    <div class="recibo-total"><span>Total estimado</span><span>${fmtMoeda(total)}</span></div>
    <p class="text-muted" style="font-size:.78rem; margin-top:.5rem;">* O valor final é confirmado pelo servidor ao salvar.</p>`;
}

async function confirmar() {
  const corpo = {
    clienteId: parseInt(elCliente.value),
    quartoId: quartoSelecionado.id,
    dataEntrada: elEntrada.value.length === 16 ? elEntrada.value + ":00" : elEntrada.value,
    dataSaida: elSaida.value.length === 16 ? elSaida.value + ":00" : elSaida.value,
    numHospedes: parseInt(elHospedes.value) || 1,
    solicitouBerco: elBerco.checked,
  };

  btn.disabled = true; btn.innerHTML = '<span class="spinner"></span> Confirmando…';
  try {
    const aluguel = await api.post("/alugueis", corpo);
    toast("Reserva confirmada!", "ok");
    setTimeout(() => window.location.href = "recibo.html?id=" + aluguel.id + "&nova=1", 700);
  } catch (e) {
    toast(e.message, "err");
    btn.disabled = false; btn.textContent = "Confirmar reserva";
  }
}

function paraInputLocal(d) {
  const p = (n) => String(n).padStart(2, "0");
  return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())}T${p(d.getHours())}:${p(d.getMinutes())}`;
}