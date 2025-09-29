package com.vini334.dashboard.controller;

import com.vini334.dashboard.dto.UserGrowthPoint;
import com.vini334.dashboard.dto.WeeklyEngagementPoint;
import com.vini334.dashboard.service.AdvancedMetricsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
public class AdvancedMetricsController {
    private final AdvancedMetricsService metricsService;
    private final com.vini334.dashboard.service.OracleProcedureService oracleProcedureService;

    public AdvancedMetricsController(AdvancedMetricsService metricsService,
                                     com.vini334.dashboard.service.OracleProcedureService oracleProcedureService) {
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
}