package com.pts.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.pts.model.PhoneLocation;
import com.pts.model.User;
import com.pts.util.HibernateUtil;

public class PhoneLocationDAO {

	/**
	 * Save location to database using Hibernate
	 * Returns boolean to indicate success/failure
	 */
	public boolean saveLocation(PhoneLocation location) {
		Transaction transaction = null;
		System.out.println("========================================");
		System.out.println("[SAVE LOCATION] Starting save operation...");
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			System.out.println(" Hibernate session opened successfully");
			
			// Validate location object
			if (location == null) {
				System.err.println(" Location object is null!");
				return false;
			}
			
			// CRITICAL: Validate that at least phone or email is present
			if ((location.getPhoneNumber() == null || location.getPhoneNumber().trim().isEmpty()) &&
			    (location.getEmailId() == null || location.getEmailId().trim().isEmpty())) {
				System.err.println("✗ Both phone number and email ID are null/empty!");
				return false;
			}
			
			// CRITICAL: Ensure trackedAt is not null
			if (location.getTrackedAt() == null) {
				System.out.println(" Warning: trackedAt was null, setting to current time");
				location.setTrackedAt(new Date());
			}
			
			// Validate coordinates
			if (location.getLatitude() == 0.0 && location.getLongitude() == 0.0) {
				System.err.println(" Warning: Coordinates are (0.0, 0.0)");
			}
			
			transaction = session.beginTransaction();
			System.out.println(" Transaction started");
			
			// Log location details
			System.out.println("→ Saving location:");
			System.out.println("  - Phone: " + location.getPhoneNumber());
			System.out.println("  - Email: " + location.getEmailId());
			System.out.println("  - City: " + location.getCity());
			System.out.println("  - State: " + location.getState());
			System.out.println("  - Country: " + location.getCountry());
			System.out.println("  - Coordinates: " + location.getLatitude() + ", " + location.getLongitude());
			System.out.println("  - Address: " + location.getAddress());
			System.out.println("  - Accuracy: " + location.getAccuracy());
			System.out.println("  - Tracked At: " + location.getTrackedAt());
			System.out.println("  - Tracked By: " + (location.getTrackedByUser() != null ? location.getTrackedByUser().getUsername() : "null"));
			
			session.save(location);
			transaction.commit();
			
			System.out.println(" Location saved successfully!");
			System.out.println("  - Generated ID: " + location.getId());
			System.out.println("========================================\n");
			
			return true;
			
		} catch (Exception e) {
			System.err.println(" Error saving location: " + e.getMessage());
			System.err.println(" Error type: " + e.getClass().getName());
			e.printStackTrace();
			
			if (transaction != null && transaction.isActive()) {
				try {
					transaction.rollback();
					System.out.println(" Transaction rolled back");
				} catch (Exception rollbackEx) {
					System.err.println(" Rollback failed: " + rollbackEx.getMessage());
				}
			}
			System.out.println("========================================\n");
			return false;
		}
	}

	/**
	 * Find location by ID
	 */
	public PhoneLocation findById(Long id) {
		System.out.println("[FIND BY ID] Searching for location with ID: " + id);
		
		if (id == null) {
			System.err.println(" ID is null!");
			return null;
		}
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			PhoneLocation location = session.get(PhoneLocation.class, id);
			
			if (location != null) {
				// Ensure trackedAt is not null
				if (location.getTrackedAt() == null) {
					location.setTrackedAt(new Date());
				}
				System.out.println(" Location found: " + location.getCity());
			} else {
				System.out.println(" No location found with ID: " + id);
			}
			
			return location;
			
		} catch (Exception e) {
			System.err.println(" Error finding location by ID: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Find all locations by phone number
	 */
	public List<PhoneLocation> findByPhoneNumber(String phoneNumber) {
		System.out.println("[FIND BY PHONE] Searching locations for: " + phoneNumber);
		
		if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
			System.err.println(" Phone number is null or empty!");
			return new ArrayList<>();
		}
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<PhoneLocation> query = session.createQuery(
				"FROM PhoneLocation WHERE phoneNumber = :phoneNumber ORDER BY trackedAt DESC", 
				PhoneLocation.class
			);
			query.setParameter("phoneNumber", phoneNumber);
			
			List<PhoneLocation> locations = query.list();
			
			// Ensure all locations have valid trackedAt
			for (PhoneLocation loc : locations) {
				if (loc.getTrackedAt() == null) {
					loc.setTrackedAt(new Date());
				}
			}
			
			System.out.println(" Found " + locations.size() + " location(s)");
			return locations;
			
		} catch (Exception e) {
			System.err.println(" Error finding locations by phone: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * Find all locations by email ID
	 */
	public List<PhoneLocation> findByEmailId(String emailId) {
		System.out.println("[FIND BY EMAIL] Searching locations for: " + emailId);
		
		if (emailId == null || emailId.trim().isEmpty()) {
			System.err.println(" Email ID is null or empty!");
			return new ArrayList<>();
		}
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<PhoneLocation> query = session.createQuery(
				"FROM PhoneLocation WHERE emailId = :emailId ORDER BY trackedAt DESC", 
				PhoneLocation.class
			);
			query.setParameter("emailId", emailId);
			
			List<PhoneLocation> locations = query.list();
			
			// Ensure all locations have valid trackedAt
			for (PhoneLocation loc : locations) {
				if (loc.getTrackedAt() == null) {
					loc.setTrackedAt(new Date());
				}
			}
			
			System.out.println(" Found " + locations.size() + " location(s)");
			return locations;
			
		} catch (Exception e) {
			System.err.println(" Error finding locations by email: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * Find all locations tracked by a user
	 */
	public List<PhoneLocation> findByUser(User user) {
		System.out.println("[FIND BY USER] Searching locations for user: " + 
			(user != null ? user.getUsername() : "null"));
		
		if (user == null) {
			System.err.println(" User is null!");
			return new ArrayList<>();
		}
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<PhoneLocation> query = session.createQuery(
				"FROM PhoneLocation WHERE trackedByUser = :user ORDER BY trackedAt DESC", 
				PhoneLocation.class
			);
			query.setParameter("user", user);
			
			List<PhoneLocation> locations = query.list();
			
			// CRITICAL: Ensure all locations have valid trackedAt
			for (PhoneLocation loc : locations) {
				if (loc.getTrackedAt() == null) {
					System.out.println(" Warning: Found location with null trackedAt, fixing...");
					loc.setTrackedAt(new Date());
				}
			}
			
			System.out.println(" Found " + locations.size() + " location(s) for user: " + user.getUsername());
			return locations;
			
		} catch (Exception e) {
			System.err.println(" Error finding locations by user: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * Find latest location by phone number
	 */
	public PhoneLocation findLatestByPhoneNumber(String phoneNumber) {
		System.out.println("[FIND LATEST] Searching latest location for: " + phoneNumber);
		
		if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
			System.err.println(" Phone number is null or empty!");
			return null;
		}
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<PhoneLocation> query = session.createQuery(
				"FROM PhoneLocation WHERE phoneNumber = :phoneNumber ORDER BY trackedAt DESC", 
				PhoneLocation.class
			);
			query.setParameter("phoneNumber", phoneNumber);
			query.setMaxResults(1);
			
			PhoneLocation location = query.uniqueResult();
			
			if (location != null) {
				// Ensure trackedAt is not null
				if (location.getTrackedAt() == null) {
					location.setTrackedAt(new Date());
				}
				System.out.println(" Latest location found: " + location.getCity());
			} else {
				System.out.println(" No location found for: " + phoneNumber);
			}
			
			return location;
			
		} catch (Exception e) {
			System.err.println(" Error finding latest location: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Find latest location by email ID
	 */
	public PhoneLocation findLatestByEmailId(String emailId) {
		System.out.println("[FIND LATEST EMAIL] Searching latest location for: " + emailId);
		
		if (emailId == null || emailId.trim().isEmpty()) {
			System.err.println(" Email ID is null or empty!");
			return null;
		}
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<PhoneLocation> query = session.createQuery(
				"FROM PhoneLocation WHERE emailId = :emailId ORDER BY trackedAt DESC", 
				PhoneLocation.class
			);
			query.setParameter("emailId", emailId);
			query.setMaxResults(1);
			
			PhoneLocation location = query.uniqueResult();
			
			if (location != null) {
				// Ensure trackedAt is not null
				if (location.getTrackedAt() == null) {
					location.setTrackedAt(new Date());
				}
				System.out.println(" Latest location found: " + location.getCity());
			} else {
				System.out.println(" No location found for: " + emailId);
			}
			
			return location;
			
		} catch (Exception e) {
			System.err.println(" Error finding latest location by email: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get all locations
	 */
	public List<PhoneLocation> findAll() {
		System.out.println("[FIND ALL] Fetching all locations...");
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<PhoneLocation> query = session.createQuery(
				"FROM PhoneLocation ORDER BY trackedAt DESC", 
				PhoneLocation.class
			);
			
			List<PhoneLocation> locations = query.list();
			
			// Ensure all locations have valid trackedAt
			for (PhoneLocation loc : locations) {
				if (loc.getTrackedAt() == null) {
					loc.setTrackedAt(new Date());
				}
			}
			
			System.out.println(" Found " + locations.size() + " total location(s)");
			return locations;
			
		} catch (Exception e) {
			System.err.println(" Error finding all locations: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * Delete location by ID
	 */
	public boolean deleteById(Long id) {
		Transaction transaction = null;
		System.out.println("[DELETE] Attempting to delete location with ID: " + id);
		
		if (id == null) {
			System.err.println(" ID is null!");
			return false;
		}
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			
			PhoneLocation location = session.get(PhoneLocation.class, id);
			
			if (location != null) {
				session.delete(location);
				transaction.commit();
				System.out.println(" Location deleted successfully");
				return true;
			} else {
				System.out.println(" No location found with ID: " + id);
				return false;
			}
			
		} catch (Exception e) {
			System.err.println(" Error deleting location: " + e.getMessage());
			e.printStackTrace();
			
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			return false;
		}
	}
}