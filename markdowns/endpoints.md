# Endpoints da School API

## Visão geral

Base URL local do backend: `http://localhost:8080`

| Área | Regra de acesso |
| :--- | :--- |
| `/auth/me` | qualquer usuário autenticado |
| `/users/**` | `ROLE_ADMIN` |
| `/teachers/**` | `ROLE_ADMIN` |
| `/gpa/**` | `ROLE_TEACHER` |
| `/departments/**`, `/students/**`, `/subjects/**`, `/school-classes/**` | `ROLE_ADMIN` ou `ROLE_TEACHER` |

## Autenticação

### `GET /auth/me`

Retorna o usuário autenticado, issuer, expiração, authorities e claims relevantes do JWT validado pelo Spring Security.

Exemplo de resposta:

```json
{
  "username": "admin",
  "subject": "uuid-do-usuario",
  "issuer": "http://localhost:8180/realms/school",
  "issuedAt": "2026-06-08T20:00:00Z",
  "expiresAt": "2026-06-08T20:05:00Z",
  "authorities": ["ROLE_ADMIN"],
  "claims": {
    "preferred_username": "admin",
    "email": "admin@school.local",
    "realm_access": {
      "roles": ["admin"]
    }
  }
}
```

## Usuários locais

| Método | Rota | Corpo | Resposta |
| :--- | :--- | :--- | :--- |
| `POST` | `/users` | `{ "username": "operador", "password": "Senha123!", "role": "admin" }` | `201 Created` |
| `GET` | `/users` | — | lista de `UserResponseDto` |
| `GET` | `/users/{username}` | — | `UserResponseDto` |
| `PUT` | `/users/{username}` | mesmo corpo do `POST` | `204 No Content` |
| `DELETE` | `/users/{username}` | — | `204 No Content` |

`UserResponseDto`:

```json
{
  "id": 1,
  "username": "operador",
  "roles": ["admin"]
}
```

## Departamentos

| Método | Rota | Corpo | Resposta |
| :--- | :--- | :--- | :--- |
| `POST` | `/departments` | `{ "name": "Matematica" }` | `201 Created` |
| `GET` | `/departments` | — | lista de `Department` |
| `GET` | `/departments/{id}` | — | `DepartmentDto` |
| `GET` | `/departments/name?name=Matematica` | — | `DepartmentDto` |
| `GET` | `/departments/{id}/teachers` | — | `Department` com teachers |
| `PUT` | `/departments/{id}` | `{ "name": "Matematica Aplicada" }` | `204 No Content` |
| `DELETE` | `/departments/{id}` | — | `204 No Content` |

`DepartmentDto`:

```json
{
  "id": 1,
  "name": "Matematica"
}
```

## Matérias

| Método | Rota | Corpo | Resposta |
| :--- | :--- | :--- | :--- |
| `POST` | `/subjects` | `{ "name": "Algebra Linear" }` | `201 Created` com body `Subject` |
| `GET` | `/subjects` | — | lista de `Subject` |
| `GET` | `/subjects/{id}` | — | `SubjectDto` |
| `GET` | `/subjects/name/{name}` | — | `Subject` com teachers |
| `GET` | `/subjects/nameSubject/{name}` | — | `Subject` simples |
| `PUT` | `/subjects/{id}` | `{ "name": "Calculo I" }` | `200 OK` |
| `DELETE` | `/subjects/{id}` | — | `200 OK` |

`SubjectDto`:

```json
{
  "id": 1,
  "name": "Algebra Linear"
}
```

## Professores

