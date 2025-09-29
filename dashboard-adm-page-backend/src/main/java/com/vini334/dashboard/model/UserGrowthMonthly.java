package com.vini334.dashboard.model;

import jakarta.persistence.*;

@Entity
@Table(name = "USER_GROWTH_MONTHLY")
public class UserGrowthMonthly {
    @Id
    @Column(name = "MONTH_YEAR")
    private String monthYear; // formato YYYY-MM

    @Column(name = "TOTAL_USERS")
    private Integer totalUsers;

    // Getters e Setters
    public String getMonthYear() { return monthYear; }
    public void setMonthYear(String monthYear) { this.monthYear = monthYear; }
    public Integer getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Integer totalUsers) { this.totalUsers = totalUsers; }
}
