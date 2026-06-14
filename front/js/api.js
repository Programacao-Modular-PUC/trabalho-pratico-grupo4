
const API_BASE = "http://localhost:8080";



async function apiRequest(metodo, caminho, corpo) {
  const opcoes = {
    method: metodo,
    headers: { "Content-Type": "application/json" },
  };
  if (corpo !== undefined) opcoes.body = JSON.stringify(corpo);

  let resposta;
  try {
    resposta = await fetch(API_BASE + caminho, opcoes);
  } catch (e) {
    throw new Error("Não foi possível conectar ao servidor. O backend está rodando em " + API_BASE + "?");
  }


  if (resposta.status === 204) return null;

  const texto = await resposta.text();
  const dados = texto ? JSON.parse(texto) : null;

  if (!resposta.ok) {
    const msg = (dados && dados.mensagem) ? dados.mensagem : "Erro " + resposta.status;
    throw new Error(msg);
  }
  return dados;
}

const api = {
  get:  (c) => apiRequest("GET", c),
  post: (c, b) => apiRequest("POST", c, b),
  put:  (c, b) => apiRequest("PUT", c, b),
  del:  (c) => apiRequest("DELETE", c),
};


function fmtMoeda(valor) {
  if (valor == null || isNaN(valor)) return "R$ 0,00";
  return valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
}

function fmtDataHora(iso) {
  if (!iso) return "—";
  const d = new Date(iso);
  return d.toLocaleString("pt-BR", { day: "2-digit", month: "2-digit", year: "numeric", hour: "2-digit", minute: "2-digit" });
}

function fmtData(iso) {
  if (!iso) return "—";
  const d = new Date(iso);
  return d.toLocaleDateString("pt-BR");
}

function qs(nome) {
  return new URLSearchParams(window.location.search).get(nome);
}


function toast(mensagem, tipo) {
  let area = document.getElementById("toasts");
  if (!area) {
    area = document.createElement("div");
    area.id = "toasts";
    document.body.appendChild(area);
  }
  const t = document.createElement("div");
  t.className = "toast " + (tipo === "ok" ? "ok" : tipo === "err" ? "err" : "");
  t.textContent = mensagem;
  area.appendChild(t);
  setTimeout(() => t.remove(), 3500);
}


function confirmar(titulo, texto) {
  return new Promise((resolve) => {
    const fundo = document.createElement("div");
    fundo.className = "modal-fundo aberto";
    fundo.innerHTML = `
      <div class="modal">
        <h2>${titulo}</h2>
        <p>${texto}</p>
        <div class="acoes" style="justify-content:flex-end;">
          <button class="btn btn-contorno" data-acao="nao">Cancelar</button>
          <button class="btn btn-perigo" data-acao="sim">Confirmar</button>
        </div>
      </div>`;
    document.body.appendChild(fundo);
    fundo.addEventListener("click", (e) => {
      if (e.target === fundo || e.target.dataset.acao === "nao") { fundo.remove(); resolve(false); }
      if (e.target.dataset.acao === "sim") { fundo.remove(); resolve(true); }
    });
  });
}


function fazerLogin(email) {
  localStorage.setItem("sishospLogado", email || "operador@sishosp.com");
}
function logout() {
  localStorage.removeItem("sishospLogado");
  window.location.href = "login.html";
}
function usuarioLogado() {
  return localStorage.getItem("sishospLogado");
}
function exigirLogin() {
  if (!usuarioLogado()) window.location.href = "login.html";
}


const ICONES = {
  dashboard: '<path d="M3 3h7v7H3zM14 3h7v4h-7zM14 10h7v11h-7zM3 14h7v7H3z"/>',
  casa:      '<path d="M3 11l9-8 9 8M5 10v10h14V10"/>',
  reserva:   '<rect x="3" y="5" width="18" height="16" rx="2"/><path d="M16 3v4M8 3v4M3 11h18M12 14v4M10 16h4"/>',
  historico: '<circle cx="12" cy="12" r="9"/><path d="M12 7v5l3 2"/>',
  clientes:  '<path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75"/>',
  sair:      '<path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4M16 17l5-5-5-5M21 12H9"/>',
  onda:      '<path d="M2 12c2-3 4-3 6 0s4 3 6 0 4-3 6 0M2 17c2-3 4-3 6 0s4 3 6 0 4-3 6 0"/>',
};

function svg(nome) {
  return `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">${ICONES[nome]}</svg>`;
}

function montarSidebar(ativo) {
  const barra = document.getElementById("barra");
  if (!barra) return;

  const links = [
    { id: "dashboard", url: "dashboard.html", icone: "dashboard", texto: "Dashboard" },
    { id: "residencias", url: "residencias.html", icone: "casa", texto: "Residências" },
    { id: "reserva", url: "nova-reserva.html", icone: "reserva", texto: "Nova Reserva" },
    { id: "historico", url: "historico.html", icone: "historico", texto: "Histórico" },
    { id: "clientes", url: "clientes.html", icone: "clientes", texto: "Clientes" },
  ];

  barra.innerHTML = `
    <div class="marca">
      <div class="logo">${svg("onda")}</div>
      <div class="nome">SisHosp<small>Maraú · BA</small></div>
    </div>
    <div class="secao-label">Menu</div>
    <nav>
      ${links.map(l => `
        <a href="${l.url}" class="${l.id === ativo ? "ativo" : ""}">
          ${svg(l.icone)}<span class="txt">${l.texto}</span>
        </a>`).join("")}
    </nav>
    <div class="rodape">
      <div>Logado como<br><strong style="color:#fff;">${usuarioLogado() || "—"}</strong></div>
      <a href="#" class="sair" id="btnSair">${svg("sair")}<span class="txt">Sair</span></a>
    </div>`;

  const btn = document.getElementById("btnSair");
  if (btn) btn.addEventListener("click", (e) => { e.preventDefault(); logout(); });
}

function calcularDiaria(quarto, numHospedes, solicitouBerco) {
  let adicionais = 0;
  if (quarto.possuiAR) adicionais += 30;
  if (quarto.possuiHidro) adicionais += 50;

  if (quarto.tipo === "INDIVIDUAL") {
    const base = quarto.valorBase + adicionais;
    const camas = quarto.numeroDeCamas || 1;
    return camas <= 1 ? base : base + (camas - 1) * 40;
  }
  if (quarto.tipo === "DUPLO") {
    let total = quarto.valorBase + adicionais;
    total += (quarto.tipoCama === "QUEEN" || quarto.tipoCama === "KING") ? 60 : 20;
    if (quarto.possuiBerco && solicitouBerco) total += 30;
    return total;
  }
  if (quarto.tipo === "FAMILIA") {
    const n = numHospedes || 1;
    let total = quarto.valorBase * (1 + n * 0.10);
    if (n >= 5) total *= 0.85;
    else if (n >= 3) total *= 0.92;
    return total;
  }
  return quarto.valorBase || 0;
}

function calcularDiarias(entradaISO, saidaISO) {
  const e = new Date(entradaISO), s = new Date(saidaISO);
  const umDia = 1000 * 60 * 60 * 24;
  let dias = Math.floor((s.setHours(0,0,0,0) - new Date(entradaISO).setHours(0,0,0,0)) / umDia);
  const saida = new Date(saidaISO);
  if (saida.getHours() > 12 || (saida.getHours() === 12 && saida.getMinutes() > 0)) dias++;
  return Math.max(1, dias);
}

function rotuloTipo(tipo) {
  return { INDIVIDUAL: "Individual", DUPLO: "Duplo", FAMILIA: "Família" }[tipo] || tipo;
}
