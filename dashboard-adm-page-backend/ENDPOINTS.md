# Endpoints da API - Dashboard SmartHas

## Endpoints Públicos (Sem Autenticação)

### Autenticação
- `POST /api/auth/login` - Login de usuário

### Posts
- `POST /api/posts` - Criar novo post
- `GET /api/posts` - Listar posts (se implementado)

### Interações
- `POST /api/interactions` - Registrar interação
- `GET /api/interactions` - Listar interações (se implementado)

### Dashboard
- `GET /api/dashboard/**` - Métricas do dashboard

### Métricas Avançadas
- `GET /api/metrics/**` - Métricas avançadas

### Health Check
- `GET /ping` - Verificar se a API está funcionando
- `GET /api/ping` - Verificar se a API está funcionando

### Documentação
- `GET /swagger-ui.html` - Interface Swagger UI
- `GET /v3/api-docs` - Documentação OpenAPI JSON

## Como Testar

### 1. Health Check
```bash
curl http://localhost:8080/ping
```

### 2. Swagger UI
Acesse: http://localhost:8080/swagger-ui.html

### 3. Criar Post
```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d '{"title":"Teste","content":"Conteúdo do teste"}' \
  -G -d "userId=1"
```

### 4. Criar Interação
```bash
curl -X POST http://localhost:8080/api/interactions \
  -H "Content-Type: application/json" \
  -d '{"postId":1,"userId":1,"type":"like"}'
```

### 5. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -d "email=admin@test.com&password=123456"
```

## Configuração de Segurança

Todos os endpoints listados acima estão configurados como públicos (permitAll) no SecurityConfig.

Para endpoints que precisam de autenticação, use o token JWT retornado pelo login no header:
```
Authorization: Bearer <token>
```
