# 🏫 SchoolAPI - Sistema de Gestão Acadêmica

O **SchoolAPI** é uma API REST robusta desenvolvida para o gerenciamento completo de instituições de ensino. O projeto foca na organização de departamentos, turmas, professores e alunos, com uma lógica centralizada para o cálculo de **GPA (Grade Point Average)** e monitoramento de desempenho acadêmico.

Este projeto representa o consolidado dos meus estudos em **Backend Development**, unindo minha base acadêmica em Biologia e Neurociência com a Engenharia de Software.

---

## 🛠️ Tecnologias e Ferramentas

* **Java 21**: Uso de *Records* para imutabilidade e concisão nos DTOs.
* **Spring Boot 3.4.x**: Base do ecossistema da aplicação.
* **Spring Data JPA**: Persistência de dados com consultas otimizadas via `JOIN FETCH` para evitar o problema N+1.
* **PostgreSQL**: Banco de dados relacional (rodando em container Docker).
* **Docker & Docker Compose**: Orquestração do ambiente de banco de dados.
* **Swagger (OpenAPI 3)**: Documentação interativa disponível em `/swagger-ui/index.html`.
* **Bean Validation**: Validação de dados de entrada (`@NotBlank`, `@Email`, etc.).
* **Maven**: Gerenciamento de dependências.

---

## 🏗️ Arquitetura e Padrões

A aplicação foi construída seguindo as melhores práticas de mercado:

* **Camada de DTOs**: Proteção das entidades e controle total do JSON de saída, evitando recursão infinita.
* **Services Transacionais**: Lógica de negócio isolada com `@Transactional` para garantir a integridade das operações.
* **Global Exception Handler**: Tratamento centralizado de erros utilizando `@ControllerAdvice`, retornando respostas padronizadas (404 Not Found, 400 Bad Request).
* **Queries Customizadas**: Implementação de HQL no Repository para buscas eficientes de turmas e boletins.

---

## 📊 Estrutura de Endpoints (Principais)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/departments` | Lista todos os departamentos cadastrados. |
| **GET** | `/departments/name?name=...` | Busca um departamento específico por nome. |
| **POST** | `/teachers` | Cadastra um novo professor vinculado a um departamento. |
| **GET** | `/students/report/{id}` | Gera o boletim detalhado com cálculo automático de média. |
| **PATCH** | `/gpa/{id}` | Atualiza notas de um aluno em uma disciplina específica. |

---

## 🚀 Como Executar

1.  **Clone o repositório**:
    ```bash
    git clone [https://github.com/seu-usuario/SchoolAPI.git](https://github.com/seu-usuario/SchoolAPI.git)
    ```

2.  **Suba o Banco de Dados (Docker)**:
    ```bash
    docker-compose up -d
    ```
    *Nota: O banco PostgreSQL está configurado na porta `5431` para evitar conflitos locais.*

3.  **Rode a aplicação**:
    Utilize sua IDE (IntelliJ/VS Code) ou o terminal via Maven:
    ```bash
    mvn spring-boot:run
    ```

4.  **Acesse a Documentação**:
    Abra no navegador: `http://localhost:8080/swagger-ui/index.html`

---

## 📝 Sobre o Autor

**Rodrigo**
* 🎓 Estudante de Sistemas de Informação (3º Período).
* 🧬 Graduado em Ciências Biológicas com Pós-graduação em Neurociência.
* 🎯 Foco em Backend (Java/Spring) e DevOps.
* 📜 Certificado Java (Basic) e em preparação para Azure Fundamentals (AZ-900).

---
