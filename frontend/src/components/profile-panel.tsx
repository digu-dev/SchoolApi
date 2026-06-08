"use client";

import { useAuth } from "@/hooks/use-auth";

export function ProfilePanel() {
  const { profile, refreshProfile, tokenResponse } = useAuth();

  return (
    <div className="page-layout">
      <section className="page-header">
        <span className="badge">Perfil</span>
        <h1>Detalhes da autenticacao</h1>
        <p className="page-subtitle">
          Informacoes lidas do endpoint `/auth/me`, úteis para estudar claims e regras de acesso.
        </p>
      </section>

      <section className="button-row">
        <button className="button button-secondary" onClick={() => void refreshProfile()} type="button">
          Atualizar perfil
        </button>
      </section>

      <section className="overview-grid">
        <article className="overview-card">
          <h3>Usuario</h3>
          <p>{profile?.username ?? "Indisponivel"}</p>
        </article>
        <article className="overview-card">
          <h3>Issuer</h3>
          <p>{profile?.issuer ?? "Indisponivel"}</p>
        </article>
        <article className="overview-card">
          <h3>Expira em</h3>
          <p>{profile?.expiresAt ?? "Indisponivel"}</p>
        </article>
        <article className="overview-card">
          <h3>Expires in</h3>
          <p>{tokenResponse?.expires_in ?? 0} segundos</p>
        </article>
      </section>

      <section className="panel stack-lg">
        <div className="section-heading">
          <div>
            <h2>Authorities</h2>
            <p className="muted">Convertidas do realm_access.roles pelo backend Spring Security.</p>
          </div>
        </div>

        <div className="button-row">
          {profile?.authorities.map((authority) => (
            <span className="role-badge" key={authority}>
              {authority}
            </span>
          ))}
        </div>
      </section>

      <section className="response-card stack">
        <h3>Claims</h3>
        <pre className="response-pre">
          {JSON.stringify(profile?.claims ?? {}, null, 2)}
        </pre>
      </section>
    </div>
  );
}
