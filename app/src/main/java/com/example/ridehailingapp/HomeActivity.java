package com.example.ridehailingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ridehailingapp.data.DataManager;
import com.example.ridehailingapp.models.User;
import com.example.ridehailingapp.models.Ride;

public class HomeActivity extends AppCompatActivity {
    TextView welcomeText,activeRideTitle,activeRideDetails;
    Button requestRideBtn,viewHistoryBtn,viewRequestsBtn,logoutBtn,activeRideCancelBtn,activeRideStatusBtn;
    View activeRideCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcomeText = findViewById(R.id.welcomeText);
        requestRideBtn = findViewById(R.id.requestRideBtn);
        viewHistoryBtn = findViewById(R.id.viewHistoryBtn);
        viewRequestsBtn = findViewById(R.id.viewRequestsBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        activeRideCard = findViewById(R.id.activeRideCard);
        activeRideTitle = findViewById(R.id.activeRideTitle);
        activeRideDetails = findViewById(R.id.activeRideDetails);
        activeRideCancelBtn = findViewById(R.id.activeRideCancelBtn);
        activeRideStatusBtn = findViewById(R.id.activeRideStatusBtn);

        DataManager dataManager = DataManager.getInstance();
        User currentUser = dataManager.getCurrentUser();

        updateRequestRideButton();
        showActiveRideCard();

        if (currentUser != null) {
            welcomeText.setText("Welcome, " + currentUser.getName() + "!");

            if (currentUser.isPassenger()) {
                requestRideBtn.setVisibility(Button.VISIBLE);
                viewHistoryBtn.setVisibility(Button.VISIBLE);
                viewRequestsBtn.setVisibility(Button.GONE);
                
                requestRideBtn.setText("Request a Ride");
                viewHistoryBtn.setText("View Ride History");
                
                if (dataManager.hasActiveRide(currentUser.getId())) {
                    requestRideBtn.setText("Active Ride in Progress");
                    requestRideBtn.setEnabled(false);
                }
            } else if (currentUser.isDriver()) {
                requestRideBtn.setVisibility(Button.GONE);
                viewHistoryBtn.setVisibility(Button.VISIBLE);
                viewRequestsBtn.setVisibility(Button.VISIBLE);
                
                viewHistoryBtn.setText("View My Rides");
                viewRequestsBtn.setText("View Ride Requests");
            }
        }

        requestRideBtn.setOnClickListener(v -> {
            startActivity(new Intent(this,RideRequestActivity.class));
        });

        viewHistoryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this,RideHistoryActivity.class);
            startActivity(intent);
        });

        viewRequestsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this,DriverRequestsActivity.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v -> {
            dataManager.logout();
            Intent intent = new Intent(this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRequestRideButton();
        showActiveRideCard();
    }

    private void updateRequestRideButton() {
        DataManager dataManager = DataManager.getInstance();
        User currentUser = dataManager.getCurrentUser();
        if (currentUser != null && currentUser.isPassenger()) {
            if (dataManager.hasActiveRide(currentUser.getId())) {
                requestRideBtn.setText("Active Ride in Progress");
                requestRideBtn.setEnabled(false);
            } else {
                requestRideBtn.setText("Request a Ride");
                requestRideBtn.setEnabled(true);
            }
        }
    }

    private void showActiveRideCard() {
        DataManager dataManager = DataManager.getInstance();
        User currentUser = dataManager.getCurrentUser();
        if (currentUser != null) {
            boolean isDriver = currentUser.isDriver();
            Ride activeRide = dataManager.getActiveRideForUser(currentUser.getId(),isDriver);
            if (activeRide != null) {
                activeRideCard.setVisibility(View.VISIBLE);
                StringBuilder details = new StringBuilder();
                details.append("Status: ").append(activeRide.getStatus()).append("\n");
                details.append("Pickup: ").append(activeRide.getPickupLocation()).append("\n");
                details.append("Drop: ").append(activeRide.getDropLocation()).append("\n");
                details.append("Type: ").append(activeRide.getRideType()).append("\n");
                details.append("Price: $").append(String.format("%.2f",activeRide.getPrice()));
                activeRideDetails.setText(details.toString());

                if (currentUser.isPassenger() && ("Requested".equals(activeRide.getStatus()) || "Accepted".equals(activeRide.getStatus()))) {
                    activeRideCancelBtn.setVisibility(View.VISIBLE);
                    activeRideCancelBtn.setOnClickListener(v -> {
                        boolean success = dataManager.cancelRide(activeRide.getId());
                        if (success) {
                            showActiveRideCard();
                            updateRequestRideButton();
                        }
                    });
                } else {
                    activeRideCancelBtn.setVisibility(View.GONE);
                }
                if (currentUser.isDriver() && ("Accepted".equals(activeRide.getStatus()) || "In Progress".equals(activeRide.getStatus()))) {
                    activeRideStatusBtn.setVisibility(View.VISIBLE);
                    activeRideStatusBtn.setOnClickListener(v -> {
                        String currentStatus = activeRide.getStatus();
                        String newStatus = null;
                        if ("Accepted".equals(currentStatus)) newStatus = "In Progress";
                        else if ("In Progress".equals(currentStatus)) newStatus = "Completed";
                        if (newStatus != null) {
                            boolean success = dataManager.updateRideStatus(activeRide.getId(),newStatus);
                            if (success) showActiveRideCard();
                        }
                    });
                } else {
                    activeRideStatusBtn.setVisibility(View.GONE);
                }
            } else {
                activeRideCard.setVisibility(View.GONE);
            }
        }
    }
}