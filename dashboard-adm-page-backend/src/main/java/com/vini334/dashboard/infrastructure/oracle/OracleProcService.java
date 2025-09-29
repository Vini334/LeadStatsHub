package com.vini334.dashboard.infrastructure.oracle;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OracleProcService {
  private final DataSource dataSource;

  // === BEGIN GPT SECTION: call PROC_EVALUATE_ENGAGEMENT_SENSORS ===
  public Map<String, Object> runEngagementSensors(Timestamp from, Timestamp to, int multiplier) throws Exception {
    try (Connection cn = dataSource.getConnection();
         CallableStatement cs = cn.prepareCall("{ call PROC_EVALUATE_ENGAGEMENT_SENSORS(?, ?, ?) }")) {
      cs.setTimestamp(1, from);
      cs.setTimestamp(2, to);
      cs.setInt(3, multiplier);
      cs.execute();
      Map<String, Object> res = new HashMap<>();
      res.put("status", "ok");
      res.put("from", from.toString());
      res.put("to", to.toString());
      res.put("multiplier", multiplier);
      return res;
    }
  }
  // === END GPT SECTION: call PROC_EVALUATE_ENGAGEMENT_SENSORS ===

  // === BEGIN GPT SECTION: call PROC_USER_CONSUMPTION_REPORT ===
  public Map<String, Object> userConsumptionReport(long userId, Timestamp from, Timestamp to) throws Exception {
    try (Connection cn = dataSource.getConnection();
         CallableStatement cs = cn.prepareCall("{ call PROC_USER_CONSUMPTION_REPORT(?, ?, ?, ?) }")) {
      cs.setLong(1, userId);
      cs.setTimestamp(2, from);
      cs.setTimestamp(3, to);
      cs.registerOutParameter(4, Types.REF_CURSOR);
      cs.execute();

      Map<String, Object> body = new HashMap<>();
      try (ResultSet rs = (ResultSet) cs.getObject(4)) {
        if (rs != null && rs.next()) {
          body.put("userId", rs.getLong("USER_ID"));
          body.put("from", rs.getTimestamp("PERIOD_FROM"));
          body.put("to", rs.getTimestamp("PERIOD_TO"));
          body.put("posts", rs.getLong("POSTS_COUNT"));
          body.put("interactions", rs.getLong("INTERACTIONS_COUNT"));
          body.put("likes", rs.getLong("LIKES_COUNT"));
          body.put("likeRatio", rs.getBigDecimal("LIKE_RATIO"));
          try {
            body.put("error", rs.getString("ERROR_MSG"));
          } catch (SQLException ignore) {
            // coluna opcional
          }
        } else {
          body.put("message", "no data");
        }
      }
      return body;
    }
  }
  // === END GPT SECTION: call PROC_USER_CONSUMPTION_REPORT ===
}
