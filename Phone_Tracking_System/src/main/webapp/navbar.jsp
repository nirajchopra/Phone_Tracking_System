<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.pts.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    String contextPath = request.getContextPath();
%>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand" href="<%= contextPath %>/">
            <i class="bi bi-geo-alt-fill me-2"></i>PhoneTracker Pro
        </a>
        
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <% if (user != null) { %>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= contextPath %>/dashboard">
                            <i class="bi bi-speedometer2 me-1"></i>Dashboard
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= contextPath %>/profile.jsp">
                            <i class="bi bi-person me-1"></i>Profile
                        </a>
                    </li>
                <% } %>
            </ul>
            
            <ul class="navbar-nav">
                <% if (user != null) { %>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" 
                           data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="bi bi-person-circle me-1"></i><%= user.getFullName() %>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                            <li><a class="dropdown-item" href="<%= contextPath %>/profile.jsp">
                                <i class="bi bi-person me-2"></i>My Profile
                            </a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="<%= contextPath %>/logout">
                                <i class="bi bi-box-arrow-right me-2"></i>Logout
                            </a></li>
                        </ul>
                    </li>
                <% } else { %>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= contextPath %>/login">Login</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= contextPath %>/register">Register</a>
                    </li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>