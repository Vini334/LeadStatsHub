package com.vini334.dashboard.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

@Service
public class OracleProcedureService {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public OracleProcedureService(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    public Double getAvgInteractionsPerUser() {
        return jdbcTemplate.queryForObject(
                "SELECT AVG_INTERACTIONS_PER_USER() FROM DUAL",
                Double.class
        );
    }

    public Double getAvgEngagementPerUser() {
        return jdbcTemplate.queryForObject(
                "SELECT FN_AVG_ENGAGEMENT_PER_USER() FROM DUAL",
                Double.class
        );
    }

    public Double getAvgNewUsersPerMonth(int months) {
        return jdbcTemplate.queryForObject(
                "SELECT FN_AVG_NEW_USERS_PER_MONTH(?) FROM DUAL",
                Double.class,
                months
        );
    }

    public Double getDailyEngagement(LocalDate day) {
        return jdbcTemplate.queryForObject(
                "SELECT FN_DAILY_ENGAGEMENT(?) FROM DUAL",
                Double.class,
                Date.valueOf(day)
        );
    }

    public Map<String, Object> getUserSummary(long userId) {
        String payload = jdbcTemplate.queryForObject(
                "SELECT FN_USER_SUMMARY_JSON(?) AS SUMMARY FROM DUAL",
                (rs, rowNum) -> clobToString(rs.getClob("SUMMARY")),
                userId
        );
        if (payload == null || payload.isBlank()) {
            return Map.of();
        }
        try {
            return objectMapper.readValue(payload, new TypeReference<>() {});
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao converter retorno de FN_USER_SUMMARY_JSON", e);
        }
    }

    private String clobToString(Clob clob) {
        if (clob == null) {
            return null;
        }
        try {
            long length = clob.length();
            if (length == 0) {
                return "";
            }
            return clob.getSubString(1, (int) length);
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao ler CLOB retornado pelo Oracle", e);
        }
    }
}


