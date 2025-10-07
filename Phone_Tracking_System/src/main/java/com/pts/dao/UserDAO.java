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
		System.out.println("[UserDAO] Start saving user...");
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			System.out.println("[UserDAO] Hibernate session opened.");
			transaction = session.beginTransaction();
			System.out.println("[UserDAO] Transaction started.");
			session.save(user);
			System.out.println("[UserDAO] User saved: " + user.getUsername());
			transaction.commit();
			System.out.println("[UserDAO] Transaction committed successfully.");
		} catch (Exception e) {
			System.out.println("[UserDAO] Error: " + e.getMessage());
			if (transaction != null) {
				transaction.rollback();
				System.out.println("[UserDAO] Transaction rolled back.");
			}
			throw e;
		}
	}

	public void updateUser(User user) {
		Transaction transaction = null;
		System.out.println("[UPDATE UserDAO] Start updating user...");
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			System.out.println("[UPDATE UserDAO] Hibernate session opened.");
			transaction = session.beginTransaction();
			System.out.println("[UPDATE UserDAO] Transaction started.");
			session.update(user);
			System.out.println("[UPDATE UserDAO] User updated: " + user.getUsername());
			transaction.commit();
			System.out.println("[UPDATE UserDAO] Transaction committed successfully.");
		} catch (Exception e) {
			System.out.println("[UPDATE UserDAO] Error: " + e.getMessage());
			if (transaction != null) {
				transaction.rollback();
				System.out.println("[UPDATE UserDAO] Transaction rolled back.");
			}
			throw e;
		}
	}

	public User findById(Long id) {
		System.out.println("[FIND BY ID UserDAO] Searching for user with ID: " + id);
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			User user = session.get(User.class, id);
			System.out.println("[FIND BY ID UserDAO] Found user: " + (user != null ? user.getUsername() : "No user found"));
			return user;
		}
	}

	public User findByUsername(String username) {
		System.out.println("[FIND BY USERNAME UserDAO] Searching for username: " + username);
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
			query.setParameter("username", username);
			User user = query.uniqueResult();
			System.out.println("[FIND BY USERNAME UserDAO] Result: " + (user != null ? "User found" : "No user found"));
			return user;
		}
	}

	public User findByEmail(String email) {
		System.out.println("[FIND BY EMAIL UserDAO] Searching for email: " + email);
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
			query.setParameter("email", email);
			User user = query.uniqueResult();
			System.out.println("[FIND BY EMAIL UserDAO] Result: " + (user != null ? "User found" : "No user found"));
			return user;
		}
	}

	public User findByResetToken(String token) {
		System.out.println("[FIND BY TOKEN UserDAO] Searching for token: " + token);
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<User> query = session.createQuery("FROM User WHERE resetToken = :token AND resetTokenExpiry > :now",
					User.class);
			query.setParameter("token", token);
			query.setParameter("now", LocalDateTime.now());
			User user = query.uniqueResult();
			System.out.println(
					"[FIND BY TOKEN UserDAO] Result: " + (user != null ? "Valid token found" : "Invalid or expired token"));
			return user;
		}
	}

	public List<User> findAllUsers() {
		System.out.println("[FIND ALL USERS UserDAO] Fetching all users...");
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			List<User> users = session.createQuery("FROM User", User.class).list();
			System.out.println("[FIND ALL USERS UserDAO] Total users found: " + users.size());
			return users;
		}
	}

	public boolean existsByUsername(String username) {
		System.out.println("[CHECK USERNAME UserDAO] Checking if username exists: " + username);
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username",
					Long.class);
			query.setParameter("username", username);
			long count = query.uniqueResult();
			System.out.println("[CHECK USERNAME UserDAO] Count: " + count);
			return count > 0;
		}
	}

	public boolean existsByEmail(String email) {
		System.out.println("[CHECK EMAIL UserDAO] Checking if email exists: " + email);
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
			query.setParameter("email", email);
			long count = query.uniqueResult();
			System.out.println("[CHECK EMAIL UserDAO] Count: " + count);
			return count > 0;
		}
	}
}
