package com.pts.util;

import java.util.regex.Pattern;

import org.apache.commons.validator.routines.EmailValidator;

public class ValidationUtil {

	private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

	private static final String PHONE_PATTERN = "^(\\+\\d{1,3}[- ]?)?\\d{10}$";

	private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
	private static final Pattern phonePattern = Pattern.compile(PHONE_PATTERN);

	public static boolean isValidEmail(String email) {
		return EmailValidator.getInstance().isValid(email);
	}

	public static boolean isValidPassword(String password) {
		return password != null && passwordPattern.matcher(password).matches();
	}

	public static boolean isValidPhoneNumber(String phoneNumber) {
		return phoneNumber != null && phonePattern.matcher(phoneNumber).matches();
	}

	public static boolean isNotEmpty(String str) {
		return str != null && !str.trim().isEmpty();
	}

	public static boolean isValidUsername(String username) {
		return username != null && username.length() >= 3 && username.length() <= 20
				&& username.matches("^[a-zA-Z0-9_]+$");
	}
}
