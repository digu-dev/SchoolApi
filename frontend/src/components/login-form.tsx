"use client";

import { FormEvent, useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/hooks/use-auth";

export function LoginForm() {
  const router = useRouter();
  const { login, loginError, status } = useAuth();
  const [username, setUsername] = useState("admin");
  const [password, setPassword] = useState("admin123");
  const [pending, setPending] = useState(false);

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setPending(true);

    try {
      await login({ username, password });
      router.push("/dashboard");
    } catch {
      // AuthProvider already stores the current message.
    } finally {
      setPending(false);
    }
  };

  return (
    <main className="auth-screen screen">
      <section className="auth-card">
        <div className="auth-copy stack-lg">
          <span className="badge">React + Next + Spring Security</span>
          <h1>Frontend de estudo para a School API</h1>
          <p>
            Este login chama um Route Handler do Next, que converte o envio para
            `application/x-www-form-urlencoded` e fala com o Keycloak sem expor a integração no
            cliente.
          </p>

          <div className="panel stack">
            <h2>Credenciais de estudo</h2>
            <ul className="credentials-list">
              <li>admin / admin123</li>
              <li>teacher / teacher123</li>
              <li>Console do Keycloak: admin / admin</li>
            </ul>
          </div>
        </div>

        <form className="auth-form stack-lg" onSubmit={handleSubmit}>
          <div className="stack">
            <span className="badge">{status === "authenticated" ? "Sessao ativa" : "Login"}</span>
            <h2>Entrar</h2>
            <p className="muted">
              Use o mesmo usuário do realm `school` para obter o token e navegar pelos módulos.
            </p>
          </div>

          <div className="field-group">
            <label className="field-label" htmlFor="username">
              Usuario
            </label>
            <input
              className="field-input"
              id="username"
              onChange={(event) => setUsername(event.target.value)}
              value={username}
            />
          </div>

          <div className="field-group">
            <label className="field-label" htmlFor="password">
              Senha
            </label>
            <input
              className="field-input"
              id="password"
              onChange={(event) => setPassword(event.target.value)}
              type="password"
              value={password}
            />
          </div>

          {loginError ? <div className="auth-error">{loginError}</div> : null}

          <div className="button-row">
            <button className="button button-primary" disabled={pending} type="submit">
              {pending ? "Entrando..." : "Entrar com Keycloak"}
            </button>
          </div>
        </form>
      </section>
    </main>
  );
}
