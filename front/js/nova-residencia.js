exigirLogin();
montarSidebar("residencias");

const idEdicao = qs("id");
let contador = 0;

document.getElementById("btnAddQuarto").addEventListener("click", () => adicionarBlocoQuarto());
document.getElementById("btnSalvar").addEventListener("click", salvar);

if (idEdicao) {
  carregarParaEdicao(idEdicao);
} else {
  adicionarBlocoQuarto(); 
}

async function carregarParaEdicao(id) {
  document.getElementById("titulo").textContent = "Editar residência";
  document.getElementById("bcTitulo").textContent = "Editar";
  try {
    const r = await api.get("/residencias/" + id);
    ["nome","endereco","numero","bairro","cep","cidade","telefone","email"].forEach(c => {
      if (r[c] != null) document.getElementById(c).value = r[c];
    });
    mostrarQuartosExistentes(r.quartos || []);
  } catch (e) {
    toast(e.message, "err");
  }
}

function mostrarQuartosExistentes(quartos) {
  const bloco = document.getElementById("blocoExistentes");
  const cont = document.getElementById("quartosExistentes");
  if (quartos.length === 0) { bloco.style.display = "none"; return; }
  bloco.style.display = "block";
  cont.innerHTML = quartos.map(q => `
    <div class="gap" style="justify-content:space-between; padding:.6rem .9rem; border:1px solid var(--line); border-radius:11px; margin-bottom:.5rem;">
      <span><strong>${rotuloTipo(q.tipo)}</strong> · ${fmtMoeda(q.valorBase)} / diária base · diária ${fmtMoeda(calcularDiaria(q, 2, false))}</span>
      <button class="btn btn-perigo btn-peq" data-del-quarto="${q.id}">Remover</button>
    </div>`).join("");
  cont.querySelectorAll("[data-del-quarto]").forEach(b =>
    b.addEventListener("click", () => removerQuartoExistente(b.dataset.delQuarto)));
}

async function removerQuartoExistente(idQuarto) {
  const ok = await confirmar("Remover quarto", "Deseja remover este quarto da residência?");
  if (!ok) return;
  try {
    await api.del("/quartos/" + idQuarto);
    toast("Quarto removido.", "ok");
    carregarParaEdicao(idEdicao);
  } catch (e) { toast(e.message, "err"); }
}

function adicionarBlocoQuarto() {
  const id = "q" + (contador++);
  const div = document.createElement("div");
  div.className = "card mb-2";
  div.style.background = "var(--surface-2)";
  div.dataset.bloco = id;
  div.innerHTML = `
    <div class="campos-3">
      <div class="campo">
        <label>Tipo do quarto</label>
        <select data-campo="tipo">
          <option value="INDIVIDUAL">Individual</option>
          <option value="DUPLO">Duplo</option>
          <option value="FAMILIA">Família</option>
        </select>
      </div>
      <div class="campo">
        <label>Valor base da diária (R$)</label>
        <input type="number" data-campo="valorBase" placeholder="180.00" min="0" step="0.01" />
      </div>
      <div class="campo" style="display:flex; align-items:flex-end;">
        <button class="btn btn-contorno btn-peq" data-remover style="width:100%;">Remover quarto</button>
      </div>
    </div>
    <div data-especifico></div>
    <div class="check-linha"><input type="checkbox" data-campo="possuiAR" id="${id}ar"/><label for="${id}ar">Ar-condicionado (+R$ 30,00)</label></div>
    <div class="check-linha"><input type="checkbox" data-campo="possuiHidro" id="${id}hidro"/><label for="${id}hidro">Hidromassagem (+R$ 50,00)</label></div>
  `;
  document.getElementById("quartosNovos").appendChild(div);

  const select = div.querySelector('[data-campo="tipo"]');
  select.addEventListener("change", () => renderEspecifico(div, select.value));
  renderEspecifico(div, select.value);

  div.querySelector("[data-remover]").addEventListener("click", () => div.remove());
}

