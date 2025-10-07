package com.pts.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    
    // URLs that don't require authentication
    private static final List<String> PUBLIC_URLS = Arrays.asList(
        "/login",
        "/register",
        "/login.jsp",
        "/register.jsp",
        "/dashboard.jsp",
        "/css/",
        "/js/",
        "/images/",
        "/assets/"
    );
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AuthenticationFilter initialized");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        System.out.println("AuthenticationFilter - Path: " + path);
        
        // Check if the path is public (doesn't require authentication)
        boolean isPublicURL = isPublicURL(path);
        
        if (isPublicURL) {
            // Allow access to public URLs
            chain.doFilter(request, response);
            return;
        }
        
        // Check if user is logged in
        HttpSession session = httpRequest.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("userId") != null);
        
        if (isLoggedIn) {
            // User is authenticated, continue
            chain.doFilter(request, response);
        } else {
            // User is not authenticated, redirect to login
            System.out.println("User not authenticated, redirecting to login");
            httpResponse.sendRedirect(contextPath + "/dashboard");
        }
    }
    
    private boolean isPublicURL(String path) {
        // Check if path starts with any public URL pattern
        for (String publicURL : PUBLIC_URLS) {
            if (path.equals(publicURL) || path.startsWith(publicURL)) {
                return true;
            }
        }
        
        // Allow root path
        if (path.equals("/") || path.isEmpty()) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public void destroy() {
        System.out.println("AuthenticationFilter destroyed");
    }
}