package com.pts.util;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordUtil {
    
    private static final String SALT = "PhoneTrackingSystem2025"; // Static salt
    
    /**
     * Hash password using SHA-256
     * For development/testing - NOT recommended for production
     */
    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        // Combine password with salt and hash
        String saltedPassword = password + SALT;
        return DigestUtils.sha256Hex(saltedPassword);
    }
    
    /**
     * Verify password matches the hash
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) {
            return false;
        }
        String hashOfInput = hashPassword(password);
        return hashOfInput.equals(hashedPassword);
    }
    
    /**
     * For development - store plain text password
     * WARNING: Only for testing! Never use in production!
     */
    public static String encodeSimple(String password) {
        // For testing, return as-is
        // In real app, always hash passwords
        return password;
    }
    
    /**
     * For development - verify plain text password
     */
    public static boolean matchesSimple(String rawPassword, String encodedPassword) {
        // For testing, direct comparison
        return rawPassword != null && rawPassword.equals(encodedPassword);
    }
}