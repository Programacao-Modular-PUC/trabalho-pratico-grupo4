
exigirLogin();
montarSidebar("historico");

let alugueis = [];

document.getElementById("filtroResidencia").addEventListener("change", render);
document.getElementById("filtroStatus").addEventListener("change", render);
carregar();

async function carregar() {
  try {
    const [lista, residencias] = await Promise.all([api.get("/alugueis"), api.get("/residencias")]);
    alugueis = lista.sort((a, b) => new Date(b.dataEntrada) - new Date(a.dataEntrada));

    document.getElementById("filtroResidencia").innerHTML =
      `<option value="">Todas as residências</option>` +
      residencias.map(r => `<option value="${r.id}">${r.nome}</option>`).join("");

    render();
  } catch (e) {
    document.getElementById("tabela").innerHTML = `<tr><td colspan="10"><div class="aviso aviso-erro">${e.message}</div></td></tr>`;
  }
}

function render() {
  const fr = document.getElementById("filtroResidencia").value;
  const fs = document.getElementById("filtroStatus").value;

  const filtrados = alugueis.filter(a => {
    const okR = !fr || (a.residencia && a.residencia.id == fr);
    const status = a.status || "ATIVO";
    const okS = !fs || status === fs;
    return okR && okS;
  });

  const tb = document.getElementById("tabela");
  if (filtrados.length === 0) {
    tb.innerHTML = `<tr><td colspan="10" class="text-muted">Nenhum aluguel encontrado.</td></tr>`;
    return;
  }

  tb.innerHTML = filtrados.map(a => {
    const cancelado = a.status === "CANCELADO";
    return `
      <tr>
        <td>#${String(a.id).padStart(3, "0")}</td>
        <td>${a.cliente ? a.cliente.nome : "—"}</td>
        <td>${a.residencia ? a.residencia.nome : "—"}</td>
        <td>${a.quarto ? rotuloTipo(a.quarto.tipo) : "—"}</td>
        <td>${fmtDataHora(a.dataEntrada)}</td>
        <td>${fmtDataHora(a.dataSaida)}</td>
        <td>${a.qtdDiarias}</td>
        <td>${fmtMoeda(a.valorTotal)}</td>
        <td>${cancelado ? '<span class="badge badge-coral">Cancelado</span>' : '<span class="badge badge-verde">Ativo</span>'}</td>
        <td>
          <div class="acoes">
            <a href="recibo.html?id=${a.id}" class="btn btn-contorno btn-peq">Recibo</a>
            ${cancelado ? "" : `<button class="btn btn-perigo btn-peq" data-cancelar="${a.id}">Cancelar</button>`}
          </div>
        </td>
      </tr>`;
  }).join("");

  tb.querySelectorAll("[data-cancelar]").forEach(b =>
    b.addEventListener("click", () => cancelar(b.dataset.cancelar)));
}

async function cancelar(id) {
  const ok = await confirmar("Cancelar aluguel", "Deseja realmente cancelar este aluguel? O quarto voltará a ficar disponível.");
  if (!ok) return;
  try {
    await api.put("/alugueis/" + id + "/cancelar");
    toast("Aluguel cancelado.", "ok");
    carregar();
  } catch (e) { toast(e.message, "err"); }
}
