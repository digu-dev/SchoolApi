"use client";

import Link from "next/link";
import { useAuth } from "@/hooks/use-auth";
import { resourceCatalog } from "@/lib/resource-config";
import { getResourceRoute } from "@/routes/app-routes";

export function DashboardHome() {
  const { profile, tokenResponse } = useAuth();

  return (
    <div className="page-layout">
      <section className="page-header">
        <span className="badge">Visao geral</span>
        <h1>Painel do sistema escolar</h1>
        <p className="page-subtitle">
          Explore o backend protegido por Keycloak, veja o perfil atual e execute os endpoints por
          recurso.
        </p>
      </section>

      <section className="metrics-grid">
        <article className="info-card">
          <div className="metric-value">{resourceCatalog.length}</div>
          <div className="metric-label">modulos mapeados da API</div>
        </article>
        <article className="info-card">
          <div className="metric-value">{profile?.authorities.length ?? 0}</div>
          <div className="metric-label">authorities carregadas no token</div>
        </article>
        <article className="info-card">
          <div className="metric-value">{tokenResponse?.expires_in ?? 0}s</div>
          <div className="metric-label">vida util informada pelo access token</div>
        </article>
      </section>

      <section className="panel stack-lg">
        <div className="section-heading">
          <div>
            <h2>Resumo da sessao</h2>
            <p className="muted">
              O frontend usa o token do Keycloak para consultar o endpoint protegido `/auth/me`.
            </p>
          </div>
        </div>

        <div className="overview-grid">
          <article className="overview-card">
            <h3>Usuario</h3>
            <p>{profile?.username ?? "Nao autenticado"}</p>
          </article>
          <article className="overview-card">
            <h3>Issuer</h3>
            <p>{profile?.issuer ?? "Indisponivel"}</p>
          </article>
          <article className="overview-card">
            <h3>Expira em</h3>
            <p>{profile?.expiresAt ?? "Indisponivel"}</p>
          </article>
        </div>
      </section>

      <section className="panel stack-lg">
        <div className="section-heading">
          <div>
            <h2>Explorador de recursos</h2>
            <p className="muted">
              Cada modulo abaixo abre uma tela com as rotas mais importantes daquele recurso.
            </p>
          </div>
        </div>

        <div className="resource-grid">
          {resourceCatalog.map((resource) => (
            <article className="overview-card" key={resource.slug}>
              <div className="stack">
                <span className="badge">{resource.allowedRoles.join(" / ")}</span>
                <h3>{resource.title}</h3>
                <p>{resource.description}</p>
              </div>

              <footer>
                <Link className="button button-secondary" href={getResourceRoute(resource.slug)}>
                  Abrir modulo
                </Link>
              </footer>
            </article>
          ))}
        </div>
      </section>
    </div>
  );
}
