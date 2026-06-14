montarSidebar("clientes");

const idEdicao = qs("id");
const campos = ["nome", "cpf", "telefone", "email", "endereco"];

document.getElementById("btnSalvar").addEventListener("click", salvar);

if (idEdicao) carregar();

async function carregar() {
  document.getElementById("titulo").textContent = "Editar cliente";
  document.getElementById("bc").textContent = "Editar";
  try {
    const c = await api.get("/clientes/" + idEdicao);
    campos.forEach(k => { if (c[k] != null) document.getElementById(k).value = c[k]; });
  } catch (e) { toast(e.message, "err"); }
}

async function salvar() {
  const cliente = {};
  campos.forEach(k => cliente[k] = document.getElementById(k).value.trim());

  if (!cliente.nome) { toast("Informe o nome do cliente.", "err"); return; }
  if (!cliente.cpf) { toast("Informe o CPF.", "err"); return; }

  const btn = document.getElementById("btnSalvar");
  btn.disabled = true; btn.innerHTML = '<span class="spinner"></span> Salvando…';
  try {
    if (idEdicao) await api.put("/clientes/" + idEdicao, cliente);
    else await api.post("/clientes", cliente);
    toast("Cliente salvo!", "ok");
    setTimeout(() => window.location.href = "clientes.html", 700);
  } catch (e) {
    toast(e.message, "err");
    btn.disabled = false; btn.textContent = "Salvar cliente";
  }
}
