package com.vini334.dashboard.web;

import com.vini334.dashboard.infrastructure.oracle.OracleProcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Endpoints administrativos (Oracle PL/SQL)")
public class AdminController {

  private final OracleProcService oracleProcService;

  // === BEGIN GPT SECTION: /run-alerts ===
  @Operation(summary = "Executa PROC_EVALUATE_ENGAGEMENT_SENSORS no Oracle")
  @PostMapping("/run-alerts")
  public ResponseEntity<Map<String, Object>> runAlerts(
      @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
      @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
      @RequestParam(value = "multiplier", defaultValue = "3") int multiplier
  ) throws Exception {
    Map<String, Object> res = oracleProcService.runEngagementSensors(
        Timestamp.valueOf(from), Timestamp.valueOf(to), multiplier);
    return ResponseEntity.ok(res);
  }
  // === END GPT SECTION: /run-alerts ===

  // === BEGIN GPT SECTION: /user-consumption ===
  @Operation(summary = "Executa PROC_USER_CONSUMPTION_REPORT e retorna metricas por usuario")
  @PostMapping("/user-consumption")
  public ResponseEntity<Map<String, Object>> userConsumption(
      @RequestParam("userId") long userId,
      @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
      @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
  ) throws Exception {
    Map<String, Object> res = oracleProcService.userConsumptionReport(
        userId, Timestamp.valueOf(from), Timestamp.valueOf(to));
    return ResponseEntity.ok(res);
  }
  // === END GPT SECTION: /user-consumption ===
}
