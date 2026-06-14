exigirLogin();
montarSidebar("residencias");
carregar();

async function carregar() {
  const lista = document.getElementById("lista");
  try {
    const residencias = await api.get("/residencias");

    if (residencias.length === 0) {
      lista.innerHTML = "";
    } else {
      lista.innerHTML = residencias.map((r, i) => cardResidencia(r, i)).join("");
    }

    lista.insertAdjacentHTML("beforeend", `
      <a href="nova-residencia.html" class="card-vazio reveal">
        <span>＋</span>
        <strong>Adicionar residência</strong>
      </a>`);
    lista.querySelectorAll("[data-excluir]").forEach(btn =>
      btn.addEventListener("click", () => excluir(btn.dataset.excluir, btn.dataset.nome)));
  } catch (e) {
    lista.innerHTML = `<div class="aviso aviso-erro">${e.message}</div>`;
  }
}

function cardResidencia(r, i) {
  const qtd = r.quartos ? r.quartos.length : 0;
  const endereco = [r.endereco, r.numero].filter(Boolean).join(", ");
  return `
    <div class="residencia-card reveal" style="animation-delay:${i * 0.05}s">
      <div style="display:flex; justify-content:space-between; align-items:flex-start;">
        <div>
          <div class="residencia-nome">${r.nome || "Sem nome"}</div>
          <div class="residencia-end">${endereco || "Endereço não informado"}${r.bairro ? " · " + r.bairro : ""}${r.cidade ? " · " + r.cidade : ""}</div>
        </div>
        <span class="badge badge-verde">Ativa</span>
      </div>
      <div class="gap mb-1" style="margin-top:.8rem;">
        <span class="badge badge-azul">${qtd} quarto${qtd === 1 ? "" : "s"}</span>
        ${r.telefone ? `<span class="badge badge-cinza">${r.telefone}</span>` : ""}
      </div>
      <hr />
      <div class="acoes">
        <a href="detalhe-residencia.html?id=${r.id}" class="btn btn-primario btn-peq">Ver detalhes</a>
        <a href="nova-residencia.html?id=${r.id}" class="btn btn-contorno btn-peq">Editar</a>
        <button class="btn btn-perigo btn-peq" data-excluir="${r.id}" data-nome="${r.nome}">Excluir</button>
      </div>
    </div>`;
}

async function excluir(id, nome) {
  const ok = await confirmar("Excluir residência", `Tem certeza que deseja excluir "${nome}"? Os quartos vinculados também serão removidos.`);
  if (!ok) return;
  try {
    await api.del("/residencias/" + id);
    toast("Residência excluída.", "ok");
    carregar();
  } catch (e) {
    toast(e.message, "err");
  }
}
