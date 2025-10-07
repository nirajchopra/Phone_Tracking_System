package com.pts.service;

import java.time.LocalDateTime;
import java.util.UUID;
import com.pts.dao.UserDAO;
import com.pts.model.User;
import com.pts.util.ValidationUtil;
import com.pts.util.PasswordUtil;

public class UserService {
    private UserDAO userDAO;
    
    // Set to true for development (plain text passwords)
    // Set to false for production (hashed passwords)
    private static final boolean USE_PLAIN_TEXT = true;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public boolean registerUser(String username, String email, String password, 
                                String fullName, String phoneNumber) {
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
            
            // Store password based on configuration
            if (USE_PLAIN_TEXT) {
                user.setPassword(password); // Plain text for development
            } else {
                user.setPassword(PasswordUtil.hashPassword(password)); // Hashed for production
            }
            
            user.setFullName(fullName);
            user.setPhoneNumber(phoneNumber);

            userDAO.saveUser(user);
            System.out.println("User registered successfully: " + username);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error registering user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public User authenticateUser(String usernameOrEmail, String password) {
        try {
            User user = null;

            // Try to find by username first, then by email
            if (ValidationUtil.isValidEmail(usernameOrEmail)) {
                System.out.println("Authenticating by email: " + usernameOrEmail);
                user = userDAO.findByEmail(usernameOrEmail);
            } else {
                System.out.println("Authenticating by username: " + usernameOrEmail);
                user = userDAO.findByUsername(usernameOrEmail);
            }

            if (user == null) {
                System.out.println("User not found");
                return null;
            }

            if (!user.isActive()) {
                System.out.println("User account is inactive");
                return null;
            }

            // Verify password based on configuration
            boolean passwordMatches;
            if (USE_PLAIN_TEXT) {
                passwordMatches = password.equals(user.getPassword());
                System.out.println("Plain text password match: " + passwordMatches);
            } else {
                passwordMatches = PasswordUtil.verifyPassword(password, user.getPassword());
                System.out.println("Hashed password match: " + passwordMatches);
            }

            if (passwordMatches) {
                // Update last login time
                user.setLastLogin(LocalDateTime.now());
                userDAO.updateUser(user);
                System.out.println("Authentication successful for: " + user.getUsername());
                return user;
            } else {
                System.out.println("Password does not match");
                return null;
            }
            
        } catch (Exception e) {
            System.err.println("Error authenticating user: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
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
                // Set password based on configuration
                if (USE_PLAIN_TEXT) {
                    user.setPassword(newPassword);
                } else {
                    user.setPassword(PasswordUtil.hashPassword(newPassword));
                }
                
                user.setResetToken(null);
                user.setResetTokenExpiry(null);

                userDAO.updateUser(user);
                System.out.println("Password reset successful for: " + user.getUsername());
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
    
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                return false;
            }
            
            // Verify old password
            boolean oldPasswordMatches;
            if (USE_PLAIN_TEXT) {
                oldPasswordMatches = oldPassword.equals(user.getPassword());
            } else {
                oldPasswordMatches = PasswordUtil.verifyPassword(oldPassword, user.getPassword());
            }
            
            if (!oldPasswordMatches) {
                System.out.println("Old password does not match");
                return false;
            }
            
            // Validate new password
            if (!ValidationUtil.isValidPassword(newPassword)) {
                throw new IllegalArgumentException("Invalid password format");
            }
            
            // Set new password
            if (USE_PLAIN_TEXT) {
                user.setPassword(newPassword);
            } else {
                user.setPassword(PasswordUtil.hashPassword(newPassword));
            }
            
            userDAO.updateUser(user);
            System.out.println("Password changed successfully for: " + user.getUsername());
            return true;
            
        } catch (Exception e) {
            System.err.println("Error changing password: " + e.getMessage());
            return false;
        }
    }
}