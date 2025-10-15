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
    private static final long serialVersionUID = 1L;
    private LocationService locationService;

    @Override
    public void init() throws ServletException {
        try {
            locationService = new LocationService();
            System.out.println("✓ LocationService initialized successfully");
        } catch (Exception e) {
            System.err.println("✗ Failed to initialize LocationService: " + e.getMessage());
            e.printStackTrace();
            // Don't throw exception, create service anyway
            try {
                locationService = new LocationService();
            } catch (Exception ex) {
                System.err.println("✗ Critical: Cannot create LocationService");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("→ TrackLocationServlet GET - Redirecting to dashboard");
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("\n========== TRACK LOCATION REQUEST ==========");
        
        // Check session
        HttpSession session = request.getSession(false);
        if (session == null) {
            System.out.println("✗ No session found");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Check user
        User user = (User) session.getAttribute("user");
        if (user == null) {
            System.out.println("✗ No user in session");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        System.out.println("✓ User authenticated: " + user.getUsername());

        // Get parameters
        String searchQuery = request.getParameter("searchQuery");
        String searchType = request.getParameter("searchType");

        System.out.println("→ Search Query: " + searchQuery);
        System.out.println("→ Search Type: " + searchType);

        // Initialize tracking history
        List<PhoneLocation> trackingHistory = new ArrayList<>();
        
        // Validate input
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            System.out.println("✗ Empty search query");
            request.setAttribute("error", "Please enter a phone number or email ID");
            loadTrackingHistory(request, user, trackingHistory);
            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
            return;
        }

        // Track location
        PhoneLocation location = null;

        try {
            System.out.println("→ Starting location tracking...");
            
            if ("phone".equals(searchType)) {
                System.out.println("→ Tracking by phone: " + searchQuery);
                location = locationService.trackByPhoneNumber(searchQuery, user);
            } else if ("email".equals(searchType)) {
                System.out.println("→ Tracking by email: " + searchQuery);
                location = locationService.trackByEmailId(searchQuery, user);
            } else {
                System.out.println("✗ Invalid search type: " + searchType);
                request.setAttribute("error", "Invalid search type");
                loadTrackingHistory(request, user, trackingHistory);
                request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
                return;
            }

            // Check result
            if (location != null) {
                System.out.println("✓ Location found successfully");
                System.out.println("  - City: " + location.getCity());
                System.out.println("  - Address: " + location.getAddress());
                System.out.println("  - Coordinates: " + location.getLatitude() + ", " + location.getLongitude());
                
                request.setAttribute("location", location);
                request.setAttribute("success", "Location tracked successfully!");
            } else {
                System.out.println("✗ Location not found");
                request.setAttribute("error", "Location not found for the given " 
                    + ("phone".equals(searchType) ? "phone number" : "email ID"));
            }

        } catch (IllegalArgumentException e) {
            System.err.println("✗ Validation error: " + e.getMessage());
            request.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ Error tracking location: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Failed to track location. Please try again.");
        }

        // Load tracking history
        loadTrackingHistory(request, user, trackingHistory);

        System.out.println("→ Forwarding to dashboard.jsp");
        System.out.println("==========================================\n");
        
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }

    /**
     * Load tracking history safely with error handling
     */
    private void loadTrackingHistory(HttpServletRequest request, User user, List<PhoneLocation> trackingHistory) {
        try {
            trackingHistory = locationService.getLocationHistory(user);
            System.out.println("✓ Tracking history loaded: " + trackingHistory.size() + " records");
            request.setAttribute("trackingHistory", trackingHistory);
        } catch (Exception e) {
            System.err.println("✗ Error loading tracking history: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("trackingHistory", new ArrayList<PhoneLocation>());
        }
        request.setAttribute("user", user);
    }

    @Override
    public void destroy() {
        System.out.println("✓ TrackLocationServlet destroyed");
        super.destroy();
    }
}