package com.app.connectcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_dashboard);

        // Initialize Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Log.d(TAG, "BottomNav clicked: " + getResources().getResourceEntryName(itemId));

            if (itemId == R.id.nav_dashboard) {
                return true; // Stay on Dashboard
            } else if (itemId == R.id.nav_profile) {
                navigateTo(ProfileActivity.class);
                return true;
            } else if (itemId == R.id.nav_donate) {
                Intent donateIntent = new Intent(this, DonationActivity.class);
                donateIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(donateIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (itemId == R.id.nav_more) {
                navigateTo(MoreOptionsActivity.class);
                return true;
            } else if (itemId == R.id.nav_logout) {
                navigateTo(LoginActivity.class);
                finish(); // Close Dashboard after logout
                return true;
            }
            return false;
        });

        // Set the selected item to Dashboard
        bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
    }

    // Optimized navigation function
    private void navigateTo(Class<?> targetActivity) {
        Log.d(TAG, "Navigating to: " + targetActivity.getSimpleName());
        startActivity(new Intent(this, targetActivity));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    // Handle Button Clicks
    public void openNGODirectory(View view) {
        navigateTo(NearbyNGOActivity.class);
    }

    public void openDonationPage(View view) {
        navigateTo(DonationActivity.class);
    }

    public void openVolunteerPage(View view) {
        navigateTo(VolunteerActivity.class);
    }

    public void openEventsPage(View view) {
        navigateTo(EventActivity.class);
    }
}
