package com.app.connectcare;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NgoRegistrationActivity extends AppCompatActivity {
    private EditText etNgoName, etNgoContact, etNgoEmail, etNgoAddress, etNgoDescription;
    private Button btnSubmitNgo;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_registration);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("NGOs");
        checkAuthConnection();


        // Initialize UI components
        etNgoName = findViewById(R.id.etNgoName);
        etNgoContact = findViewById(R.id.etNgoContact);
        etNgoEmail = findViewById(R.id.etNgoEmail);
        etNgoAddress = findViewById(R.id.etNgoAddress);
        etNgoDescription = findViewById(R.id.etNgoDescription);
        btnSubmitNgo = findViewById(R.id.btnRegisterNgo);

        // Handle NGO Registration
        btnSubmitNgo.setOnClickListener(view -> registerNgo());
    }

    private void checkAuthConnection() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d("FirebaseAuth", "User is logged in: " + user.getUid());
        } else {
            Log.d("FirebaseAuth", "No user is logged in.");
        }
    }

    private void registerNgo() {
        String name = etNgoName.getText().toString().trim();
        String contact = etNgoContact.getText().toString().trim();
        String email = etNgoEmail.getText().toString().trim();
        String address = etNgoAddress.getText().toString().trim();
        String description = etNgoDescription.getText().toString().trim();

        if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || address.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate Unique NGO ID (Primary Key)
        String ngoId = databaseReference.push().getKey();

        // Create NGO Object
        Ngo ngo = new Ngo(ngoId, name, contact, email, address, description);

        // Store in Firebase Database
        databaseReference.child(ngoId).setValue(ngo)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "NGO Registered Successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, NGODashboardActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to register NGO", Toast.LENGTH_LONG).show());
    }

    // NGO Model Class
    public static class Ngo {
        public String ngoId, name, contact, email, address, description;

        public Ngo() {
            // Default constructor for Firebase
        }

        public Ngo(String ngoId, String name, String contact, String email, String address, String description) {
            this.ngoId = ngoId;
            this.name = name;
            this.contact = contact;
            this.email = email;
            this.address = address;
            this.description = description;
        }
    }
}
