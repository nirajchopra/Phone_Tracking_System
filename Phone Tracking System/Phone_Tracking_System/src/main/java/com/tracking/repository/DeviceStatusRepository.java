package com.tracking.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tracking.entity.DeviceStatus;

@Repository
public interface DeviceStatusRepository extends JpaRepository<DeviceStatus, Long> {
	Optional<DeviceStatus> findTopByDeviceIdOrderByCreatedAtDesc(Long deviceId);

	List<DeviceStatus> findByDeviceIdAndCreatedAtAfter(Long deviceId, LocalDateTime after);

	List<DeviceStatus> findByStatus(DeviceStatus.Status status);

	List<DeviceStatus> findByLastHeartbeatBefore(LocalDateTime dateTime);
}
