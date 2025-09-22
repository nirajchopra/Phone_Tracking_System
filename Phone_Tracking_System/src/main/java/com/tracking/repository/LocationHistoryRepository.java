package com.tracking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tracking.entity.LocationHistory;
import com.tracking.entity.Phone;

@Repository
public interface LocationHistoryRepository extends JpaRepository<LocationHistory, Long> {
	List<LocationHistory> findByPhoneOrderByTimestampDesc(Phone phone);

	@Query("SELECT lh FROM LocationHistory lh WHERE lh.phone = :phone AND lh.timestamp BETWEEN :startDate AND :endDate ORDER BY lh.timestamp DESC")
	List<LocationHistory> findByPhoneAndTimestampBetween(@Param("phone") Phone phone,
			@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
