package com.app.connectcare;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddEmergencyRequestActivity extends AppCompatActivity {
    private EditText etRequestTitle, etRequestDescription;
    private Button btnSubmitRequest;
    private ProgressBar progressBar;
    private DatabaseReference requestDatabaseRef;
    private String ngoId = "ngo_123"; // Replace with dynamic NGO ID if available

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_emergency_request);

        etRequestTitle = findViewById(R.id.etRequestTitle);
        etRequestDescription = findViewById(R.id.etRequestDescription);
        btnSubmitRequest = findViewById(R.id.btnSubmitRequest);
        progressBar = findViewById(R.id.progressBar);

        // Firebase reference
        requestDatabaseRef = FirebaseDatabase.getInstance().getReference("NGORequests").child(ngoId);

        btnSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitEmergencyRequest();
            }
        });
    }

    private void submitEmergencyRequest() {
        String title = etRequestTitle.getText().toString().trim();
        String description = etRequestDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        String requestId = requestDatabaseRef.push().getKey(); // Generate unique request ID

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("requestId", requestId);
        requestData.put("title", title);
        requestData.put("description", description);
        requestData.put("timestamp", System.currentTimeMillis());

        if (requestId != null) {
            requestDatabaseRef.child(requestId).setValue(requestData)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(AddEmergencyRequestActivity.this, "Request added!", Toast.LENGTH_SHORT).show();
                            finish(); // Close activity after success
                        } else {
                            Toast.makeText(AddEmergencyRequestActivity.this, "Failed to add request", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
