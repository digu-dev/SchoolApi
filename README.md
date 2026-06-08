# 🏫 SchoolAPI - Sistema de Gestão Acadêmica

O **SchoolAPI** agora está organizado como um repositório full stack, com **backend Spring Boot** em `backend/` e **frontend Next.js** em `frontend/`. Além do domínio acadêmico, o projeto também serve como **laboratório de OAuth2/OIDC com Keycloak**, usando a API como **Resource Server** protegido por **JWT** e um frontend React para explorar os endpoints.

---

## 🛠️ Tecnologias e Ferramentas

* **Java 21**
* **Spring Boot**
* **Spring Web MVC**
* **Spring Data JPA**
* **Spring Security**
* **OAuth2 Resource Server**
* **PostgreSQL**
* **Keycloak**
* **Swagger / OpenAPI**
* **Maven**
* **Docker Compose**

---

## 🏗️ Arquitetura e Padrões

### Estrutura do repositório

| Pasta | Responsabilidade |
| :--- | :--- |
| `backend/` | API Spring Boot, testes, Dockerfile e realm do Keycloak |
| `frontend/` | App Next.js com hooks, providers, routes e Route Handlers para integração |
| `markdowns/` | documentação de endpoints e relatório de estudo |
| `compose.yaml` | infraestrutura local de PostgreSQL e Keycloak |

* **Controllers + Services + Repositories**: separação clara entre entrada HTTP, regra de negócio e persistência.
* **DTOs**: controle do formato de entrada e saída da API.
* **`@PreAuthorize`**: regras de autorização por papel na camada de serviço.
* **Resource Server stateless**: a API não cria sessão nem faz login local; ela valida Bearer Tokens emitidos pelo Keycloak.
* **Tratamento padronizado de erro**: respostas 401/403 e erros da aplicação seguem payload consistente.

---

## 🔐 Visão geral da segurança

### O que foi implementado

O projeto usa **Keycloak** como provedor OAuth2/OIDC e o **SchoolAPI** como **OAuth2 Resource Server**.

Na prática isso significa:

1. O usuário faz login no **Keycloak**.
2. O Keycloak emite um **access token JWT**.
3. O cliente envia `Authorization: Bearer <token>` para a API.
4. O Spring Security valida assinatura, emissor e expiração do token.
5. As roles do token são convertidas em authorities do Spring, como `ROLE_ADMIN` e `ROLE_TEACHER`.
6. A API libera ou bloqueia acesso conforme as regras configuradas.

### O que a API não faz mais

A API **não usa `formLogin()` nem `httpBasic()`** para autenticar usuários.  
Ela também **não emite JWT próprio** neste fluxo de estudo; quem emite o token é o **Keycloak**.

---

## 🧠 OAuth2, OIDC e JWT — explicação didática

### OAuth2

**OAuth2** é um protocolo de autorização. Ele define como uma aplicação obtém um token para acessar um recurso protegido.

Neste projeto:

* **Keycloak** atua como Authorization Server / Identity Provider
* **SchoolAPI** atua como Resource Server

### OIDC

**OpenID Connect (OIDC)** é uma camada de identidade sobre OAuth2.  
É ele que padroniza informações sobre o usuário autenticado, como `sub`, `preferred_username`, `email` e metadados do emissor.

### JWT

O **JWT (JSON Web Token)** é o formato do access token usado aqui.  
Ele carrega claims como:

* `sub`
* `preferred_username`
* `exp`
* `iat`
* `iss`
* `realm_access.roles`

O token é assinado pelo Keycloak e validado pela API usando a chave pública exposta pelo provedor.

---

## 🗺️ Onde cada parte da segurança está no código

