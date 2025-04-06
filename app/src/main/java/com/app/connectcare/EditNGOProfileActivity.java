package com.app.connectcare;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditNGOProfileActivity extends AppCompatActivity {

    private EditText etNgoName, etNgoContact, etNgoEmail, etNgoAddress, etNgoDescription;
    private Button btnSaveChanges;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ngo_profile);

        // Initialize UI components
        etNgoName = findViewById(R.id.etNgoName);
        etNgoContact = findViewById(R.id.etNgoContact);
        etNgoEmail = findViewById(R.id.etNgoEmail);
        etNgoAddress = findViewById(R.id.etNgoAddress);
        etNgoDescription = findViewById(R.id.etNgoDescription);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        // Load NGO data from SharedPreferences
        sharedPreferences = getSharedPreferences("ConnectCarePrefs", MODE_PRIVATE);
        loadNgoDetails();

        // Save changes button event
        btnSaveChanges.setOnClickListener(view -> saveNgoDetails());
    }

    private void loadNgoDetails() {
        // Fetch saved details
        etNgoName.setText(sharedPreferences.getString("ngoName", ""));
        etNgoContact.setText(sharedPreferences.getString("ngoContact", ""));
        etNgoEmail.setText(sharedPreferences.getString("ngoEmail", ""));
        etNgoAddress.setText(sharedPreferences.getString("ngoAddress", ""));
        etNgoDescription.setText(sharedPreferences.getString("ngoDescription", ""));
    }

    private void saveNgoDetails() {
        // Save updated details
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ngoName", etNgoName.getText().toString().trim());
        editor.putString("ngoContact", etNgoContact.getText().toString().trim());
        editor.putString("ngoEmail", etNgoEmail.getText().toString().trim());
        editor.putString("ngoAddress", etNgoAddress.getText().toString().trim());
        editor.putString("ngoDescription", etNgoDescription.getText().toString().trim());
        editor.apply();

        // Confirmation message
        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

        // Close activity after saving
        finish();
    }
}
