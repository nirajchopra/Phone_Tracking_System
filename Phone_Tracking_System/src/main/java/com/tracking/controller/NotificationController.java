package com.tracking.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tracking.entity.Notification;
import com.tracking.service.NotificationService;
import com.tracking.service.UserService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

	private final NotificationService notificationService;
	private final UserService userService;

	public NotificationController(NotificationService notificationService, UserService userService) {
		this.notificationService = notificationService;
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<?> getUserNotifications(Authentication authentication) {
		return userService.findByUsername(authentication.getName()).map(user -> {
			List<Notification> notifications = notificationService.getUserNotifications(user);
			return ResponseEntity.ok(notifications);
		}).orElse(ResponseEntity.badRequest().build());
	}

	@GetMapping("/unread")
	public ResponseEntity<?> getUnreadNotifications(Authentication authentication) {
		return userService.findByUsername(authentication.getName()).map(user -> {
			List<Notification> notifications = notificationService.getUnreadNotifications(user);
			long unreadCount = notificationService.getUnreadCount(user);
			return ResponseEntity.ok(Map.of("notifications", notifications, "unreadCount", unreadCount));
		}).orElse(ResponseEntity.badRequest().build());
	}

	@PutMapping("/{id}/read")
	public ResponseEntity<?> markAsRead(@PathVariable Long id) {
		notificationService.markAsRead(id);
		return ResponseEntity.ok(Map.of("message", "Notification marked as read"));
	}
}