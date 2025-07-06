package com.example.ridehailingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ridehailingapp.data.DataManager;
import com.example.ridehailingapp.models.Ride;
import com.example.ridehailingapp.models.User;

public class RideRequestActivity extends AppCompatActivity {
    EditText pickupLocationInput,dropLocationInput;
    RadioGroup rideTypeGroup;
    RadioButton bikeRadio,miniCarRadio,carRadio;
    Button requestBtn,backBtn;
    TextView priceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_request);

        pickupLocationInput = findViewById(R.id.pickupLocation);
        dropLocationInput = findViewById(R.id.dropLocation);
        rideTypeGroup = findViewById(R.id.rideTypeGroup);
        bikeRadio = findViewById(R.id.bikeRadio);
        miniCarRadio = findViewById(R.id.miniCarRadio);
        carRadio = findViewById(R.id.carRadio);
        requestBtn = findViewById(R.id.requestBtn);
        backBtn = findViewById(R.id.backBtn);
        priceText = findViewById(R.id.priceText);

        miniCarRadio.setChecked(true);
        updatePrice();

        rideTypeGroup.setOnCheckedChangeListener((group,checkedId) -> {
            updatePrice();
        });

        pickupLocationInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s,int start,int count,int after) {}

            @Override
            public void onTextChanged(CharSequence s,int start,int before,int count) {
                updatePrice();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        dropLocationInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s,int start,int count,int after) {}

            @Override
            public void onTextChanged(CharSequence s,int start,int before,int count) {
                updatePrice();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        requestBtn.setOnClickListener(v -> {
            String pickupLocation = pickupLocationInput.getText().toString().trim();
            String dropLocation = dropLocationInput.getText().toString().trim();
            String selectedRideType = getSelectedRideType();

            if (pickupLocation.isEmpty() || dropLocation.isEmpty()) {
                Toast.makeText(this,"Please fill in all fields",Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedRideType == null) {
                Toast.makeText(this,"Please select a ride type",Toast.LENGTH_SHORT).show();
                return;
            }

            DataManager dataManager = DataManager.getInstance();
            User currentUser = dataManager.getCurrentUser();
            if (dataManager.hasActiveRide(currentUser.getId())) {
                Toast.makeText(this,"You already have an active ride. Please complete or cancel your current ride first.",Toast.LENGTH_LONG).show();
                return;
            }

            double price = dataManager.calculatePrice(pickupLocation,dropLocation,selectedRideType);
            
            Ride ride = new Ride(generateRideId(),currentUser.getId(),pickupLocation,dropLocation,selectedRideType);
            ride.setPrice(price);

            dataManager.addRide(ride);

            String message = String.format("Ride requested successfully!\n\nPickup: %s\nDrop: %s\nType: %s\nPrice: $%.2f",pickupLocation,dropLocation,selectedRideType,price);
            
            new AlertDialog.Builder(this)
                .setTitle("Ride Requested")
                .setMessage(message)
                .setPositiveButton("View Status",(dialog,which) -> {
                    Intent intent = new Intent(RideRequestActivity.this,RideStatusActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("OK",null)
                .show();
        });

        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    private String getSelectedRideType() {
        if (bikeRadio.isChecked()) {
            return "Bike";
        } else if (miniCarRadio.isChecked()) {
            return "Mini Car";
        } else if (carRadio.isChecked()) {
            return "Car";
        }
        return null;
    }

    private int generateRideId() {
        return 100000 + (int)(Math.random() * 900000);
    }

    private void updatePrice() {
        String selectedRideType = getSelectedRideType();
        if (selectedRideType != null) {
            String pickupLocation = pickupLocationInput.getText().toString().trim();
            String dropLocation = dropLocationInput.getText().toString().trim();
            
            if (!pickupLocation.isEmpty() && !dropLocation.isEmpty()) {
                double price = DataManager.getInstance().calculatePrice(pickupLocation,dropLocation,selectedRideType);
                priceText.setText(String.format("Estimated Price: $%.2f",price));
            } else {
                priceText.setText("Enter locations to see price");
            }
        } else {
            priceText.setText("Select ride type to see price");
        }
    }
}