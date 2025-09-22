package com.tracking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "phone_id", nullable = false)
	private Phone phone;

	@NotBlank(message = "Title is required")
	private String title;

	@NotBlank(message = "Message is required")
	private String message;

	@Enumerated(EnumType.STRING)
	private NotificationType type;

	private boolean isRead = false;
	private LocalDateTime createdAt = LocalDateTime.now();

	// Constructors
	public Notification() {
	}

	public Notification(User user, Phone phone, String title, String message, NotificationType type) {
		this.user = user;
		this.phone = phone;
		this.title = title;
		this.message = message;
		this.type = type;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Phone getPhone() {
		return phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean read) {
		isRead = read;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}