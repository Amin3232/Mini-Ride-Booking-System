package com.example.ridehailingapp.models;

public class User {
    private int id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String userType;
    private boolean isAvailable;

    public User(int id,String username,String password,String name,String email,String userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.userType = userType;
        this.isAvailable = true;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public boolean isDriver() {
        return "driver".equals(userType);
    }
    
    public boolean isPassenger() {
        return "passenger".equals(userType);
    }
}