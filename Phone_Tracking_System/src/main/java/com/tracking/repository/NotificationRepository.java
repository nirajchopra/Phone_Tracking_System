package com.tracking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tracking.entity.Notification;
import com.tracking.entity.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByUserOrderByCreatedAtDesc(User user);

	List<Notification> findByUserAndIsReadOrderByCreatedAtDesc(User user, boolean isRead);

	long countByUserAndIsRead(User user, boolean isRead);
}
