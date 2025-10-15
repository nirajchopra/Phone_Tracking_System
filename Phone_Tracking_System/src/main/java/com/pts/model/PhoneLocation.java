package com.pts.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "phone_locations")
public class PhoneLocation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "phone_number", length = 20, nullable = true)
    private String phoneNumber;
    
    @Column(name = "email_id", length = 100, nullable = true)
    private String emailId;
    
    @Column(name = "latitude", nullable = false)
    private double latitude;
    
    @Column(name = "longitude", nullable = false)
    private double longitude;
    
    @Column(name = "city", length = 100)
    private String city;
    
    @Column(name = "state", length = 100)
    private String state;
    
    @Column(name = "country", length = 100)
    private String country;
    
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;
    
    @Column(name = "accuracy", length = 50)
    private String accuracy;
    
    // CRITICAL: Temporal annotation for Date field with default value
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "tracked_at", nullable = false, updatable = false)
    private Date trackedAt;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tracked_by_user_id")
    private User trackedByUser;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    // Default Constructor - ALWAYS initialize trackedAt
    public PhoneLocation() {
        this.trackedAt = new Date();
        this.createdAt = new Date();
    }

    // Full Constructor
    public PhoneLocation(String phoneNumber, String emailId, double latitude, double longitude,
                        String city, String state, String country, String address, 
                        String accuracy, User trackedByUser) {
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.state = state;
        this.country = country;
        this.address = address;
        this.accuracy = accuracy;
        this.trackedByUser = trackedByUser;
        this.trackedAt = new Date();
        this.createdAt = new Date();
    }

    // PrePersist hook to ensure dates are set before saving
    @PrePersist
    protected void onCreate() {
        if (this.trackedAt == null) {
            this.trackedAt = new Date();
        }
        if (this.createdAt == null) {
            this.createdAt = new Date();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * CRITICAL: Null-safe getter for trackedAt
     * Always returns a valid Date object
     */
    public Date getTrackedAt() {
        if (this.trackedAt == null) {
            this.trackedAt = new Date();
        }
        return this.trackedAt;
    }

    /**
     * CRITICAL: Null-safe setter for trackedAt
     * Prevents null values from being set
     */
    public void setTrackedAt(Date trackedAt) {
        this.trackedAt = (trackedAt != null) ? trackedAt : new Date();
    }

    public User getTrackedByUser() {
        return trackedByUser;
    }

    public void setTrackedByUser(User trackedByUser) {
        this.trackedByUser = trackedByUser;
    }

    public Date getCreatedAt() {
        if (this.createdAt == null) {
            this.createdAt = new Date();
        }
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = (createdAt != null) ? createdAt : new Date();
    }

    @Override
    public String toString() {
        return "PhoneLocation{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emailId='" + emailId + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", accuracy='" + accuracy + '\'' +
                ", trackedAt=" + trackedAt +
                ", trackedByUser=" + (trackedByUser != null ? trackedByUser.getUsername() : "null") +
                '}';
    }
}