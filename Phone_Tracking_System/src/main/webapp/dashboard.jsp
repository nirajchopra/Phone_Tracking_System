<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Dashboard - Phone Tracking System</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://unpkg.com/leaflet@1.7.1/dist/leaflet.css">
<link href="css/dashboard.css" rel="stylesheet">
</head>
<body>
	<!-- Navigation -->
	<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
		<div class="container">
			<a class="navbar-brand" href="#"> <i
				class="fas fa-map-marker-alt me-2"></i> PhoneTracker Pro
			</a>

			<div class="navbar-nav ms-auto">
				<div class="nav-item dropdown">
					<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
						role="button" data-bs-toggle="dropdown"> <i
						class="fas fa-user me-2"></i> ${user.fullName}
					</a>
					<ul class="dropdown-menu">
						<li><a class="dropdown-item" href="#"><i
								class="fas fa-user-edit me-2"></i>Profile</a></li>
						<li><a class="dropdown-item" href="#"><i
								class="fas fa-cog me-2"></i>Settings</a></li>
						<li><hr class="dropdown-divider"></li>
						<li><a class="dropdown-item" href="logout"><i
								class="fas fa-sign-out-alt me-2"></i>Logout</a></li>
					</ul>
				</div>
			</div>
		</div>
	</nav>

	<!-- Main Content -->
	<div class="container mt-4">
		<!-- Alert Messages -->
		<c:if test="${not empty error}">
			<div class="alert alert-danger alert-dismissible fade show">
				<i class="fas fa-exclamation-circle me-2"></i> ${error}
				<button type="button" class="btn-close" data-bs-dismiss="alert"></button>
			</div>
		</c:if>

		<c:if test="${not empty success}">
			<div class="alert alert-success alert-dismissible fade show">
				<i class="fas fa-check-circle me-2"></i> ${success}
				<button type="button" class="btn-close" data-bs-dismiss="alert"></button>
			</div>
		</c:if>

		<!-- Dashboard Header -->
		<div class="row mb-4">
			<div class="col-12">
				<h1 class="display-6 mb-3">
					<i class="fas fa-tachometer-alt me-2"></i> Dashboard
				</h1>
				<p class="text-muted">Track phone numbers and email IDs with
					precise location data</p>
			</div>
		</div>

		<!-- Search Section -->
		<div class="row mb-4">
			<div class="col-12">
				<div class="card shadow-sm">
					<div class="card-header bg-primary text-white">
						<h5 class="mb-0">
							<i class="fas fa-search me-2"></i> Track Location
						</h5>
					</div>
					<div class="card-body">
						<form method="post" action="track-location" id="trackForm">
							<div class="row g-3">
								<div class="col-md-6">
									<label for="searchQuery" class="form-label">Phone
										Number / Email ID</label> <input type="text"
										class="form-control form-control-lg" id="searchQuery"
										name="searchQuery" placeholder="Enter phone number or email"
										required>
								</div>
								<div class="col-md-3">
									<label for="searchType" class="form-label">Search Type</label>
									<select class="form-select form-select-lg" id="searchType"
										name="searchType">
										<option value="phone">Phone Number</option>
										<option value="email">Email ID</option>
									</select>
								</div>
								<div class="col-md-3 d-flex align-items-end">
									<button type="submit" class="btn btn-primary btn-lg w-100">
										<i class="fas fa-crosshairs me-2"></i>Track Now
									</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>

		<!-- Location Result -->
		<c:if test="${not empty location}">
			<div class="row mb-4">
				<div class="col-12">
					<div class="card shadow-sm border-success">
						<div class="card-header bg-success text-white">
							<h5 class="mb-0">
								<i class="fas fa-map-marker-alt me-2"></i> Location Found
							</h5>
						</div>
						<div class="card-body">
							<div class="row">
								<div class="col-md-6">
									<h6 class="text-muted">Location Details</h6>
									<div class="location-details">
										<p>
											<strong>Address:</strong> ${location.address}
										</p>
										<p>
											<strong>City:</strong> ${location.city}
										</p>
										<p>
											<strong>Country:</strong> ${location.country}
										</p>
										<p>
											<strong>Coordinates:</strong> ${location.latitude},
											${location.longitude}
										</p>
										<p>
											<strong>Accuracy:</strong> <span class="badge bg-success">${location.accuracy}</span>
										</p>
										<p>
											<strong>Tracked At:</strong>
											<fmt:formatDate value="${location.trackedAt}"
												pattern="yyyy-MM-dd HH:mm:ss" />
										</p>
									</div>
								</div>
								<div class="col-md-6">
									<h6 class="text-muted">Live Map</h6>
									<div id="map" style="height: 300px; border-radius: 8px;"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</c:if>

		<!-- Tracking History -->
		<div class="row">
			<div class="col-12">
				<div class="card shadow-sm">
					<div class="card-header">
						<h5 class="mb-0">
							<i class="fas fa-history me-2"></i> Tracking History
						</h5>
					</div>
					<div class="card-body">
						<c:choose>
							<c:when test="${not empty trackingHistory}">
								<div class="table-responsive">
									<table class="table table-hover">
										<thead class="table-dark">
											<tr>
												<th>Query</th>
												<th>Location</th>
												<th>City</th>
												<th>Coordinates</th>
												<th>Tracked At</th>
												<th>Actions</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${trackingHistory}" var="track"
												varStatus="status">
												<tr>
													<td><c:choose>
															<c:when test="${not empty track.phoneNumber}">
																<i class="fas fa-phone text-primary me-2"></i>
                                                                ${track.phoneNumber}
                                                            </c:when>
															<c:otherwise>
																<i class="fas fa-envelope text-info me-2"></i>
                                                                ${track.emailId}
                                                            </c:otherwise>
														</c:choose></td>
													<td>${track.address}</td>
													<td>${track.city}</td>
													<td><small class="text-muted">
															${track.latitude}, ${track.longitude} </small></td>
													<td>${track.trackedAt.toString().replace('T', ' ').substring(0, 16).split('-')[2]}/${track.trackedAt.toString().split('-')[1]}/${track.trackedAt.toString().split('-')[0]}
														${track.trackedAt.toString().split('T')[1].substring(0, 5)}
													</td>
													<td>
														<button class="btn btn-sm btn-outline-primary"
															onclick="showOnMap(${track.latitude}, ${track.longitude}, '${track.address}')">
															<i class="fas fa-map me-1"></i>View
														</button>
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</c:when>
							<c:otherwise>
								<div class="text-center py-5">
									<i class="fas fa-search fa-3x text-muted mb-3"></i>
									<h5 class="text-muted">No tracking history yet</h5>
									<p class="text-muted">Start tracking phone numbers or email
										IDs to see your history here.</p>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Map Modal -->
	<div class="modal fade" id="mapModal" tabindex="-1">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">
						<i class="fas fa-map me-2"></i> Location Map
					</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
				</div>
				<div class="modal-body">
					<div id="modalMap" style="height: 400px;"></div>
					<div class="mt-3">
						<h6>Location Details:</h6>
						<p id="modalLocationDetails"></p>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
	<script src="https://unpkg.com/leaflet@1.7.1/dist/leaflet.js"></script>
	<script src="js/dashboard.js"></script>

	<c:if test="${not empty location}">
		<script>
            // Initialize map with current location
            document.addEventListener('DOMContentLoaded', function() {
                initializeMap('${location.latitude}', '${location.longitude}', '${location.address}');
            });
        </script>
	</c:if>
</body>
</html>