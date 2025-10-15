package com.pts.service;

import java.util.Date;
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
		try {
			this.locationDAO = new PhoneLocationDAO();
			this.trackingRequestDAO = new TrackingRequestDAO();
			System.out.println("✓ LocationService initialized successfully");
		} catch (Exception e) {
			System.err.println("✗ Error initializing LocationService: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public PhoneLocation trackByPhoneNumber(String phoneNumber, User user) {
		System.out.println("→ trackByPhoneNumber called for: " + phoneNumber);

		try {
			// Validate phone number
			if (!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
				throw new IllegalArgumentException(
						"Invalid phone number format. Please enter a valid 10-digit phone number.");
			}

			// Create tracking request log
			TrackingRequest request = new TrackingRequest(user, phoneNumber, SearchType.PHONE_NUMBER);

			// Simulate location tracking
			PhoneLocation location = simulateLocationTracking(phoneNumber, null);

			if (location != null) {
				// CRITICAL: Set user and timestamp
				location.setTrackedByUser(user);
				location.setTrackedAt(new Date()); // Set current timestamp

				System.out.println("→ Saving location to database...");

				// Save location to database
				boolean saved = locationDAO.saveLocation(location);

				if (saved) {
					System.out.println("✓ Location saved successfully");
					request.setSuccessful(true);
					request.setResultMessage("Location found successfully");
				} else {
					System.err.println("✗ Failed to save location");
					request.setSuccessful(false);
					request.setResultMessage("Failed to save location");
				}
			} else {
				System.err.println("✗ Location tracking failed");
				request.setSuccessful(false);
				request.setResultMessage("Location not found");
			}

			// Save tracking request
			try {
				trackingRequestDAO.saveRequest(request);
			} catch (Exception e) {
				System.err.println("⚠ Warning: Could not save tracking request: " + e.getMessage());
			}

			return location;

		} catch (IllegalArgumentException e) {
			System.err.println("✗ Validation error: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			System.err.println("✗ Error tracking phone number: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public PhoneLocation trackByEmailId(String emailId, User user) {
		System.out.println("→ trackByEmailId called for: " + emailId);

		try {
			// Validate email
			if (!ValidationUtil.isValidEmail(emailId)) {
				throw new IllegalArgumentException("Invalid email format. Please enter a valid email address.");
			}

			// Create tracking request log
			TrackingRequest request = new TrackingRequest(user, emailId, SearchType.EMAIL_ID);

			// Simulate location tracking
			PhoneLocation location = simulateLocationTracking(null, emailId);

			if (location != null) {
				// CRITICAL: Set user and timestamp
				location.setTrackedByUser(user);
				location.setTrackedAt(new Date()); // Set current timestamp

				System.out.println("→ Saving location to database...");

				// Save location to database
				boolean saved = locationDAO.saveLocation(location);

				if (saved) {
					System.out.println("✓ Location saved successfully");
					request.setSuccessful(true);
					request.setResultMessage("Location found successfully");
				} else {
					System.err.println("✗ Failed to save location");
					request.setSuccessful(false);
					request.setResultMessage("Failed to save location");
				}
			} else {
				System.err.println("✗ Location tracking failed");
				request.setSuccessful(false);
				request.setResultMessage("Location not found");
			}

			// Save tracking request
			try {
				trackingRequestDAO.saveRequest(request);
			} catch (Exception e) {
				System.err.println("⚠ Warning: Could not save tracking request: " + e.getMessage());
			}

			return location;

		} catch (IllegalArgumentException e) {
			System.err.println("✗ Validation error: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			System.err.println("✗ Error tracking email ID: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Simulate location tracking with realistic data TODO: Replace with real API
	 * integration (Google Geolocation, Twilio, etc.)
	 */
	private PhoneLocation simulateLocationTracking(String phoneNumber, String emailId) {
		System.out.println("→ Simulating location tracking...");

		try {
			Random random = new Random();

			// Indian cities with accurate coordinates
			String[] cities = { "Mumbai", "Delhi", "Bangalore", "Chennai", "Kolkata", "Hyderabad", "Pune", "Ahmedabad",
					"Jaipur", "Lucknow", "Kanpur", "Nagpur" };

			String[] states = { "Maharashtra", "Delhi", "Karnataka", "Tamil Nadu", "West Bengal", "Telangana",
					"Maharashtra", "Gujarat", "Rajasthan", "Uttar Pradesh", "Uttar Pradesh", "Maharashtra" };

			// Accurate coordinates for each city
			double[] latitudes = { 19.0760, 28.7041, 12.9716, 13.0827, 22.5726, 17.3850, 18.5204, 23.0225, 26.9124,
					26.8467, 26.4499, 21.1458 };

			double[] longitudes = { 72.8777, 77.1025, 77.5946, 80.2707, 88.3639, 78.4867, 73.8567, 72.5714, 75.7873,
					80.9462, 80.3319, 79.0882 };

			// Random city selection
			int index = random.nextInt(cities.length);

			// Create location object
			PhoneLocation location = new PhoneLocation();

			// Set query info
			location.setPhoneNumber(phoneNumber);
			location.setEmailId(emailId);

			// Set location details
			location.setCity(cities[index]);
			location.setState(states[index]);
			location.setCountry("India");
			location.setAddress(generateRandomAddress(cities[index]));

			// Set coordinates with slight randomness for realism
			location.setLatitude(latitudes[index] + (random.nextDouble() - 0.5) * 0.01);
			location.setLongitude(longitudes[index] + (random.nextDouble() - 0.5) * 0.01);

			// Set accuracy
			location.setAccuracy("High");

			// CRITICAL: Set tracked time
			location.setTrackedAt(new Date());

			System.out.println("✓ Location simulated: " + cities[index] + ", " + states[index]);

			return location;

		} catch (Exception e) {
			System.err.println("✗ Error simulating location: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Generate realistic random address
	 */
	private String generateRandomAddress(String city) {
		String[] areas = { "MG Road", "Park Street", "Civil Lines", "Sector 14", "Koramangala", "Banjara Hills",
				"Connaught Place", "Brigade Road", "Jubilee Hills", "Indiranagar", "Whitefield", "Viman Nagar" };

		String[] landmarks = { "Near City Mall", "Opposite Metro Station", "Behind Bus Stand", "Near Railway Station",
				"Close to Airport", "Near Hospital" };

		Random random = new Random();
		int houseNo = random.nextInt(999) + 1;
		String area = areas[random.nextInt(areas.length)];
		String landmark = landmarks[random.nextInt(landmarks.length)];

		return houseNo + ", " + area + ", " + landmark + ", " + city;
	}

	/**
	 * Get location history for a user
	 */
	public List<PhoneLocation> getLocationHistory(User user) {
		try {
			System.out.println("→ Fetching location history for user: " + user.getUsername());
			List<PhoneLocation> history = locationDAO.findByUser(user);

			// Ensure all locations have valid trackedAt dates
			if (history != null) {
				for (PhoneLocation loc : history) {
					if (loc.getTrackedAt() == null) {
						loc.setTrackedAt(new Date());
					}
				}
			}

			System.out.println("✓ Found " + (history != null ? history.size() : 0) + " location records");
			return history;

		} catch (Exception e) {
			System.err.println("✗ Error fetching location history: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get locations by phone number
	 */
	public List<PhoneLocation> getLocationsByPhoneNumber(String phoneNumber) {
		try {
			return locationDAO.findByPhoneNumber(phoneNumber);
		} catch (Exception e) {
			System.err.println("✗ Error fetching locations by phone: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Get locations by email ID
	 */
	public List<PhoneLocation> getLocationsByEmailId(String emailId) {
		try {
			return locationDAO.findByEmailId(emailId);
		} catch (Exception e) {
			System.err.println("✗ Error fetching locations by email: " + e.getMessage());
			return null;
		}
	}
}