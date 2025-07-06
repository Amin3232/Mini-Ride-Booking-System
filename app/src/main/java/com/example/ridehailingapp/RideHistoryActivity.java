package com.example.ridehailingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ridehailingapp.data.DataManager;
import com.example.ridehailingapp.models.Ride;
import com.example.ridehailingapp.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RideHistoryActivity extends AppCompatActivity {
    ListView rideListView;
    TextView emptyText;
    private List<Ride> userRides;
    private RideHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);

        rideListView = findViewById(R.id.rideListView);
        emptyText = findViewById(R.id.emptyText);
        Button backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> finish());

        loadRideHistory();

        rideListView.setOnItemClickListener((parent,view,position,id) -> {
            Ride selectedRide = userRides.get(position);
            Intent intent = new Intent(this,RideStatusActivity.class);
            intent.putExtra("rideId",selectedRide.getId());
            startActivity(intent);
        });
    }

    private void loadRideHistory() {
        DataManager dataManager = DataManager.getInstance();
        User currentUser = dataManager.getCurrentUser();
        
        if (currentUser != null) {
            userRides = dataManager.getRidesByUserId(currentUser.getId());
            
            if (userRides.isEmpty()) {
                emptyText.setVisibility(View.VISIBLE);
                rideListView.setVisibility(View.GONE);
            } else {
                emptyText.setVisibility(View.GONE);
                rideListView.setVisibility(View.VISIBLE);
                
                adapter = new RideHistoryAdapter(userRides);
                rideListView.setAdapter(adapter);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRideHistory();
    }

    private class RideHistoryAdapter extends ArrayAdapter<Ride> {
        private List<Ride> rides;

        public RideHistoryAdapter(List<Ride> rides) {
            super(RideHistoryActivity.this,0,rides);
            this.rides = rides;
        }

        @Override
        public View getView(int position,View convertView,ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.ride_history_item,parent,false);
            }

            Ride ride = rides.get(position);
            DataManager dataManager = DataManager.getInstance();
            
            TextView rideInfoText = convertView.findViewById(R.id.rideInfoText);
            TextView rideDetailsText = convertView.findViewById(R.id.rideDetailsText);
            TextView rideStatusText = convertView.findViewById(R.id.rideStatusText);

            rideInfoText.setText("Ride #" + String.format("%06d",ride.getId()));
            
            double price = dataManager.calculatePrice(ride.getPickupLocation(),ride.getDropLocation(),ride.getRideType());
            
            rideDetailsText.setText(String.format("%s → %s\n%s • $%.2f",ride.getPickupLocation(),ride.getDropLocation(),ride.getRideType(),price));
            
            rideStatusText.setText(ride.getStatus());

            switch (ride.getStatus()) {
                case "Completed":
                    rideStatusText.setBackgroundResource(R.drawable.status_badge_background);
                    rideStatusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    break;
                case "In Progress":
                    rideStatusText.setBackgroundResource(R.drawable.status_badge_background);
                    rideStatusText.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                    break;
                case "Accepted":
                    rideStatusText.setBackgroundResource(R.drawable.status_badge_background);
                    rideStatusText.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                    break;
                default:
                    rideStatusText.setBackgroundResource(R.drawable.status_badge_background);
                    rideStatusText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    break;
            }

            return convertView;
        }
    }
}
