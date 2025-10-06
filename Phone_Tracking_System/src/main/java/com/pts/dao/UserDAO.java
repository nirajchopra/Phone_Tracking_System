package com.pts.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.pts.model.User;
import com.pts.util.HibernateUtil;

public class UserDAO {

	public void saveUser(User user) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.save(user);
			System.out.println("User Save Successfully");
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		}
	}

	public void updateUser(User user) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.update(user);
			System.out.println("User Update Successfully");
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		}
	}

	public User findById(Long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.get(User.class, id);
		}
	}

	public User findByUsername(String username) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
			query.setParameter("username", username);
			return query.uniqueResult();
		}
	}

	public User findByEmail(String email) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
			query.setParameter("email", email);
			return query.uniqueResult();
		}
	}

	public User findByResetToken(String token) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<User> query = session.createQuery("FROM User WHERE resetToken = :token AND resetTokenExpiry > :now",
					User.class);
			query.setParameter("token", token);
			query.setParameter("now", LocalDateTime.now());
			return query.uniqueResult();
		}
	}

	public List<User> findAllUsers() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM User", User.class).list();
		}
	}

	public boolean existsByUsername(String username) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username",
					Long.class);
			query.setParameter("username", username);
			return query.uniqueResult() > 0;
		}
	}

	public boolean existsByEmail(String email) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
			query.setParameter("email", email);
			return query.uniqueResult() > 0;
		}
	}
}