package com.example.ridehailingapp.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Ride {
    private int id;
    private int passengerId;
    private int driverId;
    private String pickupLocation;
    private String dropLocation;
    private String rideType;
    private String status;
    private double price;
    private long requestTime;
    private Date createdAt;
    private Date updatedAt;
    private Set<Integer> declinedDrivers = new HashSet<>();

    public Ride(int id,int passengerId,String pickupLocation,String dropLocation,String rideType) {
        this.id = id;
        this.passengerId = passengerId;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.rideType = rideType;
        this.status = "Requested";
        this.requestTime = System.currentTimeMillis();
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getPassengerId() { return passengerId; }
    public void setPassengerId(int passengerId) { this.passengerId = passengerId; }
    
    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { 
        this.driverId = driverId; 
        this.updatedAt = new Date();
    }
    
    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
    
    public String getDropLocation() { return dropLocation; }
    public void setDropLocation(String dropLocation) { this.dropLocation = dropLocation; }
    
    public String getRideType() { return rideType; }
    public void setRideType(String rideType) { this.rideType = rideType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = status; 
        this.updatedAt = new Date();
    }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public long getRequestTime() { return requestTime; }
    public void setRequestTime(long requestTime) { this.requestTime = requestTime; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    
    public boolean isRequested() { return "Requested".equals(status); }
    public boolean isAccepted() { return "Accepted".equals(status); }
    public boolean isInProgress() { return "In Progress".equals(status); }
    public boolean isCompleted() { return "Completed".equals(status); }

    public Set<Integer> getDeclinedDrivers() { return declinedDrivers; }
    public void addDeclinedDriver(int driverId) { declinedDrivers.add(driverId); }
    public boolean isDeclinedBy(int driverId) { return declinedDrivers.contains(driverId); }
}