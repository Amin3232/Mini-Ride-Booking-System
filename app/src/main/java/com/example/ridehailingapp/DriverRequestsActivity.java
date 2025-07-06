package com.example.ridehailingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ridehailingapp.data.DataManager;
import com.example.ridehailingapp.models.Ride;
import com.example.ridehailingapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class DriverRequestsActivity extends AppCompatActivity {
    ListView requestsListView;
    TextView emptyText;
    private List<Ride> requestedRides;
    private DriverRequestsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_requests);

        requestsListView = findViewById(R.id.requestsListView);
        emptyText = findViewById(R.id.emptyText);
        Button backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> finish());

        loadRequestedRides();

        requestsListView.setOnItemClickListener((parent,view,position,id) -> {
            Ride selectedRide = requestedRides.get(position);
            showAcceptDialog(selectedRide);
        });
    }

    private void loadRequestedRides() {
        DataManager dataManager = DataManager.getInstance();
        User currentUser = dataManager.getCurrentUser();
        int driverId = currentUser != null ? currentUser.getId() : -1;
        requestedRides = dataManager.getRequestedRidesForDriver(driverId);
        
        if (requestedRides.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            requestsListView.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            requestsListView.setVisibility(View.VISIBLE);
            
            adapter = new DriverRequestsAdapter(requestedRides,driverId);
            requestsListView.setAdapter(adapter);
        }
    }

    private void showAcceptDialog(Ride ride) {
        User currentUser = DataManager.getInstance().getCurrentUser();
        if (currentUser == null || !currentUser.isDriver()) {
            Toast.makeText(this,"You are not authorized to accept rides",Toast.LENGTH_SHORT).show();
            return;
        }

        if (!currentUser.isAvailable()) {
            Toast.makeText(this,"You are currently on another ride",Toast.LENGTH_SHORT).show();
            return;
        }

        DataManager dataManager = DataManager.getInstance();
        double price = dataManager.calculatePrice(ride.getPickupLocation(),ride.getDropLocation(),ride.getRideType());

        new android.app.AlertDialog.Builder(this)
                .setTitle("Accept Ride Request")
                .setMessage(String.format("Do you want to accept this ride?\n\n" +
                        "Ride #%06d\n" +
                        "Pickup: %s\n" +
                        "Drop: %s\n" +
                        "Type: %s\n" +
                        "Estimated Price: $%.2f",
                        ride.getId(),
                        ride.getPickupLocation(),
                        ride.getDropLocation(),
                        ride.getRideType(),
                        price))
                .setPositiveButton("Accept",(dialog,which) -> {
                    acceptRide(ride);
                })
                .setNegativeButton("Cancel",null)
                .show();
    }

    private void acceptRide(Ride ride) {
        DataManager dataManager = DataManager.getInstance();
        User currentUser = dataManager.getCurrentUser();
        
        if (currentUser != null) {
            boolean success = dataManager.acceptRide(ride.getId(),currentUser.getId());
            if (success) {
                Toast.makeText(this,"Ride accepted successfully!",Toast.LENGTH_SHORT).show();
                loadRequestedRides();
            } else {
                Toast.makeText(this,"Failed to accept ride",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRequestedRides();
    }

    private class DriverRequestsAdapter extends ArrayAdapter<Ride> {
        private List<Ride> rides;
        private int driverId;

        public DriverRequestsAdapter(List<Ride> rides,int driverId) {
            super(DriverRequestsActivity.this,0,rides);
            this.rides = rides;
            this.driverId = driverId;
        }

        @Override
        public View getView(int position,View convertView,ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.driver_request_item,parent,false);
            }

            Ride ride = rides.get(position);
            DataManager dataManager = DataManager.getInstance();
            
            TextView requestInfoText = convertView.findViewById(R.id.requestInfoText);
            TextView requestDetailsText = convertView.findViewById(R.id.requestDetailsText);
            TextView requestTypeText = convertView.findViewById(R.id.requestTypeText);
            Button acceptBtn = convertView.findViewById(R.id.acceptBtn);
            Button rejectBtn = convertView.findViewById(R.id.rejectBtn);

            requestInfoText.setText("Request #" + String.format("%06d",ride.getId()));
            double price = dataManager.calculatePrice(ride.getPickupLocation(),ride.getDropLocation(),ride.getRideType());
            requestDetailsText.setText(String.format("%s → %s\n$%.2f",ride.getPickupLocation(),ride.getDropLocation(),price));
            requestTypeText.setText(ride.getRideType());

            acceptBtn.setEnabled(!dataManager.driverHasActiveRide(driverId));
            acceptBtn.setOnClickListener(v -> {
                if (dataManager.driverHasActiveRide(driverId)) {
                    Toast.makeText(getContext(),"You already have an active ride.",Toast.LENGTH_SHORT).show();
                    return;
                }
                User currentUser = dataManager.getCurrentUser();
                if (currentUser != null) {
                    boolean success = dataManager.acceptRide(ride.getId(),currentUser.getId());
                    if (success) {
                        Toast.makeText(getContext(),"Ride accepted successfully!",Toast.LENGTH_SHORT).show();
                        loadRequestedRides();
                    } else {
                        Toast.makeText(getContext(),"Failed to accept ride",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            rejectBtn.setOnClickListener(v -> {
                boolean success = dataManager.rejectRide(ride.getId(),driverId);
                if (success) {
                    Toast.makeText(getContext(),"Ride declined.",Toast.LENGTH_SHORT).show();
                    loadRequestedRides();
                } else {
                    Toast.makeText(getContext(),"Unable to decline ride.",Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }
    }
} 