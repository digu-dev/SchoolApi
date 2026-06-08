import Link from "next/link";

export default function NotFound() {
  return (
    <main className="auth-screen screen">
      <section className="panel stack">
        <span className="badge">404</span>
        <h1>Rota não encontrada</h1>
        <p className="muted">
          O recurso pedido não existe no frontend atual. Volte para o dashboard para escolher um
          módulo válido.
        </p>
        <div className="button-row">
          <Link className="button button-primary" href="/dashboard">
            Voltar ao dashboard
          </Link>
        </div>
      </section>
    </main>
  );
}
