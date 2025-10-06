let map;
let modalMap;

document.addEventListener('DOMContentLoaded', function() {
    // Initialize search form
    const searchForm = document.getElementById('trackForm');
    const searchQuery = document.getElementById('searchQuery');
    const searchType = document.getElementById('searchType');
    
    // Auto-detect search type based on input
    if (searchQuery) {
        searchQuery.addEventListener('input', function() {
            const value = this.value.trim();
            
            if (value.includes('@')) {
                searchType.value = 'email';
            } else if (/^\+?\d/.test(value)) {
                searchType.value = 'phone';
            }
        });
    }
    
    // Form submission with loading state
    if (searchForm) {
        searchForm.addEventListener('submit', function(e) {
            const submitBtn = this.querySelector('button[type="submit"]');
            const originalText = submitBtn.innerHTML;
            
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Tracking...';
            submitBtn.disabled = true;
            
            // Re-enable button after 10 seconds (fallback)
            setTimeout(function() {
                submitBtn.innerHTML = originalText;
                submitBtn.disabled = false;
            }, 10000);
        });
    }
    
    // Auto-hide alerts
    setTimeout(function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            if (alert) {
                alert.style.opacity = '0';
                alert.style.transform = 'translateY(-10px)';
                setTimeout(function() {
                    if (alert.parentNode) {
                        alert.parentNode.removeChild(alert);
                    }
                }, 300);
            }
        });
    }, 5000);
});

// Initialize map function
function initializeMap(lat, lng, address) {
    if (typeof L === 'undefined') {
        console.error('Leaflet library not loaded');
        return;
    }
    
    const mapContainer = document.getElementById('map');
    if (!mapContainer) return;
    
    // Initialize map
    map = L.map('map').setView([lat, lng], 15);
    
    // Add tile layer
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors',
        maxZoom: 18
    }).addTo(map);
    
    // Add marker
    const marker = L.marker([lat, lng]).addTo(map);
    marker.bindPopup(`
        <div class="popup-content">
            <h6><i class="fas fa-map-marker-alt text-danger me-2"></i>Location Found</h6>
            <p class="mb-1"><strong>Address:</strong> ${address}</p>
            <p class="mb-1"><strong>Coordinates:</strong> ${lat.toFixed(6)}, ${lng.toFixed(6)}</p>
            <small class="text-muted">Tracked at ${new Date().toLocaleString()}</small>
        </div>
    `).openPopup();
    
    // Add circle to show accuracy
    L.circle([lat, lng], {
        color: 'red',
        fillColor: '#f03',
        fillOpacity: 0.1,
        radius: 100
    }).addTo(map);
}

// Show location on modal map
function showOnMap(lat, lng, address) {
    const modal = new bootstrap.Modal(document.getElementById('mapModal'));
    modal.show();
    
    // Wait for modal to be shown before initializing map
    document.getElementById('mapModal').addEventListener('shown.bs.modal', function() {
        if (modalMap) {
            modalMap.remove();
        }
        
        modalMap = L.map('modalMap').setView([lat, lng], 15);
        
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors',
            maxZoom: 18
        }).addTo(modalMap);
        
        const marker = L.marker([lat, lng]).addTo(modalMap);
        marker.bindPopup(`
            <div class="popup-content">
                <h6><i class="fas fa-map-marker-alt text-danger me-2"></i>Historical Location</h6>
                <p class="mb-0">${address}</p>
            </div>
        `).openPopup();
        
        // Update modal details
        document.getElementById('modalLocationDetails').innerHTML = `
            <strong>Address:</strong> ${address}<br>
            <strong>Coordinates:</strong> ${lat.toFixed(6)}, ${lng.toFixed(6)}
        `;
        
        // Resize map after modal is fully shown
        setTimeout(function() {
            modalMap.invalidateSize();
        }, 100);
    }, { once: true });
}

// Real-time validation for phone numbers and emails
function validateSearchQuery(query, type) {
    if (type === 'phone') {
        return /^(\+\d{1,3}[- ]?)?\d{10}$/.test(query);
    } else if (type === 'email') {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(query);
    }
    return false;
}

// Add loading animation to table rows
function addLoadingAnimation() {
    const tableRows = document.querySelectorAll('.table tbody tr');
    tableRows.forEach(function(row, index) {
        setTimeout(function() {
            row.style.opacity = '0';
            row.style.transform = 'translateY(20px)';
            row.style.transition = 'all 0.3s ease';
            
            setTimeout(function() {
                row.style.opacity = '1';
                row.style.transform = 'translateY(0)';
            }, 50);
        }, index * 100);
    });
}

// Call loading animation when page loads
document.addEventListener('DOMContentLoaded', function() {
    setTimeout(addLoadingAnimation, 500);
});