package com.pts.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.pts.model.TrackingRequest;
import com.pts.model.User;
import com.pts.util.HibernateUtil;

public class TrackingRequestDAO {

	public void saveRequest(TrackingRequest request) {
		Transaction transaction = null;
		System.out.println("[SAVE REQUEST TrackingRequestDAO] Start saving tracking request...");
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			System.out.println("[SAVE REQUEST TrackingRequestDAO] Hibernate session opened.");
			transaction = session.beginTransaction();
			System.out.println("[SAVE REQUEST TrackingRequestDAO] Transaction started.");
			session.save(request);
			System.out.println("[SAVE REQUEST TrackingRequestDAO] Request saved successfully for user: "
					+ (request.getUser() != null ? request.getUser().getUsername() : "Unknown"));
			transaction.commit();
			System.out.println("[SAVE REQUEST TrackingRequestDAO] Transaction committed successfully.");
		} catch (Exception e) {
			System.out.println("[SAVE REQUEST TrackingRequestDAO] Error occurred: " + e.getMessage());
			if (transaction != null) {
				transaction.rollback();
				System.out.println("[SAVE REQUEST TrackingRequestDAO] Transaction rolled back.");
			}
			throw e;
		}
	}

	public List<TrackingRequest> findByUser(User user) {
		System.out
				.println("[FIND BY USER TrackingRequestDAO] Fetching requests for user: " + (user != null ? user.getUsername() : "null"));
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			System.out.println("[FIND BY USER TrackingRequestDAO] Hibernate session opened.");
			Query<TrackingRequest> query = session.createQuery(
					"FROM TrackingRequest WHERE user = :user ORDER BY requestTime DESC", TrackingRequest.class);
			query.setParameter("user", user);
			List<TrackingRequest> list = query.list();
			System.out.println("[FIND BY USER TrackingRequestDAO] Total requests found: " + list.size());
			return list;
		} catch (Exception e) {
			System.out.println("[FIND BY USER TrackingRequestDAO] Error occurred: " + e.getMessage());
			throw e;
		}
	}

	public List<TrackingRequest> findAllRequests() {
		System.out.println("[FIND ALL REQUESTS TrackingRequestDAO] Fetching all tracking requests...");
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			System.out.println("[FIND ALL REQUESTS TrackingRequestDAO] Hibernate session opened.");
			List<TrackingRequest> list = session
					.createQuery("FROM TrackingRequest ORDER BY requestTime DESC", TrackingRequest.class).list();
			System.out.println("[FIND ALL REQUESTS TrackingRequestDAO] Total requests found: " + list.size());
			return list;
		} catch (Exception e) {
			System.out.println("[FIND ALL REQUESTS TrackingRequestDAO] Error occurred: " + e.getMessage());
			throw e;
		}
	}
}
