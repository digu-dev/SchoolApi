"use client";

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useEffect } from "react";
import { useAuth } from "@/hooks/use-auth";
import { navigationSections } from "@/routes/app-routes";

export function DashboardShell({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();
  const router = useRouter();
  const { logout, profile, status } = useAuth();

  useEffect(() => {
    if (status === "anonymous") {
      router.replace("/login");
    }
  }, [router, status]);

  if (status === "loading") {
    return (
      <main className="loading-screen">
        <section className="panel stack">
          <span className="badge">Carregando</span>
          <h2>Validando sessao atual</h2>
          <p className="muted">Aguarde enquanto o frontend confirma o token salvo.</p>
        </section>
      </main>
    );
  }

  if (status !== "authenticated" || !profile) {
    return null;
  }

  return (
    <div className="app-shell">
      <aside className="sidebar stack-lg">
        <div className="sidebar-section">
          <div className="sidebar-title">School API</div>
          <div className="sidebar-subtitle">
            Frontend em Next.js com BFF para o backend Spring Boot.
          </div>
        </div>

        {navigationSections.map((section) => (
          <section className="sidebar-section" key={section.title}>
            <div className="caption">{section.title}</div>
            <nav>
              {section.links.map((link) => (
                <Link
                  className={`nav-link ${pathname === link.href ? "active" : ""}`}
                  href={link.href}
                  key={link.href}
                >
                  <strong>{link.label}</strong>
                  <span>{link.description}</span>
                </Link>
              ))}
            </nav>
          </section>
        ))}

        <section className="sidebar-section panel stack">
          <div>
            <div className="caption">Usuario autenticado</div>
            <strong>{profile.username}</strong>
          </div>
          <div className="role-list">
            {profile.authorities.map((authority) => (
              <span className="role-badge" key={authority}>
                {authority}
              </span>
            ))}
          </div>
          <button className="button button-danger" onClick={logout} type="button">
            Sair
          </button>
        </section>
      </aside>

      <div className="content-area">
        <header className="topbar">
          <div>
            <div className="caption">Painel conectado ao backend</div>
            <div className="topbar-subtitle">
              Todas as chamadas passam pelos Route Handlers do Next para manter a integração
              centralizada.
            </div>
          </div>
          <div className="topbar-actions">
            <span className="badge">{profile.issuer ?? "Issuer indisponivel"}</span>
          </div>
        </header>

        {children}
      </div>
    </div>
  );
}
