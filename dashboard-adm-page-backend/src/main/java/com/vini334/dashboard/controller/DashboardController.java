package com.vini334.dashboard.controller;

import com.vini334.dashboard.service.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/total-users")
    public long totalUsers() {
        return dashboardService.getTotalUsers();
    }

    @GetMapping("/total-posts")
    public long totalPosts() {
        return dashboardService.getTotalPosts();
    }

    @GetMapping("/total-likes")
    public long totalLikes() {
        return dashboardService.getTotalLikes();
    }
}
