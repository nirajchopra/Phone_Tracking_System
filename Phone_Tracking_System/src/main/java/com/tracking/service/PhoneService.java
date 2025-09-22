package com.tracking.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.tracking.entity.CurrentLocation;
import com.tracking.entity.LocationEventType;
import com.tracking.entity.LocationHistory;
import com.tracking.entity.NotificationType;
import com.tracking.entity.Phone;
import com.tracking.entity.PhoneStatus;
import com.tracking.entity.User;
import com.tracking.repository.CurrentLocationRepository;
import com.tracking.repository.LocationHistoryRepository;
import com.tracking.repository.PhoneRepository;
import com.tracking.service.GoogleMapsService.GoogleMapsResponse;

@Service
public class PhoneService {

	private final PhoneRepository phoneRepository;
	private final CurrentLocationRepository currentLocationRepository;
	private final LocationHistoryRepository locationHistoryRepository;
	private final NotificationService notificationService;
	private final SimpMessagingTemplate messagingTemplate;
	private final GoogleMapsService googleMapsService;

	public PhoneService(PhoneRepository phoneRepository, CurrentLocationRepository currentLocationRepository,
			LocationHistoryRepository locationHistoryRepository, NotificationService notificationService,
			SimpMessagingTemplate messagingTemplate, GoogleMapsService googleMapsService) {
		this.phoneRepository = phoneRepository;
		this.currentLocationRepository = currentLocationRepository;
		this.locationHistoryRepository = locationHistoryRepository;
		this.notificationService = notificationService;
		this.messagingTemplate = messagingTemplate;
		this.googleMapsService = googleMapsService;
	}

	public Phone registerPhone(Phone phone) {
		return phoneRepository.save(phone);
	}

	public List<Phone> getPhonesByOwner(User owner) {
		return phoneRepository.findByOwner(owner);
	}

	public Optional<Phone> getPhoneById(Long id) {
		return phoneRepository.findById(id);
	}

	// CORRECTED METHOD: Proper parameter types
	public void updatePhoneStatus(String deviceId, Phone status, Double latitude, Double longitude,
			String address) {
		Optional<Phone> phoneOpt = phoneRepository.findByDeviceId(deviceId);
		if (phoneOpt.isPresent()) {
			Phone phone = phoneOpt.get();
			PhoneStatus oldStatus = phone.getStatus();
			phone.setStatus(status);
			phone.setLastSeen(LocalDateTime.now());
			phoneRepository.save(phone);

			// Update current location with Google Maps data
			updateCurrentLocationWithGoogleMaps(phone, latitude, longitude, address);

			// Add to history
			LocationEventType eventType = determineEventType(oldStatus, status);
			addLocationHistoryWithGoogleMaps(phone, latitude, longitude, address, eventType);

			// Send notifications
			handleStatusChangeNotification(phone, oldStatus, status, address);

			// Send real-time update via WebSocket
			sendRealtimeUpdate(phone);
		}
	}

	private void handleStatusChangeNotification(Phone phone, PhoneStatus oldStatus, Phone status, String address) {
		// TODO Auto-generated method stub
		
	}

	private LocationEventType determineEventType(PhoneStatus oldStatus, Phone status) {
		// TODO Auto-generated method stub
		return null;
	}

	private void updateCurrentLocationWithGoogleMaps(Phone phone, Double latitude, Double longitude, String address) {
		Optional<CurrentLocation> currentLocationOpt = currentLocationRepository.findByPhone(phone);
		CurrentLocation currentLocation;

		if (currentLocationOpt.isPresent()) {
			currentLocation = currentLocationOpt.get();
			currentLocation.setLatitude(latitude);
			currentLocation.setLongitude(longitude);
			currentLocation.setAddress(address);
			currentLocation.setLastUpdated(LocalDateTime.now());
		} else {
			currentLocation = new CurrentLocation(phone, latitude, longitude, address);
		}

		// Get Google Maps formatted address and place ID
		try {
			GoogleMapsResponse googleResponse = googleMapsService.getLocationDetails(latitude, longitude);
			if (googleResponse != null) {
				currentLocation.setFormattedAddress(googleResponse.getFormattedAddress());
				currentLocation.setPlaceId(googleResponse.getPlaceId());
			}
		} catch (Exception e) {
			// Log error but continue with basic address
			System.err.println("Error getting Google Maps data: " + e.getMessage());
		}

		currentLocationRepository.save(currentLocation);
	}

	private void addLocationHistoryWithGoogleMaps(Phone phone, Double latitude, Double longitude, String address,
			LocationEventType eventType) {
		LocationHistory history = new LocationHistory(phone, latitude, longitude, address, eventType);

		// Get Google Maps data for history record
		try {
			GoogleMapsResponse googleResponse = googleMapsService.getLocationDetails(latitude, longitude);
			if (googleResponse != null) {
				history.setFormattedAddress(googleResponse.getFormattedAddress());
				history.setPlaceId(googleResponse.getPlaceId());
			}
		} catch (Exception e) {
			System.err.println("Error getting Google Maps data for history: " + e.getMessage());
		}

		locationHistoryRepository.save(history);
	}

	private LocationEventType determineEventType(PhoneStatus oldStatus, PhoneStatus newStatus) {
		if (oldStatus == PhoneStatus.OFFLINE && newStatus == PhoneStatus.ONLINE) {
			return LocationEventType.DEVICE_ON;
		} else if (oldStatus == PhoneStatus.ONLINE && newStatus == PhoneStatus.OFFLINE) {
			return LocationEventType.DEVICE_OFF;
		}
		return LocationEventType.LOCATION_UPDATE;
	}

	private void handleStatusChangeNotification(Phone phone, PhoneStatus oldStatus, PhoneStatus newStatus,
			String address) {
		String title = "";
		String message = "";
		NotificationType notificationType = null;

		if (oldStatus == PhoneStatus.OFFLINE && newStatus == PhoneStatus.ONLINE) {
			title = "Device Online";
			message = phone.getDeviceName() + " is now online at " + address;
			notificationType = NotificationType.DEVICE_ONLINE;
		} else if (oldStatus == PhoneStatus.ONLINE && newStatus == PhoneStatus.OFFLINE) {
			title = "Device Offline";
			message = phone.getDeviceName() + " went offline. Last location: " + address;
			notificationType = NotificationType.DEVICE_OFFLINE;
		}

		if (notificationType != null) {
			notificationService.createNotification(phone.getOwner(), phone, title, message, notificationType);
		}
	}

	private void sendRealtimeUpdate(Phone phone) {
		messagingTemplate.convertAndSendToUser(phone.getOwner().getUsername(), "/queue/phone-updates", phone);
	}

	public List<LocationHistory> getLocationHistory(Long phoneId) {
		Optional<Phone> phoneOpt = phoneRepository.findById(phoneId);
		if (phoneOpt.isPresent()) {
			return locationHistoryRepository.findByPhoneOrderByTimestampDesc(phoneOpt.get());
		}
		return List.of();
	}

	public void deletePhone(Long phoneId) {
		phoneRepository.deleteById(phoneId);
	}
}