<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.pts.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile - Phone Tracking System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
        }
        .profile-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 3rem 0;
            margin-bottom: 2rem;
        }
        .profile-avatar {
            width: 120px;
            height: 120px;
            background: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 48px;
            color: #212529;
            margin-bottom: 15px;
        }
        .badge-role {
            font-size: 14px;
            padding: 8px 15px;
        }
        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 2px 15px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        .info-item {
            padding: 15px;
            border-bottom: 1px solid #e9ecef;
        }
        .info-item:last-child {
            border-bottom: none;
        }
        .info-label {
            font-weight: 600;
            color: #6c757d;
            margin-bottom: 5px;
        }
        .info-value {
            color: #212529;
            font-size: 1.1rem;
        }
    </style>
</head>
<body>
    <!-- Include Navigation -->
    <jsp:include page="navbar.jsp" />
    
    <!-- Profile Header -->
    <div class="profile-header">
        <div class="container text-center">
            <div class="profile-avatar mx-auto">
                <i class="bi bi-person-fill"></i>
            </div>
            <h2>${user.fullName}</h2>
            <p class="mb-2">@${user.username}</p>
            <span class="badge badge-role bg-light text-dark">
                <i class="bi bi-shield-check me-1"></i>${user.role}
            </span>
        </div>
    </div>
    
    <!-- Main Content -->
    <div class="container pb-5">
        <% if (request.getAttribute("successMessage") != null) { %>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-check-circle-fill me-2"></i>${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <% } %>
        
        <div class="row">
            <!-- Profile Information -->
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0">
                            <i class="bi bi-person-badge me-2"></i>Profile Information
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="info-item">
                            <div class="info-label">Full Name</div>
                            <div class="info-value">${user.fullName}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Username</div>
                            <div class="info-value">${user.username}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Email Address</div>
                            <div class="info-value">${user.email}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Phone Number</div>
                            <div class="info-value">${user.phoneNumber}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Account Status</div>
                            <div class="info-value">
                                <% if (user.isActive()) { %>
                                    <span class="badge bg-success">
                                        <i class="bi bi-check-circle me-1"></i>Active
                                    </span>
                                <% } else { %>
                                    <span class="badge bg-danger">
                                        <i class="bi bi-x-circle me-1"></i>Inactive
                                    </span>
                                <% } %>
                            </div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Member Since</div>
                            <div class="info-value">
                                <i class="bi bi-calendar me-2"></i>
                                <%= new java.text.SimpleDateFormat("MMMM dd, yyyy").format(
                                    java.sql.Timestamp.valueOf(user.getCreatedAt())) %>
                            </div>
                        </div>
                        <% if (user.getLastLogin() != null) { %>
                            <div class="info-item">
                                <div class="info-label">Last Login</div>
                                <div class="info-value">
                                    <i class="bi bi-clock me-2"></i>
                                    <%= new java.text.SimpleDateFormat("MMMM dd, yyyy HH:mm").format(
                                        java.sql.Timestamp.valueOf(user.getLastLogin())) %>
                                </div>
                            </div>
                        <% } %>
                    </div>
                </div>
            </div>
            
            <!-- Quick Actions -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header bg-success text-white">
                        <h5 class="mb-0">
                            <i class="bi bi-lightning-fill me-2"></i>Quick Actions
                        </h5>
                    </div>
                    <div class="card-body">
                        <a href="<%= request.getContextPath() %>/dashboard" class="btn btn-primary w-100 mb-3">
                            <i class="bi bi-speedometer2 me-2"></i>Go to Dashboard
                        </a>
                        <a href="<%= request.getContextPath() %>/edit-profile" class="btn btn-outline-primary w-100 mb-3">
                            <i class="bi bi-pencil me-2"></i>Edit Profile
                        </a>
                        <a href="<%= request.getContextPath() %>/change-password" class="btn btn-outline-warning w-100 mb-3">
                            <i class="bi bi-key me-2"></i>Change Password
                        </a>
                        <hr>
                        <a href="<%= request.getContextPath() %>/logout" class="btn btn-outline-danger w-100">
                            <i class="bi bi-box-arrow-right me-2"></i>Logout
                        </a>
                    </div>
                </div>
                
                <!-- Account Stats -->
                <div class="card">
                    <div class="card-header bg-info text-white">
                        <h5 class="mb-0">
                            <i class="bi bi-graph-up me-2"></i>Account Stats
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span><i class="bi bi-search me-2"></i>Total Searches</span>
                            <span class="badge bg-primary rounded-pill">0</span>
                        </div>
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span><i class="bi bi-geo-alt me-2"></i>Locations Tracked</span>
                            <span class="badge bg-success rounded-pill">0</span>
                        </div>
                        <div class="d-flex justify-content-between align-items-center">
                            <span><i class="bi bi-calendar-day me-2"></i>Days Active</span>
                            <span class="badge bg-info rounded-pill">
                                <%= java.time.temporal.ChronoUnit.DAYS.between(
                                    user.getCreatedAt().toLocalDate(), 
                                    java.time.LocalDate.now()) %>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>