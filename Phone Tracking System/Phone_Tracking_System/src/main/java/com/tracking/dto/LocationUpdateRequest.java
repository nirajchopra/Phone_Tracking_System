package com.tracking.dto;

public class LocationUpdateRequest {
	private String deviceId;
	private Double latitude;
	private Double longitude;
	private Float accuracy;
	private Integer batteryLevel;
	private Boolean isCharging;
	private String networkType;

	// Getters and setters
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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
}
