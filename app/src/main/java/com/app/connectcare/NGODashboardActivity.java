package com.app.connectcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NGODashboardActivity extends AppCompatActivity {
    private TextView tvNgoDetails, tvVolunteerUpdates;
    private Button btnPitchAid, btnManageEvents;
    private CardView cardNgoDetails;
    private BottomNavigationView bottomNavigationView;

    private DatabaseReference ngoDatabaseRef, volunteerUpdatesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_dashboard);

        // Initialize UI components
        initUI();

        // Initialize Firebase Database References
        ngoDatabaseRef = FirebaseDatabase.getInstance().getReference("NGOs").child("ngo_123");
        volunteerUpdatesRef = FirebaseDatabase.getInstance().getReference("NGOUpdates");

        // Fetch and display NGO details
        fetchNGODetails();

        // Listen for new volunteer updates in real-time
        listenForVolunteerUpdates();

        // Handle Bottom Navigation
        setupBottomNavigation();
    }

    private void initUI() {
        tvNgoDetails = findViewById(R.id.tvNgoDetails);
        tvVolunteerUpdates = findViewById(R.id.tvVolunteerUpdates);
        btnPitchAid = findViewById(R.id.btnPitchAid);
        btnManageEvents = findViewById(R.id.btnManageEvents);
        cardNgoDetails = findViewById(R.id.cardNgoDetails);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void fetchNGODetails() {
        ngoDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ngoName = snapshot.child("name").getValue(String.class);
                    String ngoId = snapshot.child("id").getValue(String.class);

                    if (ngoName != null && ngoId != null) {
                        tvNgoDetails.setText(String.format("%s - %s", ngoName, ngoId));
                    }
                } else {
                    showToast("NGO details not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Failed to load NGO details!");
                Log.e("FirebaseError", error.getMessage());
            }
        });
    }

    private void listenForVolunteerUpdates() {
        volunteerUpdatesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder updates = new StringBuilder();

                for (DataSnapshot updateSnapshot : snapshot.getChildren()) {
                    String message = updateSnapshot.child("message").getValue(String.class);
                    if (message != null) {
                        updates.append("• ").append(message).append("\n");
                    }
                }

                tvVolunteerUpdates.setText(updates.length() > 0 ? updates.toString() : "No new volunteer updates.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Failed to load volunteer updates!");
            }
        });
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            Class<?> targetActivity = null;
            if (id == R.id.nav_dashboard) {
                return true;
            } else if (id == R.id.nav_profile) {
                targetActivity = NgoProfileActivity.class;
            } else if (id == R.id.nav_ngo_add_emergency_request) {
                targetActivity = AddEmergencyRequestActivity.class;
            } else if (id == R.id.nav_ngo_notifications) {
                targetActivity = NotificationsActivity.class;
            } else if (id == R.id.nav_ngo_settings) {
                targetActivity = SettingsActivity.class;
            }

            if (targetActivity != null) {
                startActivity(new Intent(this, targetActivity));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();  // Ensures only one instance of the activity runs
            }

            return true;
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
