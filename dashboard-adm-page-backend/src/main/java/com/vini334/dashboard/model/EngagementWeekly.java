package com.vini334.dashboard.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ENGAGEMENT_WEEKLY")
public class EngagementWeekly {
    @Id
    @Column(name = "WEEK_ID")
    private Long weekId;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "TOTAL_LIKES")
    private Integer totalLikes;

    @Column(name = "TOTAL_COMMENTS")
    private Integer totalComments;

    @Column(name = "TOTAL_SHARES")
    private Integer totalShares;

    // Getters e Setters
    public Long getWeekId() { return weekId; }
    public void setWeekId(Long weekId) { this.weekId = weekId; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Integer getTotalLikes() { return totalLikes; }
    public void setTotalLikes(Integer totalLikes) { this.totalLikes = totalLikes; }
    public Integer getTotalComments() { return totalComments; }
    public void setTotalComments(Integer totalComments) { this.totalComments = totalComments; }
    public Integer getTotalShares() { return totalShares; }
    public void setTotalShares(Integer totalShares) { this.totalShares = totalShares; }
}
