package com.tracking.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tracking.dto.LocationUpdateRequest;
import com.tracking.dto.TrackingResponse;
import com.tracking.entity.Device;
import com.tracking.entity.LocationData;
import com.tracking.service.TrackingService;

@RestController
@RequestMapping("/api/tracking")
@CrossOrigin(origins = "http://localhost:3000")
public class TrackingController {

	@Autowired
	private TrackingService trackingService;

	@PostMapping("/update-location")
	public ResponseEntity<?> updateLocation(@RequestBody LocationUpdateRequest request) {
		try {
			LocationData locationData = trackingService.updateLocation(request);
			return ResponseEntity.ok(locationData);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error updating location: " + e.getMessage());
		}
	}

	@GetMapping("/device/{deviceId}/current-location")
	public ResponseEntity<?> getCurrentLocation(@PathVariable String deviceId) {
		try {
			LocationData currentLocation = trackingService.getCurrentLocation(deviceId);
			if (currentLocation != null) {
				return ResponseEntity.ok(currentLocation);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching location: " + e.getMessage());
		}
	}

	@GetMapping("/device/{deviceId}/location-history")
	public ResponseEntity<?> getLocationHistory(@PathVariable String deviceId,
			@RequestParam(defaultValue = "24") int hours) {
		try {
			List<LocationData> history = trackingService.getLocationHistory(deviceId, hours);
			return ResponseEntity.ok(history);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching location history: " + e.getMessage());
		}
	}

	@GetMapping("/user/{userId}/devices")
	public ResponseEntity<?> getUserDevices(@PathVariable Long userId) {
		try {
			List<Device> devices = trackingService.getUserDevices(userId);
			return ResponseEntity.ok(devices);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching devices: " + e.getMessage());
		}
	}

	@PostMapping("/device/{deviceId}/heartbeat")
	public ResponseEntity<?> deviceHeartbeat(@PathVariable String deviceId, @RequestBody Map<String, Object> data) {
		try {
			trackingService.updateDeviceHeartbeat(deviceId, data);
			return ResponseEntity.ok("Heartbeat updated");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error updating heartbeat: " + e.getMessage());
		}
	}

	@GetMapping("/dashboard/{userId}")
	public ResponseEntity<?> getDashboardData(@PathVariable Long userId) {
		try {
			TrackingResponse response = trackingService.getDashboardData(userId);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching dashboard data: " + e.getMessage());
		}
	}
}