function renderEspecifico(div, tipo) {
  const alvo = div.querySelector("[data-especifico]");
  if (tipo === "INDIVIDUAL") {
    alvo.innerHTML = `
      <div class="campo"><label>Número de camas</label>
        <input type="number" data-campo="numeroDeCamas" value="1" min="1" /></div>`;
  } else if (tipo === "DUPLO") {
    alvo.innerHTML = `
      <div class="campos-2">
        <div class="campo"><label>Tipo de cama</label>
          <select data-campo="tipoCama"><option value="CASAL">Casal (+R$ 20)</option><option value="QUEEN">Queen (+R$ 60)</option><option value="KING">King (+R$ 60)</option></select>
        </div>
        <div class="campo" style="justify-content:center;">
          <div class="check-linha" style="margin-top:1.6rem;"><input type="checkbox" data-campo="possuiBerco"/><label>Possui berço disponível</label></div>
        </div>
      </div>`;
  } else if (tipo === "FAMILIA") {
    alvo.innerHTML = `
      <div class="campos-2">
        <div class="campo"><label>Capacidade máxima (hóspedes)</label><input type="number" data-campo="capacidadeMaxima" value="4" min="1" /></div>
        <div class="campo"><label>Número de ambientes</label><input type="number" data-campo="numeroDeAmbientes" value="2" min="1" /></div>
      </div>`;
  }
}

function lerQuarto(div) {
  const v = (c) => { const el = div.querySelector(`[data-campo="${c}"]`); return el ? el.value : null; };
  const chk = (c) => { const el = div.querySelector(`[data-campo="${c}"]`); return el ? el.checked : false; };
  const tipo = v("tipo");
  const base = {
    valorBase: parseFloat(v("valorBase")),
    possuiAR: chk("possuiAR"),
    possuiHidro: chk("possuiHidro"),
  };
  if (isNaN(base.valorBase)) return { erro: "Informe o valor base de todos os quartos." };

  if (tipo === "INDIVIDUAL") return { tipo, corpo: { ...base, numeroDeCamas: parseInt(v("numeroDeCamas")) || 1 } };
  if (tipo === "DUPLO")      return { tipo, corpo: { ...base, tipoCama: v("tipoCama"), possuiBerco: chk("possuiBerco") } };
  if (tipo === "FAMILIA")    return { tipo, corpo: { ...base, capacidadeMaxima: parseInt(v("capacidadeMaxima")) || 1, numeroDeAmbientes: parseInt(v("numeroDeAmbientes")) || 1 } };
}

async function salvar() {
  const residencia = {};
  ["nome","endereco","numero","bairro","cep","cidade","telefone","email"].forEach(c => {
    residencia[c] = document.getElementById(c).value.trim();
  });

  if (!residencia.nome) { toast("Informe o nome da residência.", "err"); return; }

  const blocos = [...document.querySelectorAll("[data-bloco]")];
  const quartos = [];
  for (const b of blocos) {
    const q = lerQuarto(b);
    if (q.erro) { toast(q.erro, "err"); return; }
    quartos.push(q);
  }

  const btn = document.getElementById("btnSalvar");
  btn.disabled = true; btn.innerHTML = '<span class="spinner"></span> Salvando…';

  try {
    let residenciaSalva;
    if (idEdicao) {
      residenciaSalva = await api.put("/residencias/" + idEdicao, residencia);
    } else {
      residenciaSalva = await api.post("/residencias", residencia);
    }

    for (const q of quartos) {
      const corpo = { ...q.corpo, residencia: { id: residenciaSalva.id } };
      await api.post("/quartos/" + q.tipo.toLowerCase(), corpo);
    }

    toast("Residência salva com sucesso!", "ok");
    setTimeout(() => window.location.href = "detalhe-residencia.html?id=" + residenciaSalva.id, 700);
  } catch (e) {
    toast(e.message, "err");
    btn.disabled = false; btn.textContent = "Salvar residência";
  }
}