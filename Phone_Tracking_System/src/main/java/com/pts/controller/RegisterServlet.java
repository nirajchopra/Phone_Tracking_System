package com.pts.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pts.service.UserService;
import com.pts.util.ValidationUtil;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private UserService userService;

	@Override
	public void init() throws ServletException {
		userService = new UserService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/register.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		String fullName = request.getParameter("fullName");
		String phoneNumber = request.getParameter("phoneNumber");

		// Validation
		if (!ValidationUtil.isNotEmpty(username) || !ValidationUtil.isValidUsername(username)) {
			request.setAttribute("error",
					"Username must be 3-20 characters and contain only letters, numbers, and underscores");
			forwardWithData(request, response);
			return;
		}

		if (!ValidationUtil.isValidEmail(email)) {
			request.setAttribute("error", "Please enter a valid email address");
			forwardWithData(request, response);
			return;
		}

		if (!ValidationUtil.isValidPassword(password)) {
			request.setAttribute("error",
					"Password must be at least 8 characters with uppercase, lowercase, number and special character");
			forwardWithData(request, response);
			return;
		}

		if (!password.equals(confirmPassword)) {
			request.setAttribute("error", "Passwords do not match");
			forwardWithData(request, response);
			return;
		}

		if (!ValidationUtil.isNotEmpty(fullName)) {
			request.setAttribute("error", "Full name is required");
			forwardWithData(request, response);
			return;
		}

		if (!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
			request.setAttribute("error", "Please enter a valid phone number");
			forwardWithData(request, response);
			return;
		}

		try {
			boolean success = userService.registerUser(username, email, password, fullName, phoneNumber);

			if (success) {
				request.setAttribute("success", "Registration successful! Please login.");
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			} else {
				request.setAttribute("error", "Registration failed. Username or email may already exist.");
				forwardWithData(request, response);
			}
		} catch (IllegalArgumentException e) {
			request.setAttribute("error", e.getMessage());
			forwardWithData(request, response);
		} catch (Exception e) {
			request.setAttribute("error", "Registration failed. Please try again.");
			forwardWithData(request, response);
		}
	}

	private void forwardWithData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("username", request.getParameter("username"));
		request.setAttribute("email", request.getParameter("email"));
		request.setAttribute("fullName", request.getParameter("fullName"));
		request.setAttribute("phoneNumber", request.getParameter("phoneNumber"));
		request.getRequestDispatcher("/register.jsp").forward(request, response);
	}
}