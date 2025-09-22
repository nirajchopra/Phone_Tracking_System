package com.tracking.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "current_locations")
public class CurrentLocation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "phone_id", nullable = false)
	private Phone phone;

	@NotNull(message = "Latitude is required")
	private Double latitude;

	@NotNull(message = "Longitude is required")
	private Double longitude;

	private String address;
	private Double accuracy;
	private String formattedAddress; // Google Maps formatted address
	private String placeId; // Google Places ID
	private LocalDateTime timestamp = LocalDateTime.now();
	private LocalDateTime lastUpdated = LocalDateTime.now();

	// Constructors
	public CurrentLocation() {
	}

	public CurrentLocation(Phone phone, Double latitude, Double longitude, String address) {
		this.phone = phone;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
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

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}