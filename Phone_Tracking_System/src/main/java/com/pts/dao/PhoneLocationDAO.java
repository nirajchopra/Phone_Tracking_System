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
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.save(location);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		}
	}

	public PhoneLocation findById(Long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.get(PhoneLocation.class, id);
		}
	}

	public List<PhoneLocation> findByPhoneNumber(String phoneNumber) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<PhoneLocation> query = session.createQuery(
					"FROM PhoneLocation WHERE phoneNumber = :phoneNumber ORDER BY trackedAt DESC", PhoneLocation.class);
			query.setParameter("phoneNumber", phoneNumber);
			return query.list();
		}
	}

	public List<PhoneLocation> findByEmailId(String emailId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<PhoneLocation> query = session.createQuery(
					"FROM PhoneLocation WHERE emailId = :emailId ORDER BY trackedAt DESC", PhoneLocation.class);
			query.setParameter("emailId", emailId);
			return query.list();
		}
	}

	public List<PhoneLocation> findByUser(User user) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<PhoneLocation> query = session.createQuery(
					"FROM PhoneLocation WHERE trackedByUser = :user ORDER BY trackedAt DESC", PhoneLocation.class);
			query.setParameter("user", user);
			return query.list();
		}
	}

	public PhoneLocation findLatestByPhoneNumber(String phoneNumber) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<PhoneLocation> query = session.createQuery(
					"FROM PhoneLocation WHERE phoneNumber = :phoneNumber ORDER BY trackedAt DESC", PhoneLocation.class);
			query.setParameter("phoneNumber", phoneNumber);
			query.setMaxResults(1);
			return query.uniqueResult();
		}
	}
}
