# Mini Ride Booking System - Android App

A complete ride-hailing app prototype built in Java for Android, designed for testing in smaller cities. This app simulates the basic ride booking system without real-time maps or GPS integration.

## 🚀 Features

### Core Features
- ✅ **User Registration/Login** - Mock authentication system
- ✅ **Request a Ride** - Enter pickup/drop locations and choose ride type
- ✅ **View Ride Status** - Track ride progress (Requested → Accepted → In Progress → Completed)
- ✅ **View Ride History** - Complete history of all rides for the user
- ✅ **Driver Accepts Ride** - Drivers can view and accept available ride requests
- ✅ **Basic UI** - Clean, intuitive user interface

### User Types
- **Passengers**: Can request rides, view status, and check history
- **Drivers**: Can view available requests, accept rides, and update ride status

### Ride Types
- 🚗 Car
- 🏍️ Bike  
- 🛺 Rickshaw

### Ride Status Flow
1. **Requested** - Ride has been created by passenger
2. **Accepted** - Driver has accepted the ride
3. **In Progress** - Driver has started the ride
4. **Completed** - Ride has been completed

## 🛠️ Tech Stack

- **Language**: Java
- **Platform**: Android
- **Architecture**: MVC Pattern
- **Data Storage**: In-memory (DataManager singleton)
- **UI**: Native Android Views
- **Minimum SDK**: API 24 (Android 7.0)

## 📱 Screenshots & Flow

### User Flow
1. **Login/Register** → User authentication
2. **Home Screen** → Different options for passengers vs drivers
3. **Request Ride** → Passengers can book rides
4. **View Requests** → Drivers can see available rides
5. **Ride Status** → Real-time status tracking
6. **Ride History** → Complete ride history

## 🏗️ Project Structure

```
app/src/main/java/com/example/ridehailingapp/
├── MainActivity.java              # App launcher
├── LoginActivity.java             # User authentication
├── RegisterActivity.java          # User registration
├── HomeActivity.java              # Main dashboard
├── RideRequestActivity.java       # Book a ride
├── RideStatusActivity.java        # View ride status
├── RideHistoryActivity.java       # View ride history
├── DriverRequestsActivity.java    # Driver ride requests
├── data/
│   └── DataManager.java           # Data management singleton
└── models/
    ├── User.java                  # User model
    └── Ride.java                  # Ride model
```

## 🗄️ Data Models

### User Model
```java
- id: int
- username: String
- password: String
- name: String
- userType: String ("passenger" or "driver")
- isAvailable: boolean
```

### Ride Model
```java
- id: int
- passengerId: int
- driverId: int
- pickupLocation: String
- dropLocation: String
- rideType: String ("Bike", "Car", "Rickshaw")
- status: String ("Requested", "Accepted", "In Progress", "Completed")
- createdAt: Date
- updatedAt: Date
```

## 🚀 Getting Started

### Prerequisites
- Android Studio
- Android SDK (API 24+)
- Java 11

### Installation
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Build and run on an emulator or device

### Sample Credentials
The app comes with pre-loaded sample data:

**Passengers:**
- Username: `passenger1`, Password: `1234`

**Drivers:**
- Username: `driver1`, Password: `1234`
- Username: `driver2`, Password: `1234`

## 🎯 Usage Guide

### For Passengers
1. Login with passenger credentials
2. Click "Request a Ride"
3. Enter pickup and drop locations
4. Select ride type (Bike/Car/Rickshaw)
5. Submit request
6. View ride status and history

### For Drivers
1. Login with driver credentials
2. Click "View Ride Requests"
3. Browse available ride requests
4. Accept desired rides
5. Update ride status as you progress
6. View your ride history

## 🔧 Key Features Implementation

### Data Management
- **Singleton Pattern**: DataManager ensures single instance of data
- **In-Memory Storage**: All data stored in memory for simplicity
- **Sample Data**: Pre-loaded with sample users and rides

### User Authentication
- **Mock System**: Simple username/password validation
- **User Types**: Separate flows for passengers and drivers
- **Session Management**: Current user tracking

### Ride Management
- **Status Tracking**: Complete ride lifecycle management
- **Driver Assignment**: Automatic driver availability management
- **History Tracking**: Complete ride history for all users

## 🎨 UI/UX Features

- **Modern Material Design**: Clean, card-based interface with elevation and rounded corners
- **Colorful Theme**: Professional blue and orange color scheme with accent colors
- **Responsive Design**: Works on different screen sizes with proper spacing
- **Intuitive Navigation**: Clear button labels and logical flow
- **Status Indicators**: Color-coded status badges and visual status updates
- **Empty States**: Proper handling when no data is available
- **Confirmation Dialogs**: Important actions require confirmation
- **Custom List Items**: Beautiful card-based list items for ride history and requests
- **Rounded Inputs**: Modern rounded EditText fields with subtle borders
- **Consistent Typography**: Proper text hierarchy and readable fonts

## 🔮 Future Enhancements

- Real-time GPS integration
- Payment system integration
- Push notifications
- Real database (SQLite/Room)
- User ratings and reviews
- Ride pricing calculation
- Driver earnings tracking

## 📋 Assumptions Made

1. **No Real Authentication**: Using mock login for prototype
2. **No GPS/Maps**: Location names are text strings
3. **No Real-time Updates**: Status updates require manual refresh
4. **No Payment System**: Focus on core booking logic
5. **Single Driver per Ride**: One driver can only handle one ride at a time
6. **Local Data**: All data is stored in memory (not persistent)

## 🧪 Testing

The app includes sample data for testing:
- 1 passenger with existing ride history
- 2 drivers with different availability statuses
- Sample rides in different states (completed, in progress)

## 📄 License

This project is created for educational and prototyping purposes.

## 👨‍💻 Author

Created as a junior software engineer project for ride-hailing app prototype.

---

**Note**: This is a prototype application designed for testing and demonstration purposes. It does not include real-world features like GPS, payments, or persistent data storage. 