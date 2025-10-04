# Banco Oracle - Smart HAS

Este projeto inclui os artefatos completos do banco Oracle utilizados pelo Smart HAS. Use estes materiais para criar o schema, importar dados simulados e consultar o DER oficial.

## Estrutura dos arquivos (`db/`)
- `db/schema.sql`: DDL exportada do Oracle com sequences, tabelas, chaves e objetos PL/SQL (`FN_*`, `PROC_*`).
- `db/data.sql`: conjunto de dados de demonstracao para popular usuarios, posts, interacoes, metricas semanais e alertas.
- `db/Diagrama.pdf` e `db/Diagrama(Relatorio).html`: DER e relatorio gerados a partir do Oracle Data Modeler.

## Como aplicar o schema e os dados
1. Conecte-se ao banco Oracle da FIAP (ou ao seu ambiente local) usando `sqlplus`, SQL Developer ou outra ferramenta.
2. Execute o script de schema:
   ```sql
   @/caminho/para/db/schema.sql
   ```
3. Em seguida aplique os dados de exemplo:
   ```sql
   @/caminho/para/db/data.sql
   ```
4. Verifique se as funcoes e procedures foram compiladas:
   ```sql
   SELECT object_name, object_type, status
     FROM user_objects
    WHERE object_type IN ('FUNCTION','PROCEDURE')
    ORDER BY object_type, object_name;
   ```

> **Dica:** no arquivo `src/main/resources/application.properties` ha a flag `app.oracle.bootstrap`. Deixe-a ligada apenas se quiser que a aplicacao recompile automaticamente os objetos PL/SQL em tempo de execucao; caso contrario, mantenha `false` depois de executar os scripts acima.

## Funcoes PL/SQL disponiveis
- `AVG_INTERACTIONS_PER_USER()` ? indicador medio de interacoes por usuario. Exemplo:
  ```sql
  SELECT AVG_INTERACTIONS_PER_USER() AS avg_interactions FROM dual;
  ```
- `FN_AVG_ENGAGEMENT_PER_USER()` ? replica a media de engajamento, com tratamento de excecao para datasets vazios.
- `FN_AVG_NEW_USERS_PER_MONTH(p_months IN PLS_INTEGER)` ? media de novos usuarios no periodo informado.
- `FN_DAILY_ENGAGEMENT(p_day IN DATE)` ? taxa de engajamento diaria (interacoes / usuarios ativos).
- `FN_USER_SUMMARY_JSON(p_user_id IN NUMBER)` ? retorna um JSON com resumo de atividade do usuario.

## Procedures PL/SQL disponiveis
- `PROC_EVALUATE_ENGAGEMENT_SENSORS(p_from, p_to, p_multiplier)` ? avalia o periodo informado, usa um cursor/loop para identificar o pico de interacoes e grava um alerta no banco. Integrada ao endpoint `POST /api/admin/run-alerts`.
- `PROC_USER_CONSUMPTION_REPORT(p_user_id, p_from, p_to, p_out)` ? agrega posts e interacoes por tipo usando loop, retornando um cursor consumido na API. Integrada ao endpoint `POST /api/admin/user-consumption`.

Exemplos:
```sql
BEGIN
  PROC_EVALUATE_ENGAGEMENT_SENSORS(p_from => DATE '2025-06-01',
                                   p_to   => DATE '2025-06-07',
                                   p_multiplier => 3);
END;
/

VAR rc REFCURSOR;
EXEC PROC_USER_CONSUMPTION_REPORT(1, DATE '2025-06-01', DATE '2025-06-30', :rc);
PRINT rc;
```

## Endpoints que consomem PL/SQL
- `POST /api/admin/run-alerts` (Procedure de alertas)
- `POST /api/admin/user-consumption` (Procedure de relatorio por usuario)
- `GET  /api/metrics/avg-interactions` (Function `AVG_INTERACTIONS_PER_USER`)
- `GET  /api/metrics/avg-engagement` (Function `FN_AVG_ENGAGEMENT_PER_USER`)
- `GET  /api/metrics/avg-new-users?months=6` (Function `FN_AVG_NEW_USERS_PER_MONTH`)
- `GET  /api/metrics/daily-engagement?day=2025-06-15` (Function `FN_DAILY_ENGAGEMENT`)
- `GET  /api/metrics/user-summary?userId=1` (Function `FN_USER_SUMMARY_JSON`)

Com estes arquivos e endpoints o projeto atende aos requisitos da Fase 2 e da Fase 3: schema Oracle documentado, objetos PL/SQL integrados ao back-end (com uso de cursor, loop, tratamento de excecao), dados simulados disponiveis e DER oficial anexado.
