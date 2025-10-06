package com.pts.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pts.model.User;
import com.pts.service.UserService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private UserService userService;

	@Override
	public void init() throws ServletException {
		userService = new UserService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String usernameOrEmail = request.getParameter("usernameOrEmail");
		String password = request.getParameter("password");

		try {
			User user = userService.authenticateUser(usernameOrEmail, password);

			if (user != null) {
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				session.setMaxInactiveInterval(30 * 60); // 30 minutes

				response.sendRedirect(request.getContextPath() + "/dashboard");
			} else {
				request.setAttribute("error", "Invalid username/email or password");
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			}
		} catch (Exception e) {
			request.setAttribute("error", "Login failed. Please try again.");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}
}
