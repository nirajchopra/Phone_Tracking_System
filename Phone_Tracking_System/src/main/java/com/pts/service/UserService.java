package com.pts.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.pts.dao.UserDAO;
import com.pts.model.User;
import com.pts.util.ValidationUtil;

public class UserService {
	private UserDAO userDAO;
	private BCryptPasswordEncoder passwordEncoder;

	public UserService() {
		this.userDAO = new UserDAO();
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	public boolean registerUser(String username, String email, String password, String fullName, String phoneNumber) {
		try {
			// Validation
			if (!ValidationUtil.isValidEmail(email)) {
				throw new IllegalArgumentException("Invalid email format");
			}

			if (!ValidationUtil.isValidPassword(password)) {
				throw new IllegalArgumentException(
						"Password must be at least 8 characters long and contain uppercase, lowercase, number and special character");
			}

			if (!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
				throw new IllegalArgumentException("Invalid phone number format");
			}

			// Check if user already exists
			if (userDAO.existsByUsername(username)) {
				throw new IllegalArgumentException("Username already exists");
			}

			if (userDAO.existsByEmail(email)) {
				throw new IllegalArgumentException("Email already registered");
			}

			// Create new user
			User user = new User();
			user.setUsername(username);
			user.setEmail(email);
			user.setPassword(passwordEncoder.encode(password));
			user.setFullName(fullName);
			user.setPhoneNumber(phoneNumber);

			userDAO.saveUser(user);
			return true;
		} catch (Exception e) {
			System.err.println("Error registering user: " + e.getMessage());
			return false;
		}
	}

	public User authenticateUser(String usernameOrEmail, String password) {
		User user = null;

		// Try to find by username first, then by email
		if (ValidationUtil.isValidEmail(usernameOrEmail)) {
			user = userDAO.findByEmail(usernameOrEmail);
		} else {
			user = userDAO.findByUsername(usernameOrEmail);
		}

		if (user != null && user.isActive() && passwordEncoder.matches(password, user.getPassword())) {
			// Update last login time
			user.setLastLogin(LocalDateTime.now());
			userDAO.updateUser(user);
			return user;
		}

		return null;
	}

	public boolean initiatePasswordReset(String email) {
		try {
			User user = userDAO.findByEmail(email);
			if (user != null && user.isActive()) {
				String resetToken = UUID.randomUUID().toString();
				user.setResetToken(resetToken);
				user.setResetTokenExpiry(LocalDateTime.now().plusHours(1)); // 1 hour expiry

				userDAO.updateUser(user);

				// In a real application, you would send an email here
				System.out.println("Password reset token for " + email + ": " + resetToken);
				return true;
			}
			return false;
		} catch (Exception e) {
			System.err.println("Error initiating password reset: " + e.getMessage());
			return false;
		}
	}

	public boolean resetPassword(String token, String newPassword) {
		try {
			if (!ValidationUtil.isValidPassword(newPassword)) {
				throw new IllegalArgumentException("Invalid password format");
			}

			User user = userDAO.findByResetToken(token);
			if (user != null) {
				user.setPassword(passwordEncoder.encode(newPassword));
				user.setResetToken(null);
				user.setResetTokenExpiry(null);

				userDAO.updateUser(user);
				return true;
			}
			return false;
		} catch (Exception e) {
			System.err.println("Error resetting password: " + e.getMessage());
			return false;
		}
	}

	public User getUserById(Long id) {
		return userDAO.findById(id);
	}

	public User getUserByUsername(String username) {
		return userDAO.findByUsername(username);
	}

	public User getUserByEmail(String email) {
		return userDAO.findByEmail(email);
	}
}