| Método | Rota | Corpo | Resposta |
| :--- | :--- | :--- | :--- |
| `POST` | `/teachers` | `{ "name": "Maria Souza", "email": "maria@school.local", "subject": ["Algebra Linear"] }` | `201 Created` com body `Teacher` |
| `GET` | `/teachers` | — | lista de `Teacher` |
| `GET` | `/teachers/{id}` | — | `TeacherDto` |
| `GET` | `/teachers/{id}/subjects` | — | lista de `String` |
| `GET` | `/teachers/{id}/department` | — | `String` |
| `GET` | `/teachers/department/{name}` | — | lista de `Teacher` |
| `PUT` | `/teachers/{id}` | `{ "name": "Maria Souza Lima", "email": "maria@school.local" }` | `200 OK` |
| `DELETE` | `/teachers/{id}` | — | `200 OK` |

`TeacherDto`:

```json
{
  "id": 1,
  "name": "Maria Souza",
  "email": "maria@school.local",
  "subject": ["Algebra Linear"]
}
```

## Turmas

| Método | Rota | Corpo | Resposta |
| :--- | :--- | :--- | :--- |
| `POST` | `/school-classes` | `{ "classCode": "A1" }` | `201 Created` |
| `GET` | `/school-classes` | — | lista de `SchoolClass` |
| `GET` | `/school-classes/{id}` | — | `SchoolClassDto` |
| `GET` | `/school-classes/code/{classCode}` | — | `SchoolClassDto` |
| `GET` | `/school-classes/code/{classCode}/students` | — | `SchoolClass` com students |
| `PUT` | `/school-classes/{id}` | `{ "classCode": "B2" }` | `200 OK` |
| `DELETE` | `/school-classes/{id}` | — | `204 No Content` |

`SchoolClassDto`:

```json
{
  "id": 1,
  "classCode": "A1"
}
```

## Estudantes

| Método | Rota | Corpo | Resposta |
| :--- | :--- | :--- | :--- |
| `POST` | `/students` | `{ "name": "Joao Silva", "registration": "2026-001", "schoolClassCode": "A1" }` | `201 Created` |
| `GET` | `/students` | — | lista de `Student` |
| `GET` | `/students/{id}` | — | `StudentDto` |
| `GET` | `/students/registration/{registration}` | — | `StudentDto` |
| `PUT` | `/students/{id}` | `{ "name": "Joao da Silva", "registration": "2026-001" }` | `200 OK` |
| `DELETE` | `/students/{id}` | — | `200 OK` |

`StudentDto`:

```json
{
  "id": 1,
  "name": "Joao Silva",
  "registration": "2026-001",
  "schoolClassCode": "A1"
}
```

## Notas / GPA

| Método | Rota | Corpo | Resposta |
| :--- | :--- | :--- | :--- |
| `POST` | `/gpa` | ver payload abaixo | `201 Created` |
| `GET` | `/gpa` | — | lista de `GPA` |
| `GET` | `/gpa/{id}` | — | `GPADto` |
| `GET` | `/gpa/student/{studentId}` | — | lista de `GPA` |
| `GET` | `/gpa/student/{studentId}/subject/{subjectId}/final-grade` | — | `Double` |
| `GET` | `/gpa/student/{studentId}/final-grades` | — | lista de `Double` |
| `PUT` | `/gpa/{id}` | mesmo payload do `POST` | `204 No Content` |
| `DELETE` | `/gpa/{id}` | — | `204 No Content` |

Exemplo de `GPADto`:

```json
{
  "g1": 20,
  "g2": 25,
  "g3": 35,
  "finalGrade": 80,
  "subject": { "id": 1 },
  "student": { "id": 1 },
  "status": "APPROVED"
}
```

## Observações úteis para o frontend

1. O login não acontece no backend; ele é feito no Keycloak e o backend só aceita Bearer token.
2. Alguns endpoints devolvem `204 No Content` quando não há dados; outros devolvem `404`.
3. `TeacherDto.subject` recebe nomes de matérias, não IDs.
4. `StudentDto.schoolClassCode` recebe o `classCode` da turma.
5. O frontend criado em `frontend/` usa Route Handlers do Next em `src/app/api/` para centralizar a comunicação com o backend e o Keycloak.