| Conceito | Arquivo | O que estudar |
| :--- | :--- | :--- |
| Configuração principal da segurança | `backend/src/main/java/digu_dev/com/github/SchoolAPI/config/SecurityConfig.java` | `SecurityFilterChain`, regras por endpoint, modo stateless, JWT, handlers 401/403 |
| Conversão de roles do Keycloak | `backend/src/main/java/digu_dev/com/github/SchoolAPI/security/KeycloakRealmRoleConverter.java` | leitura de `realm_access.roles` e conversão para `ROLE_*` |
| Endpoint didático do usuário autenticado | `backend/src/main/java/digu_dev/com/github/SchoolAPI/controller/AuthController.java` | como acessar claims e authorities do token já validado |
| DTO de resposta do usuário autenticado | `backend/src/main/java/digu_dev/com/github/SchoolAPI/dto/AuthenticatedUserResponse.java` | estrutura de retorno do `/auth/me` |
| Regras por papel na camada de negócio | `backend/src/main/java/digu_dev/com/github/SchoolAPI/service/*.java` | uso de `@PreAuthorize` |
| Erro padronizado | `backend/src/main/java/digu_dev/com/github/SchoolAPI/exception/StandardError.java` | formato dos erros retornados |
| Infra local do Keycloak | `compose.yaml` e `backend/infra/keycloak/school-realm.json` | containers, realm, client, roles e usuários de estudo |
| Testes de segurança | `backend/src/test/java/digu_dev/com/github/SchoolAPI/tests/controller` | uso de JWT simulado com `spring-security-test` |

---

## 🔎 Regras atuais de autorização

| Área | Regra |
| :--- | :--- |
| Swagger / OpenAPI | público |
| `/auth/me` | qualquer usuário autenticado |
| `/users/**` | `ADMIN` |
| `/teachers/**` | `ADMIN` |
| `/gpa/**` | `TEACHER` |
| `/departments/**`, `/students/**`, `/subjects/**`, `/school-classes/**` | `ADMIN` ou `TEACHER` |

Além disso, a camada de serviço também reforça autorização com `@PreAuthorize`.

---

## 🚀 Como executar

### 1. Suba PostgreSQL e Keycloak

Na raiz do projeto:

```bash
docker compose up -d
```

Serviços disponíveis:

* **PostgreSQL**: `localhost:5431`
* **Keycloak**: `http://localhost:8180`

### 2. Credenciais de estudo do Keycloak

**Acesso ao console do Keycloak**

* usuário: `admin`
* senha: `admin`

**Usuários do realm `school`**

| Usuário | Senha | Role |
| :--- | :--- | :--- |
| `admin` | `admin123` | `admin` |
| `teacher` | `teacher123` | `teacher` |

### 3. Rode o backend

No Windows:

```bash
cd backend
.\mvnw.cmd spring-boot:run
```

O backend ficará disponível em:

* `http://localhost:8080`

### 4. Rode o frontend

Em outro terminal:

```bash
cd frontend
npm run dev
```

O frontend ficará disponível em:

* `http://localhost:3000`

### 5. Resumo rápido para rodar tudo

**Terminal 1 — infraestrutura**

```bash
docker compose up -d
```

**Terminal 2 — backend**

```bash
cd backend
.\mvnw.cmd spring-boot:run
```

**Terminal 3 — frontend**

```bash
cd frontend
npm run dev
```

### 6. Acesse a documentação

* Swagger UI: `http://localhost:8080/swagger-ui/index.html`
* Frontend Next.js: `http://localhost:3000`

---

## 🔑 Como obter um token de acesso

Para estudo local, o client `school-api` foi configurado com **Direct Access Grants** habilitado.

### Token para admin

```bash
curl -X POST "http://localhost:8180/realms/school/protocol/openid-connect/token" ^
  -H "Content-Type: application/x-www-form-urlencoded" ^
  -d "client_id=school-api" ^
  -d "username=admin" ^
  -d "password=admin123" ^
  -d "grant_type=password"
```

### Token para teacher

```bash
curl -X POST "http://localhost:8180/realms/school/protocol/openid-connect/token" ^
  -H "Content-Type: application/x-www-form-urlencoded" ^
  -d "client_id=school-api" ^
  -d "username=teacher" ^
  -d "password=teacher123" ^
  -d "grant_type=password"
```

O campo importante da resposta é `access_token`.

> **Observação didática:** o grant type `password` é útil para laboratório e entendimento do fluxo, mas não é a melhor opção para produção moderna.

