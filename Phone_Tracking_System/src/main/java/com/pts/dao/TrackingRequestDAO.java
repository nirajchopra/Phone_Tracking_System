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
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.save(request);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		}
	}

	public List<TrackingRequest> findByUser(User user) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<TrackingRequest> query = session.createQuery(
					"FROM TrackingRequest WHERE user = :user ORDER BY requestTime DESC", TrackingRequest.class);
			query.setParameter("user", user);
			return query.list();
		}
	}

	public List<TrackingRequest> findAllRequests() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM TrackingRequest ORDER BY requestTime DESC", TrackingRequest.class).list();
		}
	}
}
