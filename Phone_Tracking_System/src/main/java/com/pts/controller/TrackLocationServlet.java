package com.pts.controller;

import java.io.IOException;

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
		locationService = new LocationService();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		String searchQuery = request.getParameter("searchQuery");
		String searchType = request.getParameter("searchType");

		if (!ValidationUtil.isNotEmpty(searchQuery)) {
			request.setAttribute("error", "Please enter a phone number or email ID");
			request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
			return;
		}

		PhoneLocation location = null;

		try {
			if ("phone".equals(searchType)) {
				location = locationService.trackByPhoneNumber(searchQuery, user);
			} else if ("email".equals(searchType)) {
				location = locationService.trackByEmailId(searchQuery, user);
			}

			if (location != null) {
				request.setAttribute("location", location);
				request.setAttribute("success", "Location found successfully!");
			} else {
				request.setAttribute("error", "Location not found for the given "
						+ ("phone".equals(searchType) ? "phone number" : "email ID"));
			}
		} catch (IllegalArgumentException e) {
			request.setAttribute("error", e.getMessage());
		} catch (Exception e) {
			request.setAttribute("error", "Failed to track location. Please try again.");
		}

		// Reload dashboard with results
		request.getRequestDispatcher("/dashboard").forward(request, response);
	}
}