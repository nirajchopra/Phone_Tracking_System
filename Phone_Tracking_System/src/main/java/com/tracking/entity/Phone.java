package com.tracking.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "phones")
public class Phone {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	@NotBlank(message = "Phone number is required")
	private String phoneNumber;

	@Column(unique = true, nullable = false)
	@NotBlank(message = "Device ID is required")
	private String deviceId;

	@NotBlank(message = "Device name is required")
	private String deviceName;

	private String model;
	private String brand;

	@Enumerated(EnumType.STRING)
	private PhoneStatus status = PhoneStatus.OFFLINE;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id", nullable = false)
	private User owner;

	@OneToMany(mappedBy = "phone", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<LocationHistory> locationHistory;

	@OneToOne(mappedBy = "phone", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CurrentLocation currentLocation;

	private LocalDateTime registeredAt = LocalDateTime.now();
	private LocalDateTime lastSeen;

	// Constructors
	public Phone() {
	}

	public Phone(String phoneNumber, String deviceId, String deviceName, User owner) {
		this.phoneNumber = phoneNumber;
		this.deviceId = deviceId;
		this.deviceName = deviceName;
		this.owner = owner;
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

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public PhoneStatus getStatus() {
		return status;
	}

	public void setStatus(PhoneStatus status) {
		this.status = status;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<LocationHistory> getLocationHistory() {
		return locationHistory;
	}

	public void setLocationHistory(List<LocationHistory> locationHistory) {
		this.locationHistory = locationHistory;
	}

	public CurrentLocation getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(CurrentLocation currentLocation) {
		this.currentLocation = currentLocation;
	}

	public LocalDateTime getRegisteredAt() {
		return registeredAt;
	}

	public void setRegisteredAt(LocalDateTime registeredAt) {
		this.registeredAt = registeredAt;
	}

	public LocalDateTime getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(LocalDateTime lastSeen) {
		this.lastSeen = lastSeen;
	}

	public void setStatus(Phone status2) {
		// TODO Auto-generated method stub
		
	}

	public static Phone valueOf(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}