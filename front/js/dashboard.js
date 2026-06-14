
exigirLogin();
montarSidebar("dashboard");
carregar();

async function carregar() {
  try {
    const [residencias, quartos, clientes, alugueis] = await Promise.all([
      api.get("/residencias"),
      api.get("/quartos"),
      api.get("/clientes"),
      api.get("/alugueis"),
    ]);

    const ativos = alugueis.filter(a => a.status !== "CANCELADO");

    document.getElementById("stResidencias").textContent = residencias.length;
    document.getElementById("stQuartos").textContent = quartos.length;
    document.getElementById("stAlugueis").textContent = ativos.length;
    document.getElementById("stClientes").textContent = clientes.length;

    montarProximas(ativos);
    montarOcupacao(residencias, ativos);
  } catch (e) {
    toast(e.message, "err");
  }
}

function montarProximas(ativos) {
  const agora = new Date();
  const proximas = ativos
    .filter(a => new Date(a.dataEntrada) >= agora)
    .sort((a, b) => new Date(a.dataEntrada) - new Date(b.dataEntrada))
    .slice(0, 6);

  const tb = document.getElementById("tbProximas");
  if (proximas.length === 0) {
    tb.innerHTML = `<tr><td colspan="4" class="text-muted">Nenhuma entrada futura agendada.</td></tr>`;
    return;
  }
  tb.innerHTML = proximas.map(a => `
    <tr>
      <td>${a.cliente ? a.cliente.nome : "—"}</td>
      <td>${a.quarto ? rotuloTipo(a.quarto.tipo) : "—"}</td>
      <td>${fmtDataHora(a.dataEntrada)}</td>
      <td><span class="badge badge-azul">Reservado</span></td>
    </tr>`).join("");
}

function montarOcupacao(residencias, ativos) {
  const agora = new Date();
  const tb = document.getElementById("tbOcupacao");

  if (residencias.length === 0) {
    tb.innerHTML = `<tr><td colspan="4" class="text-muted">Nenhuma residência cadastrada.</td></tr>`;
    return;
  }

  tb.innerHTML = residencias.map(r => {
    const total = r.quartos ? r.quartos.length : 0;
    const ocupados = ativos.filter(a =>
      a.residencia && a.residencia.id === r.id &&
      new Date(a.dataEntrada) <= agora && new Date(a.dataSaida) >= agora
    ).length;
    const livres = Math.max(0, total - ocupados);
    return `
      <tr>
        <td>${r.nome}</td>
        <td>${total}</td>
        <td><span class="badge ${ocupados > 0 ? "badge-coral" : "badge-verde"}">${ocupados}</span></td>
        <td><span class="badge badge-verde">${livres}</span></td>
      </tr>`;
  }).join("");
}
