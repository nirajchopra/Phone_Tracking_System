package com.tracking.service;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.tracking.entity.Notification;
import com.tracking.entity.NotificationType;
import com.tracking.entity.Phone;
import com.tracking.entity.User;
import com.tracking.repository.NotificationRepository;

@Service
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final SimpMessagingTemplate messagingTemplate;

	public NotificationService(NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate) {
		this.notificationRepository = notificationRepository;
		this.messagingTemplate = messagingTemplate;
	}

	public void createNotification(User user, Phone phone, String title, String message, NotificationType notificationType) {
		Notification notification = new Notification();
		notificationRepository.save(notification);

		// Send real-time notification via WebSocket
		messagingTemplate.convertAndSendToUser(user.getUsername(), "/queue/notifications", notification);
	}

	public List<Notification> getUserNotifications(User user) {
		return notificationRepository.findByUserOrderByCreatedAtDesc(user);
	}

	public List<Notification> getUnreadNotifications(User user) {
		return notificationRepository.findByUserAndIsReadOrderByCreatedAtDesc(user, false);
	}

	public void markAsRead(Long notificationId) {
		notificationRepository.findById(notificationId).ifPresent(notification -> {
			notification.setRead(true);
			notificationRepository.save(notification);
		});
	}

	public long getUnreadCount(User user) {
		return notificationRepository.countByUserAndIsRead(user, false);
	}
}
