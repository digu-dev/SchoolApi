# Frontend da School API

Aplicação **Next.js 16 + React 19** para autenticar no Keycloak, chamar o backend Spring Boot e navegar pelos recursos do projeto com uma camada BFF em `src/app/api/`.

## Estrutura

| Pasta | Uso |
| :--- | :--- |
| `src/app/` | rotas App Router, páginas protegidas e Route Handlers |
| `src/components/` | layout, login e explorador genérico de endpoints |
| `src/hooks/` | hooks de autenticação, proxy e execução de endpoints |
| `src/providers/` | providers globais da aplicação |
| `src/routes/` | metadados das rotas do frontend |
| `src/lib/` | contratos, catálogo de recursos e resolução de ambiente |

## Como rodar

1. Suba PostgreSQL e Keycloak na raiz do repositório:

   ```bash
   docker compose up -d
   ```

2. Suba o backend:

   ```bash
   cd ..\backend
   .\mvnw.cmd spring-boot:run
   ```

3. Volte para `frontend` e inicie o app:

   ```bash
   npm run dev
   ```

4. Abra `http://localhost:3000`.

## Variáveis de ambiente

Copie `.env.example` para `.env.local` se quiser customizar:

```env
BACKEND_API_URL=http://localhost:8080
KEYCLOAK_REALM_URL=http://localhost:8180/realms/school
KEYCLOAK_CLIENT_ID=school-api
```

## Credenciais de estudo

- `admin` / `admin123`
- `teacher` / `teacher123`

O login do frontend chama `src/app/api/auth/login/route.ts`, que converte o envio para `application/x-www-form-urlencoded` e fala com o Keycloak em nome da interface.
