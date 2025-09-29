package com.vini334.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class OracleProcedureService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Exemplo de chamada a função Oracle que retorna um inteiro
    public int getEngajamentoTotal() {
        // Supondo que exista uma função chamada CALC_ENGAJAMENTO_TOTAL no Oracle
        return jdbcTemplate.queryForObject("SELECT CALC_ENGAJAMENTO_TOTAL() FROM DUAL", Integer.class);
    }

    // Exemplo de chamada a procedure Oracle
    public int callProcedureExemplo(int param) {
        // Supondo que exista uma procedure chamada PROC_EXEMPLO que recebe um parâmetro
        return jdbcTemplate.queryForObject("CALL PROC_EXEMPLO(?)", Integer.class, param);
    }

    // Chamada para média de interações por usuário
    public Double getAvgInteractionsPerUser() {
        return jdbcTemplate.queryForObject("SELECT AVG_INTERACTIONS_PER_USER() FROM DUAL", Double.class);
    }
}
