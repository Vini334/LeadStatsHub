package com.vini334.dashboard.controller;

import com.vini334.dashboard.dto.UserGrowthPoint;
import com.vini334.dashboard.dto.WeeklyEngagementPoint;
import com.vini334.dashboard.service.AdvancedMetricsService;
import com.vini334.dashboard.service.OracleProcedureService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/metrics")
public class AdvancedMetricsController {
    private final AdvancedMetricsService metricsService;
    private final OracleProcedureService oracleProcedureService;

    public AdvancedMetricsController(AdvancedMetricsService metricsService,
                                     OracleProcedureService oracleProcedureService) {
        this.metricsService = metricsService;
        this.oracleProcedureService = oracleProcedureService;
    }

    @GetMapping("/user-growth-last6")
    public List<UserGrowthPoint> userGrowthLast6() {
        return metricsService.getUserGrowthLast6Months();
    }

    @GetMapping("/weekly-engagement")
    public List<WeeklyEngagementPoint> weeklyEngagement() {
        return metricsService.getWeeklyEngagement();
    }

    @GetMapping("/engagement-rate")
    public double engagementRate() {
        return metricsService.getEngagementRate();
    }

    @GetMapping("/avg-interactions")
    public Double avgInteractions() {
        return oracleProcedureService.getAvgInteractionsPerUser();
    }

    @GetMapping("/avg-engagement")
    public Double avgEngagement() {
        return oracleProcedureService.getAvgEngagementPerUser();
    }

    @GetMapping("/avg-new-users")
    public Double avgNewUsers(@RequestParam(value = "months", defaultValue = "6") int months) {
        return oracleProcedureService.getAvgNewUsersPerMonth(months);
    }

    @GetMapping("/daily-engagement")
    public Double dailyEngagement(@RequestParam("day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        return oracleProcedureService.getDailyEngagement(day);
    }

    @GetMapping("/user-summary")
    public Map<String, Object> userSummary(@RequestParam("userId") long userId) {
        return oracleProcedureService.getUserSummary(userId);
    }
}

