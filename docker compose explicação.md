# Docker Compose explicação

## Como testar o login deste projeto

Antes de testar o login, deixe os containers no ar com:

```bash
docker compose up -d
```

Depois suba a API Spring Boot:

```bash
cd backend
.\mvnw.cmd spring-boot:run
```

Com a API em `http://localhost:8080` e o Keycloak em `http://localhost:8180`, o fluxo de login deste projeto funciona assim:

1. pedir um token ao Keycloak
2. copiar o `access_token`
3. chamar a API usando `Authorization: Bearer <token>`

### Exemplo 1: gerar token de admin

```bash
curl -X POST "http://localhost:8180/realms/school/protocol/openid-connect/token" ^
  -H "Content-Type: application/x-www-form-urlencoded" ^
  -d "client_id=school-api" ^
  -d "username=admin" ^
  -d "password=admin123" ^
  -d "grant_type=password"
```

Se estiver tudo certo, a resposta terá um campo `access_token`.

### Exemplo 2: testar a autenticação na API

Use o token retornado acima:

```bash
curl "http://localhost:8080/auth/me" ^
  -H "Authorization: Bearer SEU_ACCESS_TOKEN"
```

Se o login estiver funcionando, a API deve responder com informações do usuário autenticado, como `preferred_username`, `email`, `realm_access` e as authorities resolvidas.

## O que é Docker Compose

O Docker Compose é uma ferramenta para subir varios containers com um único arquivo de configuração. Em vez de criar manualmente um container do PostgreSQL, outro do Keycloak, portas, variáveis de ambiente e volumes, você descreve tudo em um `compose.yaml` e executa um único comando:

```bash
docker compose up -d
```

O Compose lê esse arquivo, cria a rede entre os serviços, sobe os containers e aplica as configurações declaradas.

## Como ele funciona neste projeto

Neste repositório, o arquivo `compose.yaml` define dois serviços:

```yaml
services:
  schooldb:
    image: postgres:16.3
```

Esse trecho mostra que o Compose cria um container PostgreSQL a partir da imagem `postgres:16.3`.

```yaml
  keycloak:
    image: quay.io/keycloak/keycloak:26.2
```

Esse trecho mostra que ele também sobe um container do Keycloak.

### 1. Serviço do banco

Trecho do `compose.yaml`:

```yaml
schooldb:
  image: postgres:16.3
  container_name: schooldb
  environment:
    POSTGRES_DB: school
    POSTGRES_USER: admin
    POSTGRES_PASSWORD: 1234
  ports:
    - "5431:5432"
  volumes:
    - postgres-data:/var/lib/postgresql/data
```

O que isso faz:

- cria um banco chamado `school`
- define usuário `admin`
- define senha `1234`
- expõe a porta local `5431`
- persiste os dados no volume `postgres-data`

Por isso a aplicação consegue acessar o banco por:

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5431/school}
    username: ${DB_USERNAME:admin}
    password: ${DB_PASSWORD:1234}
```

Esse trecho de `backend/src/main/resources/application.yaml` mostra a API conectando no PostgreSQL exposto pelo Compose.

### 2. Serviço do Keycloak

Trecho do `compose.yaml`:

```yaml
keycloak:
  image: quay.io/keycloak/keycloak:26.2
  container_name: school-keycloak
  command:
    - start-dev
    - --import-realm
  environment:
    KEYCLOAK_ADMIN: admin
    KEYCLOAK_ADMIN_PASSWORD: admin
  ports:
    - "8180:8080"
  volumes:
    - ./backend/infra/keycloak/school-realm.json:/opt/keycloak/data/import/school-realm.json:ro
  depends_on:
    - schooldb
```

O que isso faz:

- sobe o Keycloak em modo de desenvolvimento
- cria o admin do console com `admin` / `admin`
- publica o Keycloak em `http://localhost:8180`
- importa automaticamente o realm definido em `backend/infra/keycloak/school-realm.json`
- sobe o banco antes do Keycloak por causa de `depends_on`

Na pratica, isso quer dizer que você não precisa configurar o realm manualmente toda vez. O arquivo JSON já entra no container na inicialização.

### 3. O volume

No final do arquivo existe:

```yaml
volumes:
  postgres-data:
```

Esse volume guarda os dados do PostgreSQL fora do ciclo de vida do container. Então, mesmo que o container seja recriado, os dados podem continuar existindo.

Isso explica por que, em alguns casos, o Docker "diz que o banco já existe": normalmente o volume antigo ainda está lá.

### 4. Relação com a autenticação da API

A API foi configurada para confiar no Keycloak local:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: ${KEYCLOAK_ISSUER_URI:http://localhost:8180/realms/school}
          jwk-set-uri: ${KEYCLOAK_JWK_SET_URI:http://localhost:8180/realms/school/protocol/openid-connect/certs}
```

Esse trecho de `backend/src/main/resources/application.yaml` mostra que o Spring Security valida tokens emitidos pelo Keycloak do `localhost:8180`.

E o endpoint usado para testar o usuário autenticado está aqui:

```java
@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/me")
    public AuthenticatedUserResponse currentUser(JwtAuthenticationToken authentication) {
```

Esse trecho de `backend/src/main/java/digu_dev/com/github/SchoolAPI/controller/AuthController.java` mostra que a API expõe `/auth/me`, um endpoint útil para testar se o token recebido está valido e foi aceito pelo Spring Security.

## Comandos principais do Compose

### Subir os serviços

```bash
docker compose up -d
```

### Ver o status

```bash
docker compose ps
```

### Ver logs

```bash
docker compose logs
```

### Derrubar os containers

```bash
docker compose down
```

### Derrubar os containers e apagar o volume do banco

```bash
docker compose down -v
```

Use `down -v` com cuidado, porque ele remove os dados persistidos do PostgreSQL.
