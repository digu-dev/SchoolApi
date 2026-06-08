# Para melhorar

## Visão geral

O projeto está **bom para estudo e já tem uma espinha dorsal saudável**, mas ainda há alguns pontos de arquitetura e consistência que eu trataria antes de considerá-lo mais maduro.

## Prioridades altas

1. **Padronizar contrato da API.**  
Hoje há muita inconsistência de status HTTP e payloads: `DELETE` retorna `200` em alguns controllers e `204` em outros; listas vazias retornam `404` em `DepartmentController` e `GPAController`, mas `204` em outros (`src/main/java/.../controller/*.java`). Isso dificulta cliente, testes e documentação.

2. **Parar de expor entidades JPA diretamente nas respostas.**  
Vários endpoints retornam `Department`, `Teacher`, `Student`, `SchoolClass`, `Subject` e `GPA` diretamente (`DepartmentController`, `TeacherController`, `StudentController`, etc.). Isso acopla API ao modelo interno, aumenta risco de lazy loading/recursão e dificulta evolução.

3. **Remover duplicação entre controllers e handler global.**  
Os controllers capturam `IllegalArgumentException` e `ResourceAlreadyExistsException` manualmente, enquanto já existe `ResourceExceptionHandler` (`src/main/java/.../exception/ResourceExceptionHandler.java`). Hoje existem duas estratégias competindo.

4. **Corrigir o uso de `RuntimeException -> 404`.**  
Em `ResourceExceptionHandler.java`, qualquer `RuntimeException` vira “resource not found”. Isso mascara bugs reais como erro funcional de negócio. `404` deveria ser reservado para um tipo específico, não para exceções genéricas.

5. **Aplicar validação de entrada de verdade.**  
Os DTOs têm anotações úteis (`TeacherDto`, `StudentDto`, `DepartmentDto`, `GPADto`), mas os controllers não usam `@Valid` em `@RequestBody`, então parte do contrato não está sendo garantida.

## Problemas de modelagem

- **Entidade e API estão desalinhadas.**  
Exemplo: `Teacher` exige `department` (`entity/Teacher.java`), mas `TeacherDto` não carrega departamento e `TeacherService.create(...)` não o define. Isso indica que o fluxo de criação de professor está incompleto do ponto de vista do domínio.

- O mesmo vale para campos importantes do domínio que existem na entidade mas não aparecem nos DTOs, como `creditHours`, `gradeLevel`, `dateOfBirth`, `status`.

- `GPADto` aceita `finalGrade` do cliente, mas o serviço recalcula internamente. Isso cria um contrato ambíguo: o cliente informa ou o servidor calcula?

## JPA e serialização

- Eu evitaria `@Data` + `@ToString` em entidades com relacionamentos bidirecionais (`entity/*.java`). Isso costuma gerar `equals/hashCode/toString` perigosos com proxies, lazy loading e grafos cíclicos.

- Em `UserEntity`, `roles` está como `List<String>` sem mapeamento explícito. Eu tornaria isso explícito com `@ElementCollection` ou outro modelo, para evitar depender de comportamento implícito do provedor JPA.

- Há bom uso de queries com `JOIN FETCH` nos repositories, mas o projeto ainda depende bastante de retornar entidades completas, o que limita o ganho dessa preocupação.

## Segurança

- A direção nova com **OAuth2 Resource Server + Keycloak** está boa, mas ainda existe uma **dupla verdade de identidade**: o Keycloak autentica, mas o CRUD local de `/users` continua existindo (`UserController`, `UserService`, `UserEntity`). Para estudo isso funciona, mas arquiteturalmente fica confuso.

- Em `SecurityConfig.java`, a configuração está clara, mas eu evitaria criar `ObjectMapper` manualmente dentro da config. Em projeto mais maduro, isso deveria vir do container e dos mesmos módulos globais da aplicação.

- Em `application.yaml`, `ddl-auto: update`, `show-sql: true` e credenciais default em claro são aceitáveis para estudo, mas eu separaria melhor por perfis.

## Qualidade de código

- Há muito **field injection com `@Autowired`**. Eu migraria para **constructor injection** em todos os services/controllers/configs: melhora testabilidade, imutabilidade e legibilidade.

- Os controllers repetem muito fluxo de “buscar `Optional`, testar vazio, montar `ResponseEntity`”. Isso sugere espaço para extrair padrões ou mover mais responsabilidade para a camada de serviço.

- Alguns nomes e mensagens de erro ainda são inconsistentes; por exemplo, há typo em `"Resouce not found"` no handler.

## Testes

- A suíte cobre controller e service razoavelmente bem, o que é um ponto forte.

- O que eu senti falta:
  - testes de integração JPA para validar mapeamentos reais;
  - testes de segurança mais próximos do fluxo real com claims/roles do Keycloak;
  - testes de contrato HTTP para garantir consistência de status e payload entre endpoints.

## Pontos positivos

- Boa separação em **controller / service / repository**.
- Uso de **DTOs record** é um acerto.
- Já existe preocupação com **queries específicas para evitar N+1**.
- A introdução de **OAuth2/JWT** e o endpoint `/auth/me` deixaram o projeto muito melhor como material de estudo.

## Ordem sugerida de melhoria

1. consistência da API  
2. correção da modelagem de domínio vs DTOs  
3. parar de expor entidades JPA  
4. refinar exception handling  
5. simplificar a arquitetura de usuários em torno do Keycloak
