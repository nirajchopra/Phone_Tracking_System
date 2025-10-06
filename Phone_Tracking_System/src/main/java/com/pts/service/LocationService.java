package com.pts.service;

import java.util.List;
import java.util.Random;

import com.pts.dao.PhoneLocationDAO;
import com.pts.dao.TrackingRequestDAO;
import com.pts.model.PhoneLocation;
import com.pts.model.SearchType;
import com.pts.model.TrackingRequest;
import com.pts.model.User;
import com.pts.util.ValidationUtil;

public class LocationService {
	private PhoneLocationDAO locationDAO;
	private TrackingRequestDAO trackingRequestDAO;

	public LocationService() {
		this.locationDAO = new PhoneLocationDAO();
		this.trackingRequestDAO = new TrackingRequestDAO();
	}

	public PhoneLocation trackByPhoneNumber(String phoneNumber, User user) {
		try {
			if (!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
				throw new IllegalArgumentException("Invalid phone number format");
			}

			// Log the tracking request
			TrackingRequest request = new TrackingRequest(user, phoneNumber, SearchType.PHONE_NUMBER);

			// Simulate location tracking (In real app, you would use actual APIs)
			PhoneLocation location = simulateLocationTracking(phoneNumber, null);
			location.setTrackedByUser(user);

			if (location != null) {
				locationDAO.saveLocation(location);
				request.setSuccessful(true);
				request.setResultMessage("Location found successfully");
			} else {
				request.setSuccessful(false);
				request.setResultMessage("Location not found");
			}

			trackingRequestDAO.saveRequest(request);
			return location;

		} catch (Exception e) {
			System.err.println("Error tracking phone number: " + e.getMessage());
			return null;
		}
	}

	public PhoneLocation trackByEmailId(String emailId, User user) {
		try {
			if (!ValidationUtil.isValidEmail(emailId)) {
				throw new IllegalArgumentException("Invalid email format");
			}

			// Log the tracking request
			TrackingRequest request = new TrackingRequest(user, emailId, SearchType.EMAIL_ID);

			// Simulate location tracking
			PhoneLocation location = simulateLocationTracking(null, emailId);
			location.setTrackedByUser(user);

			if (location != null) {
				locationDAO.saveLocation(location);
				request.setSuccessful(true);
				request.setResultMessage("Location found successfully");
			} else {
				request.setSuccessful(false);
				request.setResultMessage("Location not found");
			}

			trackingRequestDAO.saveRequest(request);
			return location;

		} catch (Exception e) {
			System.err.println("Error tracking email ID: " + e.getMessage());
			return null;
		}
	}

	private PhoneLocation simulateLocationTracking(String phoneNumber, String emailId) {
		// This is a simulation. In a real application, you would integrate with:
		// - Telecom APIs for phone number location
		// - Social media APIs for email-based location
		// - IP geolocation services
		// - Mobile network operator APIs

		Random random = new Random();
		String[] cities = { "Mumbai", "Delhi", "Bangalore", "Chennai", "Kolkata", "Hyderabad", "Pune", "Ahmedabad" };
		String[] countries = { "India", "USA", "UK", "Canada", "Australia" };

		double[] latitudes = { 19.0760, 28.7041, 12.9716, 13.0827, 22.5726, 17.3850, 18.5204, 23.0225 };
		double[] longitudes = { 72.8777, 77.1025, 77.5946, 80.2707, 88.3639, 78.4867, 73.8567, 72.5714 };

		int index = random.nextInt(cities.length);

		PhoneLocation location = new PhoneLocation();
		location.setPhoneNumber(phoneNumber);
		location.setEmailId(emailId);
		location.setLatitude(latitudes[index] + (random.nextDouble() - 0.5) * 0.01); // Add some randomness
		location.setLongitude(longitudes[index] + (random.nextDouble() - 0.5) * 0.01);
		location.setCity(cities[index]);
		location.setCountry("India");
		location.setAddress(generateRandomAddress(cities[index]));
		location.setAccuracy("High");

		return location;
	}

	private String generateRandomAddress(String city) {
		String[] areas = { "MG Road", "Park Street", "Civil Lines", "Sector 14", "Koramangala", "Banjara Hills" };
		Random random = new Random();
		return (random.nextInt(999) + 1) + ", " + areas[random.nextInt(areas.length)] + ", " + city;
	}

	public List<PhoneLocation> getLocationHistory(User user) {
		return locationDAO.findByUser(user);
	}

	public List<PhoneLocation> getLocationsByPhoneNumber(String phoneNumber) {
		return locationDAO.findByPhoneNumber(phoneNumber);
	}

	public List<PhoneLocation> getLocationsByEmailId(String emailId) {
		return locationDAO.findByEmailId(emailId);
	}
}