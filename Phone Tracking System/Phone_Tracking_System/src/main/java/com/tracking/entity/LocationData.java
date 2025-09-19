package com.tracking.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "location_data")
public class LocationData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double latitude;
	private Double longitude;
	private Float accuracy;
	private Float altitude;
	private Float speed;
	private Integer batteryLevel;
	private Boolean isCharging = false;
	private String networkType;
	private String address;
	private Boolean isLastKnown = false;

	private LocalDateTime timestamp = LocalDateTime.now();

	@ManyToOne
	@JoinColumn(name = "device_id")
	private Device device;

	// Constructors
	public LocationData() {
	}

	public LocationData(Double latitude, Double longitude, Device device) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.device = device;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Float accuracy) {
		this.accuracy = accuracy;
	}

	public Integer getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(Integer batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	public Boolean getIsCharging() {
		return isCharging;
	}

	public void setIsCharging(Boolean isCharging) {
		this.isCharging = isCharging;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getIsLastKnown() {
		return isLastKnown;
	}

	public void setIsLastKnown(Boolean isLastKnown) {
		this.isLastKnown = isLastKnown;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}
}