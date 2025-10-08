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
        
        System.out.println("===== LOGIN ATTEMPT =====");
        System.out.println("Username/Email: " + usernameOrEmail);
        
        try {
            User user = userService.authenticateUser(usernameOrEmail, password);
            
            if (user != null) {
                System.out.println("Authentication SUCCESS for: " + user.getUsername());
                
                // Create new session
                HttpSession session = request.getSession(true);
                
                // Set session attributes
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                
//                String userRole = "USER"; // default value
//                if (user.getRole() != null) {
//                    userRole = user.getRole().name();
//                }
//                session.setAttribute("userRole", userRole);
//                
                session.setMaxInactiveInterval(30 * 60); // 30 minutes
                
                System.out.println("Session ID: " + session.getId());
                System.out.println("UserId set: " + session.getAttribute("userId"));
                System.out.println("Username set: " + session.getAttribute("username"));
                System.out.println("UserRole set: " + session.getAttribute("userRole"));
                System.out.println("Redirecting to dashboard...");
                System.out.println("=========================");
                
                // Redirect to dashboard
                response.sendRedirect(request.getContextPath() + "/dashboard");
                
            } else {
                System.out.println("Authentication FAILED");
                request.setAttribute("error", "Invalid username/email or password");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Login failed. Please try again.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}