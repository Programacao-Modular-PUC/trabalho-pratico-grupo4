
document.getElementById("btnEntrar").addEventListener("click", entrar);
document.getElementById("senha").addEventListener("keydown", (e) => { if (e.key === "Enter") entrar(); });

function entrar() {
  const email = document.getElementById("email").value.trim();
  const senha = document.getElementById("senha").value.trim();

  if (!email || !senha) {
    toast("Preencha e-mail e senha.", "err");
    return;
  }
  fazerLogin(email);
  window.location.href = "dashboard.html";
}
