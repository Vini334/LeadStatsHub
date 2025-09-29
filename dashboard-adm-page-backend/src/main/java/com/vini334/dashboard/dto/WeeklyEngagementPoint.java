package com.vini334.dashboard.dto;

public record WeeklyEngagementPoint(
    Long weekId,
    String weekLabel,
    String startDate,
    String endDate,
    long totalLikes,
    long totalComments,
    long totalShares
) {}