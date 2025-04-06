package com.app.connectcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

public class OnboardingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        Button btnVolunteer = findViewById(R.id.btnVolunteer);
        Button btnNGO = findViewById(R.id.btnNGO);

        btnVolunteer.setOnClickListener(view -> {
            saveUserType("volunteer");
            navigateToDashboard();
        });

        btnNGO.setOnClickListener(view -> {
            saveUserType("ngo");
            navigateToNGORegistration();
        });
    }

    private void navigateToNGORegistration() {
        startActivity(new Intent(this, NgoRegistrationActivity.class));
        finish();
    }

    private void saveUserType(String userType) {
        getSharedPreferences("ConnectCarePrefs", MODE_PRIVATE)
                .edit()
                .putString("userType", userType)
                .apply();
    }

    private void navigateToDashboard() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }
}
