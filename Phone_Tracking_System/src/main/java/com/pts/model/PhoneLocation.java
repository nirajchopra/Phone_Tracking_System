package com.pts.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "phone_locations")
public class PhoneLocation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@Column(name = "email_id")
	private String emailId;

	@Column(nullable = false)
	private double latitude;

	@Column(nullable = false)
	private double longitude;

	@Column(name = "address")
	private String address;

	@Column(name = "city")
	private String city;

	@Column(name = "country")
	private String country;

	@Column(name = "accuracy")
	private String accuracy;

	@Column(name = "tracked_at")
	private LocalDateTime trackedAt = LocalDateTime.now();

	@ManyToOne
	@JoinColumn(name = "tracked_by_user_id")
	private User trackedByUser;

	// Constructors
	public PhoneLocation() {
	}

	public PhoneLocation(String phoneNumber, double latitude, double longitude) {
		this.phoneNumber = phoneNumber;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}

	public LocalDateTime getTrackedAt() {
		return trackedAt;
	}

	public void setTrackedAt(LocalDateTime trackedAt) {
		this.trackedAt = trackedAt;
	}

	public User getTrackedByUser() {
		return trackedByUser;
	}

	public void setTrackedByUser(User trackedByUser) {
		this.trackedByUser = trackedByUser;
	}

	public String getFormattedTrackedAt() {
		if (trackedAt != null) {
			return trackedAt.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
		}
		return "";
	}
}
