package com.example.ridehailingapp.data;

import com.example.ridehailingapp.models.Ride;
import com.example.ridehailingapp.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DataManager {
    private static DataManager instance;
    private List<User> users;
    private List<Ride> rides;
    private int nextUserId = 1;
    private int nextRideId = 100000;
    private User currentUser;
    private Random random = new Random();

    private DataManager() {
        users = new ArrayList<>();
        rides = new ArrayList<>();
        
        users.add(new User(1,"john","password123","John Doe","john@example.com","passenger"));
        users.add(new User(2,"jane","password123","Jane Smith","jane@example.com","passenger"));
        users.add(new User(3,"driver1","password123","Mike Johnson","mike@example.com","driver"));
        users.add(new User(4,"driver2","password123","Sarah Wilson","sarah@example.com","driver"));
        
        rides.add(new Ride(100001,1,"Central Park","Times Square","Car"));
        rides.add(new Ride(100002,2,"Brooklyn Bridge","Manhattan","Bike"));
        
        nextUserId = 5;
        nextRideId = 100003;
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private void initializeSampleData() {
        users.add(new User(nextUserId++,"passenger1","1234","John Passenger","john.passenger@example.com","passenger"));
        users.add(new User(nextUserId++,"driver1","1234","Mike Driver","mike.driver@example.com","driver"));
        users.add(new User(nextUserId++,"driver2","1234","Sarah Driver","sarah.driver@example.com","driver"));
        
        rides.add(new Ride(nextRideId++,1,"Mall Road","Airport","Car"));
        rides.add(new Ride(nextRideId++,1,"University","Shopping Center","Mini Car"));
        
        rides.get(0).setStatus("Completed");
        rides.get(0).setDriverId(2);
        rides.get(1).setStatus("In Progress");
        rides.get(1).setDriverId(3);
    }

    public User authenticateUser(String username,String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return user;
            }
        }
        return null;
    }

    public User registerUser(String username,String password,String name,String email,String userType) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return null;
            }
        }
        
        User newUser = new User(nextUserId++,username,password,name,email,userType);
        users.add(newUser);
        return newUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }

    public List<User> getAvailableDrivers() {
        return users.stream()
                .filter(User::isDriver)
                .filter(User::isAvailable)
                .collect(Collectors.toList());
    }

    public Ride createRide(int passengerId,String pickupLocation,String dropLocation,String rideType) {
        Ride ride = new Ride(nextRideId++,passengerId,pickupLocation,dropLocation,rideType);
        rides.add(ride);
        return ride;
    }

    public Ride getRideById(int rideId) {
        for (Ride ride : rides) {
            if (ride.getId() == rideId) {
                return ride;
            }
        }
        return null;
    }

    public List<Ride> getRidesByUserId(int userId) {
        return rides.stream()
                .filter(ride -> ride.getPassengerId() == userId || ride.getDriverId() == userId)
                .collect(Collectors.toList());
    }

    public List<Ride> getRequestedRides() {
        return rides.stream()
                .filter(ride -> "Requested".equals(ride.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Ride> getRequestedRidesForDriver(int driverId) {
        return rides.stream()
                .filter(ride -> "Requested".equals(ride.getStatus()))
                .filter(ride -> !ride.isDeclinedBy(driverId))
                .collect(Collectors.toList());
    }

    public void addRide(Ride ride) {
        rides.add(ride);
    }

    public boolean hasActiveRide(int passengerId) {
        return rides.stream()
                .anyMatch(ride -> ride.getPassengerId() == passengerId && ("Requested".equals(ride.getStatus()) || "Accepted".equals(ride.getStatus()) || "In Progress".equals(ride.getStatus())));
    }

    public boolean acceptRide(int rideId,int driverId) {
        Ride ride = getRideById(rideId);
        if (ride != null && ride.isRequested()) {
            ride.setDriverId(driverId);
            ride.setStatus("Accepted");
            
            User driver = getUserById(driverId);
            if (driver != null) {
                driver.setAvailable(false);
            }
            return true;
        }
        return false;
    }

    public boolean updateRideStatus(int rideId,String status) {
        Ride ride = getRideById(rideId);
        if (ride != null) {
            ride.setStatus(status);
            
            if ("Completed".equals(status)) {
                User driver = getUserById(ride.getDriverId());
                if (driver != null) {
                    driver.setAvailable(true);
                }
            }
            return true;
        }
        return false;
    }

    public boolean cancelRide(int rideId) {
        Ride ride = getRideById(rideId);
        if (ride != null && "Requested".equals(ride.getStatus())) {
            ride.setStatus("Cancelled");
            return true;
        }
        return false;
    }

    public boolean rejectRide(int rideId,int driverId) {
        Ride ride = getRideById(rideId);
        if (ride != null && "Requested".equals(ride.getStatus())) {
            ride.addDeclinedDriver(driverId);
            return true;
        }
        return false;
    }

    public User getUserById(int userId) {
        for (User user : users) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }

    public List<Ride> getRidesForDriver(int driverId) {
        return rides.stream()
                .filter(ride -> ride.getDriverId() == driverId)
                .collect(Collectors.toList());
    }

    public double calculatePrice(String pickupLocation,String dropLocation,String rideType) {
        double basePrice = 0;
        switch (rideType) {
            case "Bike":
                basePrice = 5.0;
                break;
            case "Mini Car":
                basePrice = 8.0;
                break;
            case "Car":
                basePrice = 12.0;
                break;
        }
        
        double locationMultiplier = 1.0 + (pickupLocation.length() + dropLocation.length()) * 0.01;
        
        return Math.round((basePrice * locationMultiplier) * 100.0) / 100.0;
    }

    public String getRideTypeDisplayName(String rideType) {
        switch (rideType) {
            case "Bike":
                return "Bike";
            case "Mini Car":
                return "Mini Car";
            case "Car":
                return "Car";
            default:
                return rideType;
        }
    }

    public boolean driverHasActiveRide(int driverId) {
        return rides.stream()
                .anyMatch(ride -> ride.getDriverId() == driverId && ("Accepted".equals(ride.getStatus()) || "In Progress".equals(ride.getStatus())));
    }

    public Ride getActiveRideForUser(int userId,boolean isDriver) {
        if (isDriver) {
            return rides.stream()
                    .filter(ride -> ride.getDriverId() == userId && ("Accepted".equals(ride.getStatus()) || "In Progress".equals(ride.getStatus())))
                    .findFirst().orElse(null);
        } else {
            return rides.stream()
                    .filter(ride -> ride.getPassengerId() == userId && ("Requested".equals(ride.getStatus()) || "Accepted".equals(ride.getStatus()) || "In Progress".equals(ride.getStatus())))
                    .findFirst().orElse(null);
        }
    }
} 