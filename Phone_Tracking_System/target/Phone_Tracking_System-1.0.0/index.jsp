<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Phone Tracking System - Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .hero-container {
            text-align: center;
            color: white;
            padding: 2rem;
        }
        .hero-container h1 {
            font-size: 3rem;
            font-weight: bold;
            margin-bottom: 1rem;
        }
        .hero-container p {
            font-size: 1.2rem;
            margin-bottom: 2rem;
            opacity: 0.9;
        }
        .btn-custom {
            padding: 12px 30px;
            font-weight: 600;
            border-radius: 50px;
            margin: 0 10px;
            transition: all 0.3s ease;
        }
        .btn-custom:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 25px rgba(0,0,0,0.3);
        }
        .feature-box {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.2);
            border-radius: 15px;
            padding: 1.5rem;
            margin: 1rem;
            display: inline-block;
            min-width: 200px;
        }
        .feature-box i {
            font-size: 2.5rem;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <div class="hero-container">
        <i class="fas fa-map-marker-alt" style="font-size: 4rem; margin-bottom: 1rem;"></i>
        <h1>PhoneTracker Pro</h1>
        <p>Advanced Phone Location Tracking System</p>
        
        <div style="margin: 2rem 0;">
            <div class="feature-box">
                <i class="fas fa-crosshairs"></i>
                <h5>Precise Location</h5>
                <p>High accuracy tracking</p>
            </div>
            <div class="feature-box">
                <i class="fas fa-shield-alt"></i>
                <h5>Secure & Private</h5>
                <p>Your data is protected</p>
            </div>
            <div class="feature-box">
                <i class="fas fa-clock"></i>
                <h5>Real-time</h5>
                <p>Instant updates</p>
            </div>
        </div>
        
        <div style="margin-top: 2rem;">
            <a href="register" class="btn btn-primary btn-custom">
                <i class="fas fa-user-plus me-2"></i>Get Started
            </a>
            <a href="login" class="btn btn-outline-light btn-custom">
                <i class="fas fa-sign-in-alt me-2"></i>Login
            </a>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>