---

## 🧪 Como usar o token na API

Depois de copiar o `access_token`:

```bash
curl http://localhost:8080/auth/me ^
  -H "Authorization: Bearer SEU_TOKEN"
```

Esse endpoint existe justamente para estudo. Ele mostra:

* username resolvido
* subject
* issuer
* datas do token
* authorities do Spring Security
* claims relevantes do JWT

Exemplo de uso em endpoint protegido:

```bash
curl http://localhost:8080/departments ^
  -H "Authorization: Bearer SEU_TOKEN"
```

---

## 📚 Como estudar esta implementação

### Etapa 1 — Entenda a configuração base

Abra `SecurityConfig.java` e leia nesta ordem:

1. `sessionCreationPolicy(STATELESS)`
2. `authorizeHttpRequests(...)`
3. `oauth2ResourceServer(...)`
4. handlers de `401` e `403`
5. bean `JwtAuthenticationConverter`

### Etapa 2 — Entenda como o token vira autorização

Abra `KeycloakRealmRoleConverter.java`.

Ali acontece um ponto central:

1. o código lê `realm_access.roles`
2. converte cada role em `ROLE_*`
3. entrega authorities para o Spring Security

Exemplo:

* Keycloak envia `admin`
* a aplicação converte para `ROLE_ADMIN`
* `hasRole("ADMIN")` passa a funcionar

### Etapa 3 — Entenda a diferença entre autenticação e autorização

**Autenticação**

* verificar se o token é válido
* conferir assinatura, emissor e expiração

**Autorização**

* decidir o que aquele usuário pode fazer
* isso acontece nas regras HTTP e em `@PreAuthorize`

### Etapa 4 — Veja o usuário autenticado em tempo real

Abra `AuthController.java` e faça chamadas para `/auth/me`.

Esse endpoint é importante para visualizar:

* claims do JWT
* principal resolvido
* authorities finais que o Spring montou

### Etapa 5 — Compare regra HTTP com regra de serviço

Veja:

* `SecurityConfig.java`
* `TeacherService.java`
* `DepartmentService.java`
* `StudentService.java`
* `SubjectService.java`
* `SchoolClassService.java`
* `GPAService.java`

Você vai notar duas camadas:

1. **segurança no endpoint**
2. **segurança na regra de negócio**

Isso ajuda a entender defesa em profundidade.

### Etapa 6 — Estude os testes

Nos testes de controller, veja o uso de JWT falso com `spring-security-test`.

Arquivos úteis:

* `ControllerSecurityTestSupport.java`
* `AuthControllerTest.java`
* `TeacherControllerTest.java`

Esses testes mostram:

* como simular `ROLE_ADMIN`
* como simular `ROLE_TEACHER`
* como validar `401`, `403` e `200`

---

## 🛡️ Pontos importantes de segurança aprendidos aqui

1. **Bearer Token substitui sessão**
2. **JWT não precisa ser gerado pela API para a API usar JWT**
3. **Resource Server valida token; Authorization Server emite token**
4. **roles do provedor precisam ser mapeadas para authorities da aplicação**
5. **`@EnableMethodSecurity` é necessário para `@PreAuthorize` funcionar**
6. **não exponha senha em responses**
7. **401 e 403 são erros diferentes**
   * `401`: sem autenticação válida
   * `403`: autenticado, mas sem permissão

---

## 📝 Observação sobre o CRUD `/users`

Os endpoints `/users` continuam no projeto, mas **a autenticação OAuth2/OIDC deste estudo vem do Keycloak**.  
Ou seja: o login real do fluxo estudado **não depende** do cadastro local de usuários da aplicação.

Esses endpoints foram mantidos como referência de CRUD interno e agora não expõem mais senha nas respostas.

---

## 📌 Próximos estudos sugeridos

Se quiser aprofundar depois desta base:

1. adicionar validação de `audience`
2. estudar refresh token
3. trocar `password grant` por fluxo com Authorization Code + PKCE
4. integrar Swagger com OAuth2 login
5. adicionar testes de integração com token real emitido pelo Keycloak
