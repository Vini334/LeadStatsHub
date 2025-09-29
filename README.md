# Dashboard SmartHas

Dashboard SmartHas e uma solucao full stack para monitorar e administrar interacoes de usuarios, posts e metricas de engajamento. O repositorio contem um backend Spring Boot conectado a Oracle Database e uma interface Angular focada em visualizacao de dados para a equipe administrativa.

## Sumario
- [Visao geral](#visao-geral)
- [Arquitetura do repositorio](#arquitetura-do-repositorio)
- [Tecnologias principais](#tecnologias-principais)
- [Pre-requisitos](#pre-requisitos)
- [Backend Spring Boot](#backend-spring-boot)
  - [Configuracao](#configuracao)
  - [Execucao local](#execucao-local)
  - [Endpoints chave](#endpoints-chave)
  - [Recursos Oracle](#recursos-oracle)
- [Front-end Angular](#front-end-angular)
- [Scripts uteis](#scripts-uteis)
- [Boas praticas e proximos passos](#boas-praticas-e-proximos-passos)

## Visao geral
- API REST exposta em `/api` que oferece cadastro de posts, interacoes e usuarios, alem de metricas agregadas.
- Integra com procedures e funcoes PL/SQL para consolidar informacoes de engajamento diretamente no Oracle Database.
- Front-end Angular com graficos (ApexCharts) e Tailwind/Flowbite para visualizacao responsiva.
- Documentacao automatica gerada via Swagger UI (`/swagger-ui.html`).

## Arquitetura do repositorio
```
Dashboard-SmartHas/
|-- dashboard-adm-page-backend/       # API Spring Boot 3.5.x
|   |-- src/main/java/com/vini334/... # controllers, services, security e integracoes Oracle
|   |-- src/main/resources/           # application.properties e assets
|   |-- ENDPOINTS.md                  # resumo rapido das rotas
|   `-- test-endpoints.bat            # script PowerShell/CMD com chamadas curl
|-- ProjetoAngular/
|   `-- lead-stats-hub-ng/            # aplicacao Angular 20 (Tailwind + ApexCharts)
`-- README.md                         # este arquivo
```

## Tecnologias principais
- **Backend:** Java 17, Spring Boot 3.5.6, Spring Data JPA, Spring Security, Springdoc OpenAPI, Lombok, JJWT, Oracle JDBC.
- **Banco:** Oracle Database (schema RM563326 por padrao) com procedures, funcoes e tabelas criadas sob demanda.
- **Frontend:** Angular CLI 20, SSR opcional, TailwindCSS, Flowbite, ApexCharts, Lucide Icons.
- **Testes:** Spring Boot Starter Test (JUnit), Karma e Jasmine no Angular.

## Pre-requisitos
- Java 17 (ou superior) e Maven 3.9+ (ou uso do wrapper `mvnw`/`mvnw.cmd`).
- Node.js 20 e npm 10 para o front-end (instale Angular CLI global se desejar: `npm install -g @angular/cli`).
- Instancia Oracle acessivel (credenciais configuradas via variaveis de ambiente ou `application.properties`).
- Ferramenta `curl` (opcional) para testar endpoints rapidamente.

## Backend Spring Boot

### Configuracao
Arquivo principal: `dashboard-adm-page-backend/src/main/resources/application.properties`.
Ajuste conforme o ambiente:

```properties
spring.datasource.url=jdbc:oracle:thin:@<host>:<porta>:<SID>
spring.datasource.username=<usuario>
spring.datasource.password=<senha>
spring.jpa.hibernate.ddl-auto=none
spring.jpa.properties.hibernate.default_schema=<schema>
app.oracle.bootstrap=true
```

Recomendacoes:
- Substitua credenciais hard-coded por variaveis de ambiente ou segredos gerenciados.
- Atualize `app.oracle.bootstrap` para `false` em ambientes onde os objetos PL/SQL ja existirem (o bootstrap recria funcoes/tabelas ao subir a aplicacao).
- Troque o valor de `SECRET_KEY` em `JwtUtil` por uma chave forte carregada externamente.

### Execucao local
```bash
# a partir da raiz do backend
dashboard-adm-page-backend/mvnw spring-boot:run     # Windows
dashboard-adm-page-backend/./mvnw spring-boot:run   # Linux ou Mac
```

Endpoints de verificacao rapida:
- `GET http://localhost:8080/ping` retorna `"pong"`.
- `GET http://localhost:8080/swagger-ui.html` carrega a documentacao.

Para rodar testes automatizados:
```bash
dashboard-adm-page-backend/./mvnw test
```

### Endpoints chave
| Metodo | Rota | Descricao |
| ------ | ---- | --------- |
| POST | `/api/auth/login` | Autenticacao via email/senha retornando JWT |
| GET | `/api/dashboard/total-users` | Total de usuarios cadastrados |
| GET | `/api/dashboard/total-posts` | Total de posts registrados |
| GET | `/api/dashboard/total-likes` | Total de curtidas |
| POST | `/api/posts?userId={id}` | Cria post associado a um usuario existente |
| POST | `/api/interactions` | Registra interacao (like/comment/share) |
| GET | `/api/metrics/user-growth-last6` | Serie historica de crescimento de usuarios |
| GET | `/api/metrics/weekly-engagement` | Engajamento semanal agregado |
| GET | `/api/metrics/avg-interactions` | Media de interacoes por usuario (Oracle) |
| GET | `/api/usuarios` | Lista usuarios (DTO sem senha) |
| POST/PUT/DELETE | `/api/usuarios` | CRUD basico de usuarios |
| POST | `/api/admin/run-alerts` | Executa `PROC_EVALUATE_ENGAGEMENT_SENSORS` (janela temporal obrigatoria) |
| POST | `/api/admin/user-consumption` | Executa `PROC_USER_CONSUMPTION_REPORT` |
| GET | `/swagger-ui.html` / `/v3/api-docs` | Documentacao interativa / OpenAPI JSON |

A classe `SecurityConfig` atualmente permite acesso publico a todas as rotas, mas ja possui infraestrutura de autenticacao via `JwtRequestFilter` e `UserDetailsServiceImpl`. Ajuste as `requestMatchers` para exigir `authenticated()` conforme necessario.

### Recursos Oracle
- `OracleBootstrap` (habilitado via `app.oracle.bootstrap=true`) garante a existencia de tabelas, funcoes e procedures utilitarias (`FN_AVG_NEW_USERS_PER_MONTH`, `PROC_EVALUATE_ENGAGEMENT_SENSORS`, `PROC_USER_CONSUMPTION_REPORT`, entre outras).
- `OracleProcService` encapsula chamadas via JDBC (`CallableStatement`) e e exposta pelo `AdminController`.
- `OracleProcedureService` oferece exemplos de consulta direta a funcoes PL/SQL como `AVG_INTERACTIONS_PER_USER`.
- Todos os acessos dependem do schema configurado em `spring.jpa.properties.hibernate.default_schema`.

## Front-end Angular
Diretorio: `ProjetoAngular/lead-stats-hub-ng`.

Instalacao e execucao:
```bash
cd ProjetoAngular/lead-stats-hub-ng
npm install
npm start          # alias para ng serve
```

A aplicacao fica disponivel em `http://localhost:4200/`. Para build de producao:
```bash
npm run build
```

Principais stacks do front:
- Angular 20 com suporte a SSR (`@angular/ssr`) e Express.
- TailwindCSS e Flowbite para componentes responsivos.
- ApexCharts (via `ng-apexcharts`) para graficos de crescimento e engajamento.

## Scripts uteis
- `dashboard-adm-page-backend/test-endpoints.bat` executa uma sequencia de chamadas `curl` (health check, login, criacao de post e interacao).
- `dashboard-adm-page-backend/ENDPOINTS.md` resume rotas e exemplos de `curl`.

## Boas praticas e proximos passos
- Centralize secrets (Oracle e JWT) em variaveis de ambiente ou providers de segredos.
- Considere proteger rotas administrativas exigindo token Bearer ou papeis especificos.
- Adicione testes de servico/controlador para cobrir fluxos principais e endpoints Oracle.
- Documente contratos de payload diretamente nas classes DTO com anotacoes `@Schema` (springdoc) para enriquecer o Swagger.
- Avalie configurar pipelines CI para rodar `mvn test` e `npm test` automaticamente.
