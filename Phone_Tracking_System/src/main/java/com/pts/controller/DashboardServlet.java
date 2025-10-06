package com.pts.controller;

import java.io.IOException;
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

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
	private LocationService locationService;

	@Override
	public void init() throws ServletException {
		locationService = new LocationService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		// Get user's tracking history
		List<PhoneLocation> trackingHistory = locationService.getLocationHistory(user);
		request.setAttribute("trackingHistory", trackingHistory);
		request.setAttribute("user", user);

		request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
	}
}