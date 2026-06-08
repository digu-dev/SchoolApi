"use client";

import { useResourceExplorer } from "@/hooks/use-resource-explorer";
import type { EndpointAction, ResourceDefinition } from "@/lib/types";

function EndpointCard({ action }: { action: EndpointAction }) {
  const {
    bodyValue,
    error,
    hasBody,
    pathValues,
    pending,
    queryValues,
    result,
    run,
    setBodyValue,
    setPathValues,
    setQueryValues,
  } = useResourceExplorer(action);

  return (
    <article className="action-card stack" key={action.id}>
      <div className="stack">
        <div className="button-row">
          <span className="badge">{action.method}</span>
          <code>{action.pathTemplate}</code>
        </div>
        <h3>{action.label}</h3>
        <p>{action.description}</p>
      </div>

      {action.pathParams?.length ? (
        <div className="stack">
          <div className="caption">Parametros de rota</div>
          {action.pathParams.map((param) => (
            <div className="field-group" key={param.name}>
              <label className="field-label" htmlFor={`${action.id}-${param.name}`}>
                {param.label}
              </label>
              <input
                className="field-input"
                id={`${action.id}-${param.name}`}
                onChange={(event) =>
                  setPathValues((current) => ({
                    ...current,
                    [param.name]: event.target.value,
                  }))
                }
                placeholder={param.placeholder}
                value={pathValues[param.name] ?? ""}
              />
            </div>
          ))}
        </div>
      ) : null}

      {action.queryParams?.length ? (
        <div className="stack">
          <div className="caption">Query string</div>
          {action.queryParams.map((param) => (
            <div className="field-group" key={param.name}>
              <label className="field-label" htmlFor={`${action.id}-query-${param.name}`}>
                {param.label}
              </label>
              <input
                className="field-input"
                id={`${action.id}-query-${param.name}`}
                onChange={(event) =>
                  setQueryValues((current) => ({
                    ...current,
                    [param.name]: event.target.value,
                  }))
                }
                placeholder={param.placeholder}
                value={queryValues[param.name] ?? ""}
              />
            </div>
          ))}
        </div>
      ) : null}

      {hasBody ? (
        <div className="field-group">
          <label className="field-label" htmlFor={`${action.id}-body`}>
            Corpo JSON
          </label>
          <textarea
            className="field-textarea"
            id={`${action.id}-body`}
            onChange={(event) => setBodyValue(event.target.value)}
            spellCheck={false}
            value={bodyValue}
          />
        </div>
      ) : null}

      <div className="button-row">
        <button className="button button-primary" disabled={pending} onClick={() => void run()} type="button">
          {pending ? "Executando..." : "Executar"}
        </button>
      </div>

      {error ? <div className="action-error">{error}</div> : null}

      {result ? (
        <section className="response-card stack">
          <div className="button-row">
            <span className="status-pill status-success">HTTP {result.status}</span>
            <code>
              {result.method} {result.path}
            </code>
          </div>
          <pre className="response-pre">{JSON.stringify(result.payload, null, 2)}</pre>
        </section>
      ) : null}
    </article>
  );
}

export function ResourceExplorer({ resource }: { resource: ResourceDefinition }) {
  return (
    <div className="page-layout">
      <section className="page-header">
        <span className="badge">{resource.allowedRoles.join(" / ")}</span>
        <h1>{resource.title}</h1>
        <p className="page-subtitle">{resource.description}</p>
      </section>

      <section className="panel stack-lg">
        <div className="section-heading">
          <div>
            <h2>Rotas disponíveis</h2>
            <p className="muted">
              Os botões abaixo chamam o backend real através dos Route Handlers em `/api`.
            </p>
          </div>
        </div>

        <div className="action-grid">
          {resource.actions.map((action) => (
            <EndpointCard action={action} key={action.id} />
          ))}
        </div>
      </section>
    </div>
  );
}
