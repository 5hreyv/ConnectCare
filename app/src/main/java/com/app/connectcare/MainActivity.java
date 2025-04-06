package com.app.connectcare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if user is already logged in and redirect accordingly
        checkUserStatus();

        Button btnGetStarted = findViewById(R.id.btnGetStarted);
        TextView txtLearnMore = findViewById(R.id.txtLearnMore);

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.logo);

        btnGetStarted.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));

        txtLearnMore.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AboutActivity.class)));
    }

    private void checkUserStatus() {
        SharedPreferences preferences = getSharedPreferences("ConnectCarePrefs", MODE_PRIVATE);
        String userType = preferences.getString("userType", "");

        if (!userType.isEmpty()) {
            navigateToDashboard(userType);
        }
    }

    private void navigateToDashboard(String userType) {
        Intent intent;
        if ("volunteer".equals(userType)) {
            intent = new Intent(this, VolunteerDashboardActivity.class);
        } else if ("ngo".equals(userType)) {
            intent = new Intent(this, NGODashboardActivity.class);
        } else {
            intent = new Intent(this, OnboardingActivity.class);
        }
        startActivity(intent);
        finish();
    }

    public void openNGODirectory(View view) {
        try {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=NGO near me");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(this, "Google Maps is not installed!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error opening Google Maps: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void openLearnMorePage(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }
}
