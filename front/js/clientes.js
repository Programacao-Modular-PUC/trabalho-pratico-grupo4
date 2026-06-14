
exigirLogin();
montarSidebar("clientes");

let clientes = [];
let contagem = {}; 

document.getElementById("busca").addEventListener("input", render);
carregar();

async function carregar() {
  try {
    const [lista, alugueis] = await Promise.all([api.get("/clientes"), api.get("/alugueis")]);
    clientes = lista;
    contagem = {};
    alugueis.forEach(a => {
      if (a.cliente) contagem[a.cliente.id] = (contagem[a.cliente.id] || 0) + 1;
    });
    render();
  } catch (e) {
    document.getElementById("tabela").innerHTML = `<tr><td colspan="6"><div class="aviso aviso-erro">${e.message}</div></td></tr>`;
  }
}

function render() {
  const termo = document.getElementById("busca").value.toLowerCase();
  const filtrados = clientes.filter(c =>
    (c.nome || "").toLowerCase().includes(termo) ||
    (c.cpf || "").toLowerCase().includes(termo) ||
    (c.email || "").toLowerCase().includes(termo));

  const tb = document.getElementById("tabela");
  if (filtrados.length === 0) {
    tb.innerHTML = `<tr><td colspan="6" class="text-muted">${clientes.length === 0 ? "Nenhum cliente cadastrado." : "Nenhum cliente encontrado."}</td></tr>`;
    return;
  }
  tb.innerHTML = filtrados.map(c => `
    <tr>
      <td><strong>${c.nome || "—"}</strong></td>
      <td>${c.cpf || "—"}</td>
      <td>${c.telefone || "—"}</td>
      <td>${c.email || "—"}</td>
      <td><span class="badge badge-azul">${contagem[c.id] || 0}</span></td>
      <td>
        <div class="acoes">
          <a href="cadastro-clientes.html?id=${c.id}" class="btn btn-contorno btn-peq">Editar</a>
          <button class="btn btn-perigo btn-peq" data-del="${c.id}" data-nome="${c.nome}">Excluir</button>
        </div>
      </td>
    </tr>`).join("");

  tb.querySelectorAll("[data-del]").forEach(b =>
    b.addEventListener("click", () => excluir(b.dataset.del, b.dataset.nome)));
}

async function excluir(id, nome) {
  const ok = await confirmar("Excluir cliente", `Deseja excluir "${nome}"?`);
  if (!ok) return;
  try {
    await api.del("/clientes/" + id);
    toast("Cliente excluído.", "ok");
    carregar();
  } catch (e) { toast(e.message, "err"); }
}
