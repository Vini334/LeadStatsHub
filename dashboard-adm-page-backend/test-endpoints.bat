@echo off
echo Testando endpoints da API...

echo.
echo 1. Testando Health Check...
curl -X GET http://localhost:8080/ping

echo.
echo 2. Testando Swagger UI...
curl -X GET http://localhost:8080/swagger-ui.html

echo.
echo 3. Testando Login...
curl -X POST http://localhost:8080/api/auth/login -d "email=admin@test.com&password=123456"

echo.
echo 4. Testando Criar Post...
curl -X POST http://localhost:8080/api/posts -H "Content-Type: application/json" -d "{\"title\":\"Teste Post\",\"content\":\"Conteúdo do teste\"}" -G -d "userId=1"

echo.
echo 5. Testando Criar Interação...
curl -X POST http://localhost:8080/api/interactions -H "Content-Type: application/json" -d "{\"postId\":1,\"userId\":1,\"type\":\"like\"}"

echo.
echo Testes concluídos!
pause
