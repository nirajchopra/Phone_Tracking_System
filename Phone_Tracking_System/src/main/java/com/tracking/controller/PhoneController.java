package com.tracking.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tracking.entity.LocationHistory;
import com.tracking.entity.Phone;
import com.tracking.entity.User;
import com.tracking.service.PhoneService;
import com.tracking.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/phones")
public class PhoneController {

	private final PhoneService phoneService;
	private final UserService userService;

	public PhoneController(PhoneService phoneService, UserService userService) {
		this.phoneService = phoneService;
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<?> registerPhone(@Valid @RequestBody Phone phone, Authentication authentication) {
		Optional<User> userOpt = userService.findByUsername(authentication.getName());
		if (userOpt.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
		}

		phone.setOwner(userOpt.get());
		Phone registeredPhone = phoneService.registerPhone(phone);
		return ResponseEntity.ok(registeredPhone);
	}

	@GetMapping
	public ResponseEntity<?> getUserPhones(Authentication authentication) {
		Optional<User> userOpt = userService.findByUsername(authentication.getName());
		if (userOpt.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
		}

		List<Phone> phones = phoneService.getPhonesByOwner(userOpt.get());
		return ResponseEntity.ok(phones);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getPhone(@PathVariable Long id, Authentication authentication) {
		Optional<Phone> phoneOpt = phoneService.getPhoneById(id);
		if (phoneOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Phone phone = phoneOpt.get();
		if (!phone.getOwner().getUsername().equals(authentication.getName())) {
			return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
		}

		return ResponseEntity.ok(phone);
	}

	@PostMapping("/{deviceId}/location")
	public ResponseEntity<?> updateLocation(@PathVariable String deviceId,
			@RequestBody Map<String, Object> locationData) {

		Double latitude = Double.valueOf(locationData.get("latitude").toString());
		Double longitude = Double.valueOf(locationData.get("longitude").toString());
		String address = locationData.get("address").toString();
		Phone status = Phone.valueOf(locationData.get("status").toString());

		phoneService.updatePhoneStatus(deviceId, status, latitude, longitude, address);
		return ResponseEntity.ok(Map.of("message", "Location updated successfully"));
	}

	@GetMapping("/{id}/history")
	public ResponseEntity<?> getLocationHistory(@PathVariable Long id, Authentication authentication) {
		Optional<Phone> phoneOpt = phoneService.getPhoneById(id);
		if (phoneOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Phone phone = phoneOpt.get();
		if (!phone.getOwner().getUsername().equals(authentication.getName())) {
			return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
		}

		List<LocationHistory> history = phoneService.getLocationHistory(id);
		return ResponseEntity.ok(history);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePhone(@PathVariable Long id, Authentication authentication) {
		Optional<Phone> phoneOpt = phoneService.getPhoneById(id);
		if (phoneOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Phone phone = phoneOpt.get();
		if (!phone.getOwner().getUsername().equals(authentication.getName())) {
			return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
		}

		phoneService.deletePhone(id);
		return ResponseEntity.ok(Map.of("message", "Phone deleted successfully"));
	}
}
