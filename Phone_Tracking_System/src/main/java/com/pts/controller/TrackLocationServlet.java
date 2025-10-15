package com.pts.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pts.model.PhoneLocation;
import com.pts.model.User;
import com.pts.service.LocationService;
import com.pts.util.ValidationUtil;

@WebServlet("/track-location")
public class TrackLocationServlet extends HttpServlet {
	private LocationService locationService;

	@Override
	public void init() throws ServletException {
		try {
			locationService = new LocationService();
			System.out.println("LocationService initialized successfully");
		} catch (Exception e) {
			System.err.println("Failed to initialize LocationService: " + e.getMessage());
			e.printStackTrace();
			throw new ServletException("Failed to initialize LocationService", e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect(request.getContextPath() + "/dashboard");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("TrackLocationServlet - POST request received");

		HttpSession session = request.getSession(false);
		if (session == null) {
			System.out.println("No session found, redirecting to login");
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		User user = (User) session.getAttribute("user");
		if (user == null) {
			System.out.println("No user in session, redirecting to login");
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		System.out.println("User found: " + user.getUsername());

		String searchQuery = request.getParameter("searchQuery");
		String searchType = request.getParameter("searchType");

		System.out.println("Search Query: " + searchQuery);
		System.out.println("Search Type: " + searchType);

		// Initialize tracking history
		List<PhoneLocation> trackingHistory = new ArrayList<>();
		
		try {
			trackingHistory = locationService.getLocationHistory(user);
			System.out.println("Tracking history retrieved: " + trackingHistory.size() + " records");
		} catch (Exception e) {
			System.err.println("Error getting tracking history: " + e.getMessage());
			e.printStackTrace();
		}

		if (!ValidationUtil.isNotEmpty(searchQuery)) {
			System.out.println("Empty search query");
			request.setAttribute("error", "Please enter a phone number or email ID");
			request.setAttribute("trackingHistory", trackingHistory);
			request.setAttribute("user", user);
			request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
			return;
		}

		PhoneLocation location = null;

		try {
			System.out.println("Starting location tracking...");
			
			if ("phone".equals(searchType)) {
				System.out.println("Tracking by phone number: " + searchQuery);
				location = locationService.trackByPhoneNumber(searchQuery, user);
			} else if ("email".equals(searchType)) {
				System.out.println("Tracking by email: " + searchQuery);
				location = locationService.trackByEmailId(searchQuery, user);
			}

			if (location != null) {
				System.out.println("Location found: " + location.getCity());
				request.setAttribute("location", location);
				request.setAttribute("success", "Location found successfully!");
			} else {
				System.out.println("Location not found");
				request.setAttribute("error", "Location not found for the given "
						+ ("phone".equals(searchType) ? "phone number" : "email ID"));
			}
		} catch (IllegalArgumentException e) {
			System.err.println("Validation error: " + e.getMessage());
			request.setAttribute("error", e.getMessage());
		} catch (Exception e) {
			System.err.println("Error tracking location: " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("error", "Failed to track location. Please try again. Error: " + e.getMessage());
		}

		// Refresh tracking history
		try {
			trackingHistory = locationService.getLocationHistory(user);
			System.out.println("Updated tracking history: " + trackingHistory.size() + " records");
		} catch (Exception e) {
			System.err.println("Error refreshing tracking history: " + e.getMessage());
			e.printStackTrace();
		}

		request.setAttribute("trackingHistory", trackingHistory);
		request.setAttribute("user", user);

		System.out.println("Forwarding to dashboard.jsp");
		request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
	}
}