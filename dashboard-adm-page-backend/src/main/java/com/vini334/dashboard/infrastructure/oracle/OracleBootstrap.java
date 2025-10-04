package com.vini334.dashboard.infrastructure.oracle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Slf4j
@Component
@RequiredArgsConstructor
public class OracleBootstrap {

  private final DataSource dataSource;

  @Value("${app.oracle.bootstrap:false}")
  private boolean bootstrap;

  @PostConstruct
  public void init() {
    if (!bootstrap) {
      log.info("[OracleBootstrap] Bootstrap desabilitado (app.oracle.bootstrap=false).");
      return;
    }
    log.info("[OracleBootstrap] Iniciando bootstrap Oracle...");

    // === BEGIN GPT SECTION: DDL/DML PL/SQL STRINGS ===
    String ddlAlerts = """
      BEGIN
        EXECUTE IMMEDIATE 'CREATE TABLE ALERTS (
          ALERT_ID   NUMBER PRIMARY KEY,
          ALERT_TYPE VARCHAR2(50) NOT NULL,
          SEVERITY   VARCHAR2(20) NOT NULL,
          DETAILS    CLOB,
          CREATED_AT DATE DEFAULT SYSDATE,
          REF_USER_ID NUMBER,
          REF_POST_ID NUMBER
        )';
      EXCEPTION WHEN OTHERS THEN
        IF SQLCODE != -955 THEN RAISE; END IF;
      END;
      """;

    String seqAlerts = """
      BEGIN
        EXECUTE IMMEDIATE 'CREATE SEQUENCE ALERTS_SEQ START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE';
      EXCEPTION WHEN OTHERS THEN
        IF SQLCODE != -955 THEN RAISE; END IF;
      END;
      """;

    String fnAvgNewUsers = """
      CREATE OR REPLACE FUNCTION FN_AVG_NEW_USERS_PER_MONTH(p_months IN PLS_INTEGER DEFAULT 6)
      RETURN NUMBER IS
        v_avg NUMBER := 0;
      BEGIN
        -- Calculates the average of new users per month within the informed window.
        SELECT AVG(cnt)
          INTO v_avg
          FROM (
            SELECT COUNT(*) cnt
              FROM USERS
             WHERE CREATED_AT >= ADD_MONTHS(TRUNC(SYSDATE,'MM'), -p_months + 1)
             GROUP BY TRUNC(CREATED_AT,'MM')
          );

        RETURN NVL(v_avg,0);
      EXCEPTION
        WHEN NO_DATA_FOUND THEN RETURN 0;
        WHEN OTHERS THEN RETURN 0;
      END;
      """;

    String fnDailyEngagement = """
      CREATE OR REPLACE FUNCTION FN_DAILY_ENGAGEMENT(p_day IN DATE)
      RETURN NUMBER IS
        v_interactions NUMBER := 0;
        v_active_users NUMBER := 0;
      BEGIN
        -- Engagement rate for a given day (interactions divided by active users).
        SELECT COUNT(*)
          INTO v_interactions
          FROM INTERACTIONS
         WHERE TRUNC(CREATED_AT)=TRUNC(p_day);

        SELECT COUNT(DISTINCT user_id)
          INTO v_active_users
          FROM (
            SELECT user_id FROM POSTS WHERE TRUNC(CREATED_AT)=TRUNC(p_day)
            UNION ALL
            SELECT user_id FROM INTERACTIONS WHERE TRUNC(CREATED_AT)=TRUNC(p_day)
          );
        IF v_active_users=0 THEN RETURN 0; END IF;
        RETURN v_interactions / v_active_users;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN RETURN 0;
        WHEN OTHERS THEN RETURN 0;
      END;
      """;

    String fnAvgEngPerUser = """
      CREATE OR REPLACE FUNCTION FN_AVG_ENGAGEMENT_PER_USER
      RETURN NUMBER IS
        v_avg NUMBER := 0;
      BEGIN
        -- Calculates engagement considering total interactions per user.
        SELECT AVG(cnt)
          INTO v_avg
          FROM (
            SELECT COUNT(*) cnt FROM INTERACTIONS GROUP BY USER_ID
          );
        RETURN NVL(v_avg,0);
      EXCEPTION
        WHEN NO_DATA_FOUND THEN RETURN 0;
        WHEN OTHERS THEN RETURN 0;
      END;
      """;

    String fnUserSummaryJson = """
      CREATE OR REPLACE FUNCTION FN_USER_SUMMARY_JSON(p_user_id IN NUMBER)
      RETURN CLOB IS
        v_posts NUMBER:=0; v_interactions NUMBER:=0; v_likes NUMBER:=0; v_first_seen DATE;
        v_json_vc2 VARCHAR2(32767); v_json_clob CLOB;
      BEGIN
        -- Returns a JSON payload summarising the user activity.
        SELECT MIN(CREATED_AT) INTO v_first_seen FROM USERS WHERE USER_ID=p_user_id;
        SELECT COUNT(*) INTO v_posts FROM POSTS WHERE USER_ID=p_user_id;
        SELECT COUNT(*) INTO v_interactions FROM INTERACTIONS WHERE USER_ID=p_user_id;
        SELECT COUNT(*) INTO v_likes FROM INTERACTIONS WHERE USER_ID=p_user_id AND UPPER(TYPE)='LIKE';
        SELECT JSON_OBJECT(KEY 'userId' VALUE p_user_id,
                           KEY 'firstSeen' VALUE TO_CHAR(v_first_seen,'YYYY-MM-DD" "HH24:MI:SS'),
                           KEY 'posts' VALUE v_posts,
                           KEY 'interactions' VALUE v_interactions,
                           KEY 'likes' VALUE v_likes)
          INTO v_json_vc2 FROM dual;
        v_json_clob := TO_CLOB(v_json_vc2);
        RETURN v_json_clob;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN RETURN TO_CLOB('{"error":"USER_NOT_FOUND"}');
        WHEN OTHERS THEN RETURN TO_CLOB('{"error":"' || REPLACE(SUBSTR(SQLERRM,1,1000),'"','\"') || '"}');
      END;
      """;

    String procSensors = """
      CREATE OR REPLACE PROCEDURE PROC_EVALUATE_ENGAGEMENT_SENSORS(
        p_from IN DATE, p_to IN DATE, p_multiplier IN NUMBER DEFAULT 3
      ) AS
        /*
          Procedure: PROC_EVALUATE_ENGAGEMENT_SENSORS
          Purpose : Evaluate interaction rates in a period and persist alerts when a spike is detected.
          Notes   : Uses a cursor to inspect the busiest day inside the window.
        */
        CURSOR c_daily_interactions IS
          SELECT TRUNC(created_at) AS interaction_day,
                 COUNT(*) AS interactions_qtd
            FROM INTERACTIONS
           WHERE CREATED_AT>=p_from AND CREATED_AT<p_to
           GROUP BY TRUNC(created_at);
        v_current_rate NUMBER:=0; v_baseline NUMBER:=0;
        v_peak_date DATE;
        v_peak_total NUMBER := 0;
        v_details_vc2 VARCHAR2(32767); v_details_clob CLOB; v_alert_id NUMBER;
      BEGIN
        IF p_from IS NULL OR p_to IS NULL OR p_to<=p_from THEN
          RAISE_APPLICATION_ERROR(-20001,'Janela invalida');
        END IF;

        SELECT NVL(COUNT(*),0)/GREATEST(((p_to-p_from)*24),1)
          INTO v_current_rate FROM INTERACTIONS
         WHERE CREATED_AT>=p_from AND CREATED_AT<p_to;

        SELECT NVL(COUNT(*),0)/GREATEST((((p_from-(p_from-7))*24)),1)
          INTO v_baseline FROM INTERACTIONS
         WHERE CREATED_AT>=(p_from-7) AND CREATED_AT<p_from;

        IF v_baseline=0 AND v_current_rate>0 THEN v_baseline:=0.1; END IF;

        FOR daily_rec IN c_daily_interactions LOOP
          IF daily_rec.interactions_qtd > v_peak_total THEN
            v_peak_total := daily_rec.interactions_qtd;
            v_peak_date := daily_rec.interaction_day;
          END IF;
        END LOOP;

        IF v_current_rate >= p_multiplier * v_baseline THEN
          SELECT JSON_OBJECT(KEY 'from' VALUE TO_CHAR(p_from,'YYYY-MM-DD" "HH24:MI:SS'),
                             KEY 'to' VALUE TO_CHAR(p_to,'YYYY-MM-DD" "HH24:MI:SS'),
                             KEY 'currentRate' VALUE v_current_rate,
                             KEY 'baseline' VALUE v_baseline,
                             KEY 'multiplier' VALUE p_multiplier,
                             KEY 'peakDay' VALUE TO_CHAR(v_peak_date,'YYYY-MM-DD'),
                             KEY 'peakInteractions' VALUE v_peak_total)
            INTO v_details_vc2 FROM dual;
          v_details_clob:=TO_CLOB(v_details_vc2);
          v_alert_id := ALERTS_SEQ.NEXTVAL;
          INSERT INTO ALERTS(ALERT_ID,ALERT_TYPE,SEVERITY,DETAILS,CREATED_AT)
          VALUES(v_alert_id,'ENGAGEMENT_SPIKE','HIGH',v_details_clob,SYSDATE);
        END IF;
      EXCEPTION
        WHEN OTHERS THEN
          v_details_clob := TO_CLOB('{'||
            '"error":"' || REPLACE(SUBSTR(SQLERRM,1,1000),'"','\"') || '",'||
            '"from":"'||TO_CHAR(p_from,'YYYY-MM-DD" "HH24:MI:SS')||'",'||
            '"to":"'  ||TO_CHAR(p_to  ,'YYYY-MM-DD" "HH24:MI:SS')||'",'||
            '"multiplier":'||NVL(TO_CHAR(p_multiplier),'null')||'}');
          INSERT INTO ALERTS(ALERT_ID,ALERT_TYPE,SEVERITY,DETAILS,CREATED_AT)
          VALUES(ALERTS_SEQ.NEXTVAL,'ENGAGEMENT_SPIKE_ERROR','LOW',v_details_clob,SYSDATE);
      END;
      """;

    String procUserReport = """
      CREATE OR REPLACE PROCEDURE PROC_USER_CONSUMPTION_REPORT(
        p_user_id IN NUMBER, p_from IN DATE, p_to IN DATE, p_out OUT SYS_REFCURSOR
      ) AS
        /*
          Procedure: PROC_USER_CONSUMPTION_REPORT
          Purpose : Produce a consumption summary per user and period, returning a cursor for integration.
          Notes   : Aggregates interaction types inside a loop for clarity.
        */
        v_posts NUMBER:=0; v_interactions NUMBER:=0; v_likes NUMBER:=0;
        v_comments NUMBER := 0; v_shares NUMBER := 0;
        v_errmsg VARCHAR2(2000);
      BEGIN
        IF p_user_id IS NULL OR p_from IS NULL OR p_to IS NULL OR p_to<=p_from THEN
          RAISE_APPLICATION_ERROR(-20002,'Parametros invalidos');
        END IF;

        SELECT COUNT(*) INTO v_posts FROM POSTS
         WHERE USER_ID=p_user_id AND CREATED_AT>=p_from AND CREATED_AT<p_to;

        FOR summary_rec IN (
          SELECT UPPER(TYPE) AS interaction_type,
                 COUNT(*) AS total
            FROM INTERACTIONS
           WHERE USER_ID=p_user_id AND CREATED_AT>=p_from AND CREATED_AT<p_to
           GROUP BY UPPER(TYPE)
        ) LOOP
          CASE summary_rec.interaction_type
            WHEN 'LIKE' THEN v_likes := summary_rec.total;
            WHEN 'COMMENT' THEN v_comments := summary_rec.total;
            WHEN 'SHARE' THEN v_shares := summary_rec.total;
          END CASE;
        END LOOP;

        v_interactions := v_likes + v_comments + v_shares;

        OPEN p_out FOR
          SELECT p_user_id USER_ID, p_from PERIOD_FROM, p_to PERIOD_TO,
                 v_posts POSTS_COUNT, v_interactions INTERACTIONS_COUNT, v_likes LIKES_COUNT,
                 v_comments COMMENTS_COUNT, v_shares SHARES_COUNT,
                 CASE WHEN v_interactions=0 THEN 0 ELSE v_likes/v_interactions END LIKE_RATIO
          FROM dual;
      EXCEPTION
        WHEN OTHERS THEN
          v_errmsg := REPLACE(SUBSTR(SQLERRM,1,2000),'"','\"');
          OPEN p_out FOR
            SELECT p_user_id USER_ID, p_from PERIOD_FROM, p_to PERIOD_TO,
                   -1 POSTS_COUNT, -1 INTERACTIONS_COUNT, -1 LIKES_COUNT,
                   -1 COMMENTS_COUNT, -1 SHARES_COUNT,
                   0 LIKE_RATIO, v_errmsg ERROR_MSG
            FROM dual;
      END;
      """;
    // === END GPT SECTION: DDL/DML PL/SQL STRINGS ===

    try (Connection cn = dataSource.getConnection(); Statement st = cn.createStatement()) {
      st.execute(ddlAlerts);
      st.execute(seqAlerts);
      st.execute(fnAvgNewUsers);
      st.execute(fnDailyEngagement);
      st.execute(fnAvgEngPerUser);
      st.execute(fnUserSummaryJson);
      st.execute(procSensors);
      st.execute(procUserReport);
      log.info("[OracleBootstrap] Objetos Oracle garantidos/compilados com sucesso.");
    } catch (Exception e) {
      log.error("[OracleBootstrap] Falha ao bootstrapar Oracle", e);
    }
  }
}
