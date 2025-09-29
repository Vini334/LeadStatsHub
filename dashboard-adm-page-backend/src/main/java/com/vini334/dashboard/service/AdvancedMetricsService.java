package com.vini334.dashboard.service;

import com.vini334.dashboard.dto.UserGrowthPoint;
import com.vini334.dashboard.dto.WeeklyEngagementPoint;
import com.vini334.dashboard.model.EngagementWeekly;
import com.vini334.dashboard.model.UserGrowthMonthly;
import com.vini334.dashboard.model.Interaction;
import com.vini334.dashboard.model.User;
import com.vini334.dashboard.repository.UserGrowthMonthlyRepository;
import com.vini334.dashboard.repository.EngagementWeeklyRepository;
import com.vini334.dashboard.repository.InteractionRepository;
import com.vini334.dashboard.repository.PostRepository;
import com.vini334.dashboard.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdvancedMetricsService {
    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final WeekFields WEEK_FIELDS = WeekFields.ISO;

    private final UserGrowthMonthlyRepository userGrowthMonthlyRepository;
    private final EngagementWeeklyRepository engagementWeeklyRepository;
    private final InteractionRepository interactionRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public AdvancedMetricsService(UserGrowthMonthlyRepository userGrowthMonthlyRepository,
                                  EngagementWeeklyRepository engagementWeeklyRepository,
                                  InteractionRepository interactionRepository,
                                  PostRepository postRepository,
                                  UserRepository userRepository) {
        this.userGrowthMonthlyRepository = userGrowthMonthlyRepository;
        this.engagementWeeklyRepository = engagementWeeklyRepository;
        this.interactionRepository = interactionRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<UserGrowthPoint> getUserGrowthLast6Months() {
        try {
            List<UserGrowthMonthly> rows = userGrowthMonthlyRepository.findTop6ByOrderByMonthYearDesc();
            if (rows != null && !rows.isEmpty()) {
                return rows.stream()
                        .sorted(Comparator.comparing(UserGrowthMonthly::getMonthYear))
                        .map(row -> new UserGrowthPoint(row.getMonthYear(), valueOrZero(row.getTotalUsers())))
                        .collect(Collectors.toList());
            }
        } catch (Exception ex) {
            log.warn("Falha ao consultar USER_GROWTH_MONTHLY, usando fallback calculado", ex);
        }
        return computeUserGrowthFromUsers();
    }

    public List<WeeklyEngagementPoint> getWeeklyEngagement() {
        try {
            List<EngagementWeekly> rows = engagementWeeklyRepository.findAll();
            if (rows != null && !rows.isEmpty()) {
                return rows.stream()
                        .sorted(Comparator.comparing(EngagementWeekly::getWeekId))
                        .map(row -> new WeeklyEngagementPoint(
                                row.getWeekId(),
                                weekLabel(row.getWeekId(), row.getStartDate()),
                                toString(row.getStartDate()),
                                toString(row.getEndDate()),
                                valueOrZero(row.getTotalLikes()),
                                valueOrZero(row.getTotalComments()),
                                valueOrZero(row.getTotalShares())
                        ))
                        .collect(Collectors.toList());
            }
        } catch (Exception ex) {
            log.warn("Falha ao consultar ENGAGEMENT_WEEKLY, usando fallback calculado", ex);
        }
        return computeWeeklyEngagementFromInteractions();
    }

    public double getEngagementRate() {
        long totalLikes = interactionRepository.countByType("LIKE");
        long totalComments = interactionRepository.countByType("COMMENT");
        long totalShares = interactionRepository.countByType("SHARE");
        long totalPosts = postRepository.count();
        if (totalPosts == 0) {
            return 0.0;
        }
        return (double) (totalLikes + totalComments + totalShares) / totalPosts;
    }

    private List<UserGrowthPoint> computeUserGrowthFromUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        LocalDate startMonth = LocalDate.now().withDayOfMonth(1).minusMonths(5);
        Map<String, Long> totals = new LinkedHashMap<>();
        for (int i = 0; i < 6; i++) {
            String key = startMonth.plusMonths(i).format(MONTH_FORMAT);
            totals.put(key, 0L);
        }

        for (User user : users) {
            LocalDateTime createdAt = user.getCreatedAt();
            if (createdAt == null) {
                continue;
            }
            LocalDate month = createdAt.toLocalDate().withDayOfMonth(1);
            if (month.isBefore(startMonth)) {
                continue;
            }
            String key = month.format(MONTH_FORMAT);
            totals.merge(key, 1L, Long::sum);
        }

        return totals.entrySet().stream()
                .map(entry -> new UserGrowthPoint(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private List<WeeklyEngagementPoint> computeWeeklyEngagementFromInteractions() {
        List<Interaction> interactions = interactionRepository.findAll();
        if (interactions.isEmpty()) {
            return Collections.emptyList();
        }

        LocalDate currentWeekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate firstWeekStart = currentWeekStart.minusWeeks(9);

        Map<LocalDate, WeeklyAccumulator> buckets = new TreeMap<>();
        for (int i = 0; i < 10; i++) {
            LocalDate weekStart = firstWeekStart.plusWeeks(i);
            buckets.put(weekStart, new WeeklyAccumulator(weekStart));
        }

        for (Interaction interaction : interactions) {
            LocalDateTime createdAt = interaction.getCreatedAt();
            if (createdAt == null) {
                continue;
            }
            LocalDate day = createdAt.toLocalDate();
            if (day.isBefore(firstWeekStart)) {
                continue;
            }
            LocalDate weekStart = day.with(DayOfWeek.MONDAY);
            WeeklyAccumulator accumulator = buckets.computeIfAbsent(weekStart, WeeklyAccumulator::new);
            accumulator.register(interaction.getType());
        }

        return buckets.entrySet().stream()
                .map(entry -> entry.getValue().toPoint())
                .collect(Collectors.toList());
    }

    private static long valueOrZero(Number number) {
        return number == null ? 0 : number.longValue();
    }

    private static String toString(LocalDate date) {
        return date == null ? null : date.format(ISO_DATE);
    }

    private static String weekLabel(Long weekId, LocalDate start) {
        if (weekId != null) {
            return "Semana " + weekId;
        }
        if (start != null) {
            int weekOfYear = start.get(WEEK_FIELDS.weekOfWeekBasedYear());
            return "Semana " + weekOfYear;
        }
        return null;
    }

    private static final class WeeklyAccumulator {
        private final LocalDate start;
        private final LocalDate end;
        private long likes;
        private long comments;
        private long shares;

        WeeklyAccumulator(LocalDate start) {
            this.start = Objects.requireNonNull(start);
            this.end = start.plusDays(6);
        }

        void register(String type) {
            if (type == null) {
                return;
            }
            switch (type.toUpperCase(Locale.ROOT)) {
                case "LIKE" -> likes++;
                case "COMMENT" -> comments++;
                case "SHARE" -> shares++;
                default -> { /* ignore unsupported interaction types */ }
            }
        }

        WeeklyEngagementPoint toPoint() {
            Long weekId = (long) start.get(WEEK_FIELDS.weekOfWeekBasedYear());
            String label = weekLabel(weekId, start);
            return new WeeklyEngagementPoint(
                    weekId,
                    label,
                    start.format(ISO_DATE),
                    end.format(ISO_DATE),
                    likes,
                    comments,
                    shares
            );
        }
    }
}
