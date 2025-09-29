package com.vini334.dashboard.repository;

import com.vini334.dashboard.model.UserGrowthMonthly;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGrowthMonthlyRepository extends JpaRepository<UserGrowthMonthly, String> {
	java.util.List<UserGrowthMonthly> findTop6ByOrderByMonthYearDesc();
}
