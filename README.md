# IT HelpDesk API

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-green.svg)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/JWT-JJWT%200.12.x-black.svg)](https://github.com/jwtk/jjwt)
[![Database](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Projeto pessoal desenvolvido para praticar e consolidar a construção de APIs RESTful profissionais com ecossistema Spring Boot, Java 21 e arquitetura de segurança stateless baseada em JWT.

O foco central da aplicação é implementar as regras de negócio de um HelpDesk corporativo sem uso de geradores de código prontos, exercitando modelagem relacional, controle de concorrência/transações e proteção em camadas por perfis de acesso (RBAC).

---

## Objetivos de Aprendizado & Destaques Técnicos

- **Autenticação Stateless com JWT (JJWT 0.12.x)**: Sessões sem estado gerenciadas via tokens assinados criptograficamente com HMAC-SHA. Filtro customizado (`OncePerRequestFilter`) para extração e injeção do contexto do usuário (`SecurityContextHolder`).
- **Controle de Acesso em Três Níveis (RBAC)**:
  1. **Rotas Web**: Restrição via `SecurityFilterChain` separando rotas públicas de privadas.
  2. **Nível de Método**: Anotações `@PreAuthorize("hasRole(...)")` limitando rotas administrativas e operacionais.
  3. **Regra de Propriedade**: Validação na camada de serviço para garantir que clientes só visualizem e interajam com seus próprios chamados.
- **Imutabilidade e Validação Declarativa**: Uso de Java **Records** em 100% dos contratos de DTO com validações do Jakarta Validation (`@Valid`, `@NotBlank`, `@Email`), garantindo que entidades JPA nunca vazem para os controllers.
- **Tratamento Global de Exceções**: Centralizado com `@RestControllerAdvice`, padronizando respostas HTTP de erros (`400`, `401`, `403`, `404`, `409`) com timestamps e campos violados.
- **Paginação e Performance**: Listagens construídas com `Pageable` e queries derivadas do Spring Data JPA, evitando consultas com alto consumo de memória.

---

## Modelo de Dados

O banco de dados relacional (MySQL) é estruturado em três tabelas principais:

- **`usuario`**: Armazena dados de acesso, senhas com hash BCrypt e papéis de autoridade (`ROLE_CLIENTE`, `ROLE_TECNICO`, `ROLE_ADMIN`). Implementa `UserDetails`.
- **`chamados`**: Tickets de suporte contendo título, descrição, auditoria de datas, níveis de prioridade (`BAIXA`, `MEDIA`, `ALTA`) e ciclo de vida de status (`ABERTO` $\to$ `EM_ANDAMENTO` $\to$ `RESOLVIDO` / `CANCELADO`). Relacionamentos `@ManyToOne` com o autor e técnico atribuído.
- **`comentarios`**: Interações e atualizações técnicas vinculadas a chamados específicos, ordenadas cronologicamente.

---

## Tecnologias

- **Java 21 LTS**
- **Spring Boot 3.x**
  - Spring Data JPA
  - Spring Security 6.x
  - Spring Validation
- **MySQL 8.0** / Hibernate
- **JJWT (Java JWT) 0.12.x**
- **Lombok**
- **Maven**

---

## Estrutura do Projeto

```text
com.com.empresa.helpdesk/
├── config/              # SecurityFilterChain, TokenProvider e filtros JWT
├── controllers/         # Endpoints REST com anotações de validação e rotas
├── dtos/                # Records de transporte (Requests/Responses)
│   ├── auth/            # Payloads de login, registro e tokens
│   ├── chamados/        # DTOs de tickets e alteração de status
│   └── comentarios/     # DTOs de envio e retorno de interações
├── exceptions/          # Exceções customizadas de regras de negócio
├── infra/               # Interceptador global @RestControllerAdvice
├── models/              # Entidades JPA e Enums
│   └── enums/           # PerfilUsuarioEnum, StatusChamadoEnum, PrioridadeEnum
├── repositories/        # Repositórios com paginação e buscas derivadas
└── services/            # Camada transacional e regras de negócio
