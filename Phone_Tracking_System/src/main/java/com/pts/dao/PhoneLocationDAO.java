package com.pts.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.pts.model.PhoneLocation;
import com.pts.model.User;
import com.pts.util.HibernateUtil;

public class PhoneLocationDAO {

	public void saveLocation(PhoneLocation location) {
		Transaction transaction = null;
		System.out.println("[SAVE LOCATION PhoneLocationDAO] Start saving phone location...");
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			System.out.println("[SAVE LOCATION PhoneLocationDAO] Hibernate session opened.");
			transaction = session.beginTransaction();
			System.out.println("[SAVE LOCATION PhoneLocationDAO] Transaction started.");
			session.save(location);
			System.out.println(
					"[SAVE LOCATION] Location saved successfully for phone number: " + location.getPhoneNumber());
			transaction.commit();
			System.out.println("[SAVE LOCATION PhoneLocationDAO] Transaction committed successfully.");
		} catch (Exception e) {
			System.out.println("[SAVE LOCATION PhoneLocationDAO] Error: " + e.getMessage());
			if (transaction != null) {
				transaction.rollback();
				System.out.println("[SAVE LOCATION PhoneLocationDAO] Transaction rolled back.");
			}
			throw e;
		}
	}

	public PhoneLocation findById(Long id) {
		System.out.println("[FIND BY ID PhoneLocationDAO] Searching location with ID: " + id);
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			PhoneLocation location = session.get(PhoneLocation.class, id);
			System.out.println("[FIND BY ID PhoneLocationDAO] Result: " + (location != null ? "Location found" : "No record found"));
			return location;
		} catch (Exception e) {
			System.out.println("[FIND BY ID PhoneLocationDAO] Error: " + e.getMessage());
			throw e;
		}
	}

	public List<PhoneLocation> findByPhoneNumber(String phoneNumber) {
		System.out.println("[FIND BY PHONE PhoneLocationDAO] Fetching locations for phone number: " + phoneNumber);
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			System.out.println("[FIND BY PHONE PhoneLocationDAO] Hibernate session opened.");
			Query<PhoneLocation> query = session.createQuery(
					"FROM PhoneLocation WHERE phoneNumber = :phoneNumber ORDER BY trackedAt DESC", PhoneLocation.class);
			query.setParameter("phoneNumber", phoneNumber);
			List<PhoneLocation> list = query.list();
			System.out.println("[FIND BY PHONE PhoneLocationDAO] Total locations found: " + list.size());
			return list;
		} catch (Exception e) {
			System.out.println("[FIND BY PHONE PhoneLocationDAO] Error: " + e.getMessage());
			throw e;
		}
	}

	public List<PhoneLocation> findByEmailId(String emailId) {
		System.out.println("[FIND BY EMAIL PhoneLocationDAO] Fetching locations for email ID: " + emailId);
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			System.out.println("[FIND BY EMAIL PhoneLocationDAO] Hibernate session opened.");
			Query<PhoneLocation> query = session.createQuery(
					"FROM PhoneLocation WHERE emailId = :emailId ORDER BY trackedAt DESC", PhoneLocation.class);
			query.setParameter("emailId", emailId);
			List<PhoneLocation> list = query.list();
			System.out.println("[FIND BY EMAIL PhoneLocationDAO] Total locations found: " + list.size());
			return list;
		} catch (Exception e) {
			System.out.println("[FIND BY EMAIL PhoneLocationDAO] Error: " + e.getMessage());
			throw e;
		}
	}

	public List<PhoneLocation> findByUser(User user) {
		System.out.println(
				"[FIND BY USER] Fetching locations tracked by user: " + (user != null ? user.getUsername() : "null"));
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			System.out.println("[FIND BY USER PhoneLocationDAO] Hibernate session opened.");
			Query<PhoneLocation> query = session.createQuery(
					"FROM PhoneLocation WHERE trackedByUser = :user ORDER BY trackedAt DESC", PhoneLocation.class);
			query.setParameter("user", user);
			List<PhoneLocation> list = query.list();
			System.out.println("[FIND BY USER PhoneLocationDAO] Total locations found: " + list.size());
			return list;
		} catch (Exception e) {
			System.out.println("[FIND BY USER PhoneLocationDAO] Error: " + e.getMessage());
			throw e;
		}
	}

	public PhoneLocation findLatestByPhoneNumber(String phoneNumber) {
		System.out.println("[FIND LATEST PhoneLocationDAO] Fetching latest location for phone number: " + phoneNumber);
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			System.out.println("[FIND LATEST PhoneLocationDAO] Hibernate session opened.");
			Query<PhoneLocation> query = session.createQuery(
					"FROM PhoneLocation WHERE phoneNumber = :phoneNumber ORDER BY trackedAt DESC", PhoneLocation.class);
			query.setParameter("phoneNumber", phoneNumber);
			query.setMaxResults(1);
			PhoneLocation location = query.uniqueResult();
			System.out.println(
					"[FIND LATEST PhoneLocationDAO] Result: " + (location != null ? "Location found" : "No recent record found"));
			return location;
		} catch (Exception e) {
			System.out.println("[FIND LATEST PhoneLocationDAO] Error: " + e.getMessage());
			throw e;
		}
	}
}
