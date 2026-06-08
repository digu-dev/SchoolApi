# Relatório de estudo da School API

## Objetivo do projeto

O projeto começou como uma API REST acadêmica em Spring Boot e evoluiu para um laboratório full stack com:

- **backend Java/Spring Boot** em `backend/`
- **frontend Next.js/React** em `frontend/`
- **infraestrutura local** via `compose.yaml`
- **autenticação externa** com Keycloak

O foco didático principal é entender como autenticação, autorização e integração entre frontend e backend funcionam em um cenário moderno.

## Como a arquitetura está organizada

### Backend

O backend segue uma divisão clássica:

- `controller/`: entrada HTTP
- `service/`: regra de negócio
- `repository/`: persistência JPA
- `dto/`: contratos de entrada e saída
- `config/` e `security/`: configuração do Resource Server

Essa organização deixa o fluxo de cada entidade fácil de estudar: controller recebe a requisição, service aplica regra, repository conversa com o banco.

### Frontend

O frontend foi criado em **Next.js 16** com **App Router** e a seguinte organização:

- `src/app/`: páginas, layouts e Route Handlers
- `src/hooks/`: hooks de autenticação e de chamada da API
- `src/providers/`: `AuthProvider` e composição global
- `src/routes/`: catálogo das rotas internas
- `src/lib/`: contratos e catálogo dos recursos do backend
- `src/components/`: layout, login, dashboard e explorador dos endpoints

Esse arranjo permite estudar separadamente:

1. a experiência do usuário
2. o estado de autenticação
3. a camada BFF do Next
4. o consumo real do backend Spring

## Fluxo de autenticação

O fluxo atual é:

1. o usuário digita credenciais no frontend
2. o frontend chama `src/app/api/auth/login/route.ts`
3. esse Route Handler envia `application/x-www-form-urlencoded` ao Keycloak
4. o Keycloak devolve o token
5. o frontend salva o token e consulta `/auth/me`
6. o backend valida o JWT e resolve as authorities

Esse desenho é interessante para estudo porque separa bem as responsabilidades:

- **Keycloak** autentica
- **Next.js** centraliza a integração do frontend
- **Spring Boot** autoriza e entrega os dados

## Por que usar Route Handlers no frontend

Em vez de o navegador falar direto com o backend e com o Keycloak, o frontend usa Route Handlers em `src/app/api/`.

Isso ajuda a estudar o padrão **Backend for Frontend (BFF)**:

- o cliente não precisa conhecer detalhes do `x-www-form-urlencoded` do Keycloak
- a interface fica com uma API interna estável
- a comunicação com backend e IdP fica centralizada
- futuras regras de auditoria, refresh token ou normalização de erro podem entrar nesse ponto

## Pontos fortes do projeto

1. **Separação de autenticação e autorização**
2. **API stateless com Bearer token**
3. **Mapeamento de roles do Keycloak para `ROLE_*`**
4. **Frontend separado do backend**
5. **Infra local simples de subir com Docker Compose**
6. **Cobertura funcional suficiente para explorar todo o domínio**

## Limitações importantes para estudo

Mesmo sendo um bom laboratório, há decisões que devem ser lidas como didáticas:

1. uso de **`start-dev`** no Keycloak
2. credenciais fixas de estudo
3. login por **password grant**
4. frontend sem refresh token automático
5. endpoints com alguns contratos e status codes inconsistentes

Esses pontos não invalidam o projeto. Eles apenas mostram que ele está orientado a aprendizado, não a produção.

## O que o frontend novo resolve

Antes, o projeto era centrado só na API. Agora existe uma interface para:

- autenticar com `admin` ou `teacher`
- inspecionar token, issuer, claims e authorities
- navegar pelos módulos da API
- executar `GET`, `POST`, `PUT` e `DELETE` em cada recurso
- testar payloads reais com JSON

Isso torna o projeto melhor para estudo porque elimina a dependência exclusiva de Postman ou curl.

## Como estudar o projeto por etapas

### Etapa 1 — Infra

Suba:

```bash
docker compose up -d
```

Observe:

- PostgreSQL em `localhost:5431`
- Keycloak em `http://localhost:8180`

### Etapa 2 — Backend

Rode:

```bash
cd backend
.\mvnw.cmd spring-boot:run
```

Depois teste:

- `GET /auth/me`
- Swagger em `http://localhost:8080/swagger-ui/index.html`

### Etapa 3 — Frontend

Rode:

```bash
cd frontend
npm run dev
```

Depois abra:

```text
http://localhost:3000
```

### Etapa 4 — Segurança

Estude estes arquivos:

- `backend/src/main/java/digu_dev/com/github/SchoolAPI/config/SecurityConfig.java`
- `backend/src/main/java/digu_dev/com/github/SchoolAPI/security/KeycloakRealmRoleConverter.java`
- `backend/src/main/java/digu_dev/com/github/SchoolAPI/controller/AuthController.java`
- `frontend/src/providers/auth-provider.tsx`
- `frontend/src/app/api/auth/login/route.ts`

## Aprendizados que este projeto oferece

Com esse repositório, dá para estudar de forma prática:

- OAuth2 e OIDC
- JWT e claims
- Spring Security Resource Server
- integração com Keycloak
- arquitetura controller/service/repository
- BFF com Next.js
- estado global de autenticação no frontend
- consumo de API protegida por Bearer token

## Próximos passos técnicos recomendados

1. substituir password grant por **Authorization Code + PKCE**
2. padronizar respostas e status codes dos endpoints
3. adicionar refresh token no frontend
4. proteger ou integrar melhor o Swagger com OAuth2
5. criar testes automatizados do frontend
6. adicionar validação explícita de `audience` no backend

## Conclusão

Como projeto de estudo, a School API agora ficou mais completa: ela mostra não só a API e a segurança, mas também como um frontend moderno conversa com um backend protegido por um provedor de identidade externo.

O valor didático do projeto cresceu bastante porque agora é possível visualizar o fluxo inteiro, do login até o consumo dos recursos protegidos, dentro do mesmo repositório.
