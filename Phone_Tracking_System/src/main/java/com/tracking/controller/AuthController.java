package com.tracking.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tracking.entity.User;
import com.tracking.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
		try {
			User registeredUser = userService.registerUser(user);
			return ResponseEntity
					.ok(Map.of("message", "User registered successfully", "userId", registeredUser.getId()));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser(Authentication authentication) {
		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			return userService.findByUsername(username)
					.map(user -> ResponseEntity.ok(Map.of("id", user.getId(), "username", user.getUsername(), "email",
							user.getEmail(), "fullName", user.getFullName(), "role", user.getRole())))
					.orElse(ResponseEntity.notFound().build());
		}
		return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
	}

	@GetMapping("/status")
	public ResponseEntity<?> getAuthStatus(Authentication authentication) {
		boolean authenticated = authentication != null && authentication.isAuthenticated()
				&& !"anonymousUser".equals(authentication.getName());
		return ResponseEntity
				.ok(Map.of("authenticated", authenticated, "user", authenticated ? authentication.getName() : null));
	}
}