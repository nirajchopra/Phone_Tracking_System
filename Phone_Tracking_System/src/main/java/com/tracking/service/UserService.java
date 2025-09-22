package com.tracking.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tracking.entity.User;
import com.tracking.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User registerUser(User user) {
		if (userRepository.existsByUsername(user.getUsername())) {
			throw new RuntimeException("Username already exists");
		}
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new RuntimeException("Email already exists");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}
}
