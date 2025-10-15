package com.pts.controller;

import com.pts.dao.UserDAO;
import com.pts.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    
    private UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        User sessionUser = (User) session.getAttribute("user");
        
        // Fetch fresh user data from database
        User user = userDAO.findById(sessionUser.getId());
        
        if (user != null) {
            request.setAttribute("user", user);
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        User sessionUser = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        
        if ("update".equals(action)) {
            updateProfile(request, response, sessionUser);
        } else if ("changePassword".equals(action)) {
            changePassword(request, response, sessionUser);
        }
    }
    
    private void updateProfile(HttpServletRequest request, HttpServletResponse response,
            User sessionUser) throws ServletException, IOException {
        
        try {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phoneNumber = request.getParameter("phoneNumber");
            
            // Get fresh user data
            User user = userDAO.findById(sessionUser.getId());
            
            if (user != null) {
                user.setFullName(fullName);
                user.setEmail(email);
                user.setPhoneNumber(phoneNumber);
                
                userDAO.updateUser(user);
                
                // Update session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                
                request.setAttribute("successMessage", "Profile updated successfully!");
                request.setAttribute("user", user);
            } else {
                request.setAttribute("errorMessage", "User not found.");
                request.setAttribute("user", sessionUser);
            }
        } catch (Exception e) {
            System.err.println("[PROFILE SERVLET] Update error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Failed to update profile: " + e.getMessage());
            request.setAttribute("user", sessionUser);
        }
        
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }
    
    private void changePassword(HttpServletRequest request, HttpServletResponse response,
            User sessionUser) throws ServletException, IOException {
        
        try {
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");
            
            // Get fresh user data
            User user = userDAO.findById(sessionUser.getId());
            
            if (user == null) {
                request.setAttribute("errorMessage", "User not found.");
                request.setAttribute("user", sessionUser);
                request.getRequestDispatcher("profile.jsp").forward(request, response);
                return;
            }
            
            // Validate current password
            if (!user.getPassword().equals(currentPassword)) {
                request.setAttribute("errorMessage", "Current password is incorrect.");
                request.setAttribute("user", user);
                request.getRequestDispatcher("profile.jsp").forward(request, response);
                return;
            }
            
            // Validate new password match
            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("errorMessage", "New passwords do not match.");
                request.setAttribute("user", user);
                request.getRequestDispatcher("profile.jsp").forward(request, response);
                return;
            }
            
            // Validate password length
            if (newPassword.length() < 6) {
                request.setAttribute("errorMessage", "Password must be at least 6 characters long.");
                request.setAttribute("user", user);
                request.getRequestDispatcher("profile.jsp").forward(request, response);
                return;
            }
            
            // Update password
            boolean updated = userDAO.updatePassword(user.getId(), newPassword);
            
            if (updated) {
                // Refresh user object
                user = userDAO.findById(user.getId());
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                
                request.setAttribute("successMessage", "Password changed successfully!");
                request.setAttribute("user", user);
            } else {
                request.setAttribute("errorMessage", "Failed to change password.");
                request.setAttribute("user", user);
            }
        } catch (Exception e) {
            System.err.println("[PROFILE SERVLET] Change password error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Failed to change password: " + e.getMessage());
            request.setAttribute("user", sessionUser);
        }
        
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }
}