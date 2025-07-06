package com.example.ridehailingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ridehailingapp.data.DataManager;
import com.example.ridehailingapp.models.Ride;
import com.example.ridehailingapp.models.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RideStatusActivity extends AppCompatActivity {
    TextView rideIdText,pickupText,dropText,rideTypeText,statusText,driverText,priceText,dateText;
    Button updateStatusBtn,backBtn,cancelRideBtn;
    private int rideId;
    private Ride currentRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_status);

        rideIdText = findViewById(R.id.rideIdText);
        pickupText = findViewById(R.id.pickupText);
        dropText = findViewById(R.id.dropText);
        rideTypeText = findViewById(R.id.rideTypeText);
        statusText = findViewById(R.id.statusText);
        driverText = findViewById(R.id.driverText);
        priceText = findViewById(R.id.priceText);
        dateText = findViewById(R.id.dateText);
        updateStatusBtn = findViewById(R.id.updateStatusBtn);
        backBtn = findViewById(R.id.backBtn);
        cancelRideBtn = findViewById(R.id.cancelRideBtn);

        rideId = getIntent().getIntExtra("rideId",-1);
        if (rideId == -1) {
            Toast.makeText(this,"Invalid ride ID",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadRideDetails();

        updateStatusBtn.setOnClickListener(v -> {
            updateRideStatus();
        });

        cancelRideBtn.setOnClickListener(v -> {
            DataManager dataManager = DataManager.getInstance();
            boolean success = dataManager.cancelRide(rideId);
            if (success) {
                Toast.makeText(this,"Ride cancelled.",Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this,"Unable to cancel ride.",Toast.LENGTH_SHORT).show();
            }
        });

        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    private void loadRideDetails() {
        DataManager dataManager = DataManager.getInstance();
        currentRide = dataManager.getRideById(rideId);
        
        if (currentRide != null) {
            rideIdText.setText("Ride ID: " + String.format("%06d",currentRide.getId()));
            pickupText.setText("Pickup: " + currentRide.getPickupLocation());
            dropText.setText("Drop: " + currentRide.getDropLocation());
            rideTypeText.setText("Type: " + dataManager.getRideTypeDisplayName(currentRide.getRideType()));
            statusText.setText("Status: " + currentRide.getStatus());
            priceText.setText("Price: $" + String.format("%.2f",currentRide.getPrice()));
            
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm",Locale.getDefault());
            String dateStr = sdf.format(new Date(currentRide.getRequestTime()));
            dateText.setText("Requested: " + dateStr);
            
            if (currentRide.getDriverId() > 0) {
                User driver = dataManager.getUserById(currentRide.getDriverId());
                if (driver != null) {
                    driverText.setText("Driver: " + driver.getName());
                } else {
                    driverText.setText("Driver: Assigned");
                }
            } else {
                driverText.setText("Driver: Not assigned yet");
            }

            User currentUser = dataManager.getCurrentUser();
            if (currentUser != null && currentUser.isDriver() && (currentRide.isAccepted() || currentRide.isInProgress())) {
                updateStatusBtn.setVisibility(Button.VISIBLE);
            } else {
                updateStatusBtn.setVisibility(Button.GONE);
            }
            if (currentUser != null && currentUser.isPassenger() && ("Requested".equals(currentRide.getStatus()) || "Accepted".equals(currentRide.getStatus()))) {
                cancelRideBtn.setVisibility(Button.VISIBLE);
            } else {
                cancelRideBtn.setVisibility(Button.GONE);
            }
        } else {
            Toast.makeText(this,"Ride not found",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateRideStatus() {
        if (currentRide == null) return;

        DataManager dataManager = DataManager.getInstance();
        String currentStatus = currentRide.getStatus();
        String newStatus = "";

        if ("Accepted".equals(currentStatus)) {
            newStatus = "In Progress";
        } else if ("In Progress".equals(currentStatus)) {
            newStatus = "Completed";
        }

        if (!newStatus.isEmpty()) {
            boolean success = dataManager.updateRideStatus(rideId,newStatus);
            if (success) {
                Toast.makeText(this,"Status updated to: " + newStatus,Toast.LENGTH_SHORT).show();
                loadRideDetails();
            } else {
                Toast.makeText(this,"Failed to update status",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRideDetails();
    }
}