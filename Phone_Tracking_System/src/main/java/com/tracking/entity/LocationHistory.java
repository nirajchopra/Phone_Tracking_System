package com.tracking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "location_history")
public class LocationHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "phone_id", nullable = false)
	private Phone phone;

	@NotNull(message = "Latitude is required")
	private Double latitude;

	@NotNull(message = "Longitude is required")
	private Double longitude;

	private String address;
	private Double accuracy;
	private String formattedAddress;
	private String placeId;

	@Enumerated(EnumType.STRING)
	private LocationEventType eventType;

	private LocalDateTime timestamp = LocalDateTime.now();

	// Constructors
	public LocationHistory() {
	}

	public LocationHistory(Phone phone, Double latitude, Double longitude, String address,
			LocationEventType eventType) {
		this.phone = phone;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.eventType = eventType;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Phone getPhone() {
		return phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}

	public String getFormattedAddress() {
		return formattedAddress;
	}

	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public LocationEventType getEventType() {
		return eventType;
	}

	public void setEventType(LocationEventType eventType) {
		this.eventType = eventType;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}